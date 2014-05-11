package org.jroute.function;

@FunctionalInterface
public interface FourParamsMethod<T, K, V, U> {
    void call(T a, K b, V c, U d);
}