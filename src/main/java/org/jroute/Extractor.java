package org.jroute;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jroute.route.endpoint.Instantiator;
import org.jroute.route.endpoint.MethodCaller;

import java.lang.reflect.Method;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

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
        caller = new MethodCaller(instantiator(clazz), realMethod(clazz, method));
        return null;
    }

    private Method realMethod(final Class<?> clazz, final Method method) throws ReflectiveOperationException {
        return clazz.getMethod(method.getName(), method.getParameterTypes());
    }

    private Instantiator instantiator(final Class<?> clazz) throws ReflectiveOperationException {
        return new Instantiator(clazz.getConstructor(getTypes()), args);
    }

    private Class<?>[] getTypes() {
        return stream(args).map(Object::getClass).collect(toList()).toArray(new Class<?>[args.length]);
    }
}