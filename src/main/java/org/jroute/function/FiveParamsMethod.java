package org.jroute.function;

@FunctionalInterface
public interface FiveParamsMethod<T, K, V, U, X> {
    void call(T a, K b, V c, U d, X e);
}