package org.jroute;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.util.Arrays;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.jroute.route.endpoint.Instantator;
import org.jroute.route.endpoint.MethodCaller;

class Extractor implements MethodInterceptor {

    static MethodCaller caller;
    private final Object[] args;

    public Extractor(final Object... args) {
        this.args = args;
    }

    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
            throws Throwable {
        Class<?> clazz = obj.getClass().getSuperclass();
        caller = new MethodCaller(instantator(clazz), realMethod(clazz, method));
        return null;
    }

    private Method realMethod(final Class<?> clazz, final Method method) throws ReflectiveOperationException {
        return clazz.getMethod(method.getName(), method.getParameterTypes());
    }

    private Instantator instantator(final Class<?> clazz) throws ReflectiveOperationException {
        return new Instantator(clazz.getConstructor(getTypes()), args);
    }

    private Class<?>[] getTypes() {
        return Arrays.asList(args).stream().map(o -> o.getClass()).collect(toList()).toArray(new Class<?>[args.length]);
    }
}