package org.jroute.socket;

import org.jroute.util.WaitStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SocketReader implements Runnable {

    private final AtomicReference<Socket> offered;
    private final WaitStrategy waitStrategy;
    private final AtomicBoolean shouldRun;
    private final Socket[] sockets;
    private final ByteBuffer[] buffers;
    private int count;
    private Thread runner;

    public SocketReader(final int size, final WaitStrategy waitStrategy) {
        this.waitStrategy = waitStrategy;
        sockets = new Socket[size];
        shouldRun = new AtomicBoolean();
        offered = new AtomicReference<>();
        buffers = new ByteBuffer[size];
    }

    public boolean offer(final Socket socket) {
        return offered.compareAndSet(null, socket);
    }

    @Override
    public void run() {
        allocateBuffers();
        final byte[] tmp = new byte[ConnectionHandler.RECEIVE_BUFFER_SIZE];
        while (shouldRun.get()) {
            checkForNewSocket();
            int gap = 0;
            boolean noRead = true;
            for (int i = 0; i < count; i++) {
                try {
                    final InputStream stream = sockets[i].getInputStream();
                    final int available = stream.available();
                    if (available > 0) {
                        noRead = false;
                        ByteBuffer buffer = buffers[i];
                        if (available > buffer.remaining()) {
                            throw new IOException("Incoming package is too big");
                        }
                        stream.read(tmp, 0, available);
                        buffer.put(tmp, 0, available);
                        if (canForward(buffer)) {
                            buffer.flip();
                            //
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

    private boolean canForward(final ByteBuffer buffer) {
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
            buffers[i] = ByteBuffer.allocateDirect(ConnectionHandler.RECEIVE_BUFFER_SIZE);
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
