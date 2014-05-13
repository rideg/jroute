package org.jroute.collection;

import java.util.function.Consumer;

public abstract class AsynchExecutorBase {

    public AsynchExecutorBase exception(final Consumer<Throwable> f) {
        return this;
    }

    public AsynchExecutorBase start() {
        return this;
    }

    public boolean isFinished() {
        return false;
    }

    public void cancel() {

    }

}
