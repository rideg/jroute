package org.jroute.socket;

import org.jroute.collection.buffer.OffHeapByteBuffer;
import org.jroute.util.WaitStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SocketReader implements Runnable {

    private final AtomicReference<Socket> offered;
    private final WaitStrategy waitStrategy;
    private final AtomicBoolean shouldRun;
    private final Socket[] sockets;
    private final OffHeapByteBuffer[] buffers;
    private int count;
    private Thread runner;

    public SocketReader(final int size, final WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
        sockets = new Socket[size];
        shouldRun = new AtomicBoolean();
        offered = new AtomicReference<>();
        buffers = new OffHeapByteBuffer[size];
    }

    public boolean offer(final Socket socket) {
        return offered.compareAndSet(null, socket);
    }

    @Override
    public void run() {
        allocateBuffers();
        while (shouldRun.get()) {
            checkForNewSocket();
            int gap = 0;
            boolean noRead = true;
            for (int i = 0; i < count; i++) {
                try {
                    final InputStream stream = sockets[i].getInputStream();
                    final OffHeapByteBuffer buffer = buffers[i];
                    if (buffer.readAvailable(stream) > 0) {
                        noRead = false;
                        if (canForward(buffer)) {

                        }
                    }
                    compactIfNeeded(gap, i);
                } catch (InterruptedIOException e) {
                    shouldRun.set(false);
                    break;
                } catch (IOException e) {
                    try {
                        sockets[i].close();
                    } catch (IOException ignored) {
                    }
                    gap++;
                }
            }
            count -= gap;
            if (noRead) {
                waitStrategy.doWait();
            }
        }
        closeSockets();
    }

    private boolean canForward(final OffHeapByteBuffer buffer) {
        if (buffer.getOffset() >= 4) {
            for (int i = buffer.getOffset(); i >= 4; i--) {
                if (buffer.get(i) == '\n' &&
                        buffer.get(i - 1) == '\r' &&
                        buffer.get(i - 2) == '\n' &&
                        buffer.get(i - 3) == '\r') {
                    buffer.mark(i + 1);
                    return true;
                }
            }
        }
        return false;
    }

    private void compactIfNeeded(final int gap, final int i) {
        if (gap > 0) {
            sockets[i - gap] = sockets[i];
            buffers[i - gap] = buffers[i];
        }
    }

    private void allocateBuffers() {
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new OffHeapByteBuffer(ConnectionHandler.RECEIVE_BUFFER_SIZE);
        }
    }

    private void checkForNewSocket() {
        if (count < sockets.length - 1) {
            final Socket socket = offered.getAndSet(null);
            if (socket != null) {
                sockets[count] = socket;
                buffers[count++].clear();
            }
        }
    }

    public void stop() throws InterruptedException {
        shouldRun.set(false);
        runner.interrupt();
        runner.join(TimeUnit.SECONDS.toMillis(5));
    }

    private void closeSockets() {
        for (int i = 0; i < count; i++) {
            try (final Socket sock = sockets[i]) {
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException ignored) {

            }
        }
    }

    public void start() {
        if (!shouldRun.get()) {
            runner = new Thread(this);
            runner.setName("JRoute - socket reader thread");
            runner.setDaemon(true);
            runner.setPriority(Thread.MAX_PRIORITY);
            runner.start();
        }
    }
}
