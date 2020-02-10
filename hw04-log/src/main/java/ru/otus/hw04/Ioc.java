package ru.otus.hw04;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Ioc {
    private Ioc() {}
    private static ConcurrentHashMap<Class, Set<Method>> loggedMethods = new ConcurrentHashMap();

    static Calculator createCalculator() throws NoSuchMethodException {
        return createProxy(new CalculatorImpl(), Calculator.class);
    }

    static Calculator createCalculatorWithoutLog() throws NoSuchMethodException {
        return createProxy(new CalculatorImplWithoutLog(), Calculator.class);
    }

    static <T> T createProxy(T realObject, Class<T> interfaceClass) throws NoSuchMethodException {
        Class<?> realClass = realObject.getClass();
        if (!loggedMethods.containsKey(realClass)) {
            Set<Method> withLogMethods = new HashSet<>();
            for (Method im : interfaceClass.getMethods()) {
                Method m = realClass.getMethod(im.getName(), im.getParameterTypes());
                if (m.getDeclaredAnnotation(MyLog.class) != null){
                    withLogMethods.add(im);
                }
            }
            loggedMethods.put(realClass, withLogMethods);
        }
        InvocationHandler handler = new LoggedInvocationHandler(realObject, realClass);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{interfaceClass}, handler);
    }

    static class LoggedInvocationHandler implements InvocationHandler {
        private final Object realObject;
        private final Class<?> realClass;

        LoggedInvocationHandler(Object realObject, Class<?> realClass) {
            this.realObject = realObject;
            this.realClass = realClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.get(realClass).contains(method)) {
                var argsStr = "";
                if (args != null) {
                    argsStr = Arrays.stream(args).map(x -> x == null ? "<null>" : x.toString())
                            .collect(Collectors.joining(", "));
                }
                System.out.println(String.format("executed method: %s.%s(%s)",
                        realClass.getSimpleName(),
                        method.getName(),
                        argsStr));
            }
            return method.invoke(realObject, args);
        }

        @Override
        public String toString() {
            return realObject.toString();
        }
    }
}
