package hw.logging;

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

    private final Map<Method, Method> methodsMap = new ConcurrentHashMap<>();

    private final Map<Method, Boolean> logMethodsMap = new ConcurrentHashMap<>();

    public static Object createProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(), target.getClass().getInterfaces(), new LoggingProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method realMethod;
        if (methodsMap.containsKey(method)) {
            realMethod = methodsMap.get(method);
            System.out.println("Method " + method.getName() + " description is obtained from cache");
        } else {
            // Получаем метод из класса (а не из интерфейса)
            realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            methodsMap.put(method, realMethod);
            System.out.println("Method " + method.getName() + " description has been putted to cache");
        }

        boolean isLogAnnotated;
        if (logMethodsMap.containsKey(realMethod)) {
            isLogAnnotated = true;
        } else {
            isLogAnnotated = realMethod.isAnnotationPresent(Log.class);
            logMethodsMap.put(realMethod, isLogAnnotated);
        }

        if (isLogAnnotated) {
            StringBuilder sb = new StringBuilder();
            sb.append("--> PROXY <-- executed method: ").append(method.getName());
            sb.append("executed method: ").append(method.getName());
            if (args != null && args.length > 0) {
                sb.append(", param: ");
                for (int i = 0; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i < args.length - 1) sb.append(", ");
                }
            }
            System.out.println(sb);
        }

        return method.invoke(target, args);
    }
}
