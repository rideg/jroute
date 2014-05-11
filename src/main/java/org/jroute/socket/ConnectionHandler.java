package org.jroute.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler {

    public void open(final int port) throws IOException, InterruptedException {

        AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(),
                Runtime.getRuntime().availableProcessors());

        AsynchronousServerSocketChannel socket = AsynchronousServerSocketChannel.open(group).bind(
                new InetSocketAddress(8080));

        socket.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(final AsynchronousSocketChannel worker, final Object attachment) {
            }

            @Override
            public void failed(final Throwable exc, final Object attachment) {

            }
        });

        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }
}
