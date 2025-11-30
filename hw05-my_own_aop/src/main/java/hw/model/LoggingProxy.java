package hw.model;

import hw.annotations.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoggingProxy implements InvocationHandler {

    private final Object target;

    private final Map<Method, Boolean> logMethodsMap = new ConcurrentHashMap<>();

    public static Object createProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), target.getClass().getInterfaces(), new LoggingProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        boolean isLogAnnotated;

        if (logMethodsMap.containsKey(method)) {
            isLogAnnotated = logMethodsMap.get(method);
            System.out.println("Method \"" + method.getName() + "\" description is obtained from cache");
        } else {
            Method realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            isLogAnnotated = realMethod.isAnnotationPresent(Log.class);
            logMethodsMap.put(method, isLogAnnotated);
            System.out.println("Method \"" + method.getName() + "\" description has been putted to cache");
        }

        if (isLogAnnotated) {
            StringBuilder sb = new StringBuilder();
            sb.append("--> PROXY <-- method params: ").append(method.getName());
            if (args != null && args.length > 0) {
                sb.append(", param: ");
                for (int i = 0; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i < args.length - 1) sb.append(", ");
                }
            }
            System.out.println(sb);
        }
        System.out.println("--> PROXY <-- Invoke of original method for: " + method.getName());
        return method.invoke(target, args);
    }
}
