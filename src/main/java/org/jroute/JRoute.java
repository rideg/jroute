package org.jroute;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import org.jroute.function.FiveParamsMethod;
import org.jroute.function.FourParamsMethod;
import org.jroute.function.NoParamMethod;
import org.jroute.function.OneParamMethod;
import org.jroute.function.ThreeParamsMethod;
import org.jroute.function.TwoParamsMethod;
import org.jroute.route.endpoint.MethodCaller;
import org.objenesis.ObjenesisStd;

public class JRoute {

    public static <T> T to(final Class<T> c) {
        return createProxy(createProxyClass(c));
    }

    public static <T> T to(final Class<T> c, final Object... args) {
        return createProxy(createProxyClass(c), args);
    }

    @SuppressWarnings("unchecked")
    private static <T> T createProxy(final Class<?> proxy, final Object... args) {
        Enhancer.registerCallbacks(proxy, new Callback[] { new Extractor(args) });
        return (T) new ObjenesisStd().newInstance(proxy);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> createProxyClass(final Class<T> c) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        enhancer.setCallbackType(Extractor.class);
        return enhancer.createClass();
    }

    public static synchronized MethodCaller call(final NoParamMethod f) {
        f.call();
        return Extractor.caller;
    }

    public static synchronized <T> MethodCaller call(final OneParamMethod<T> f) {
        f.call(null);
        return Extractor.caller;
    }

    public static synchronized <T, K> MethodCaller call(final TwoParamsMethod<T, K> f) {
        f.call(null, null);
        return Extractor.caller;
    }

    public static synchronized <T, K, V> MethodCaller call(final ThreeParamsMethod<T, K, V> f) {
        f.call(null, null, null);
        return Extractor.caller;
    }

    public static synchronized <T, K, V, U> MethodCaller call(final FourParamsMethod<T, K, V, U> f) {
        f.call(null, null, null, null);
        return Extractor.caller;
    }

    public static synchronized <T, K, V, U, X> MethodCaller call(final FiveParamsMethod<T, K, V, U, X> f) {
        f.call(null, null, null, null, null);
        return Extractor.caller;
    }

}
