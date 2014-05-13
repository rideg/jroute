package org.jroute.collection;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AsynchExecutor<T> extends AsynchExecutorBase {

    public static <V> AsynchExecutor<V> execute(final Supplier<V> f) {
        return new AsynchExecutor<>(f);
    }

    private AsynchExecutor(final Supplier<T> f) {

    }

    public <R> AsynchExecutor<R> then(final Function<T, R> f) {
        return null;
    }

    public AsynchExecutorBase then(final Consumer<T> f) {
        return this;
    }

}
