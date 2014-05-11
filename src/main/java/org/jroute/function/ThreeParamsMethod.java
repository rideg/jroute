package org.jroute.function;

@FunctionalInterface
public interface ThreeParamsMethod<T, K, V> {
    void call(T a, K b, V c);
}