package org.jroute.function;

@FunctionalInterface
public interface OneParamMethod<T> {
    void call(T a);
}