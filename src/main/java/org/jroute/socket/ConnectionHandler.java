package org.jroute.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionHandler {

    public static final int RECEIVE_BUFFER_SIZE = 16 * 1024;

    private final AtomicBoolean shouldRun = new AtomicBoolean(true);
    private final SocketReader socketReader;

    public ConnectionHandler(final SocketReader socketReader) {
        this.socketReader = socketReader;
    }

    public void open(final int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket();

        serverSocket.setReuseAddress(true);
        serverSocket.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
        serverSocket.setSoTimeout(5);

        final Thread acceptorThread = new Thread(() -> {
            try (final ServerSocket socket = serverSocket) {
                while (shouldRun.get()) {
                    try {
                        socketReader.offer(socket.accept());
                    } catch (SocketTimeoutException ignored) {
                    }
                }
            } catch (IOException ignored) {
            }
        });
        acceptorThread.setDaemon(true);
        acceptorThread.setName("JRoute - acceptor thread: " + port);
        serverSocket.bind(new InetSocketAddress("localhost", port));

        acceptorThread.start();
    }

    public void stop() {
        shouldRun.set(false);
    }

}
