package org.jroute.function;

@FunctionalInterface
public interface TwoParamsMethod<T, K> {
    void call(T a, K b);
}