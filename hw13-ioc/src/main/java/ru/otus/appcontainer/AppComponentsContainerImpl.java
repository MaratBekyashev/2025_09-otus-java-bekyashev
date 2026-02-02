package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> configClass) {
        processConfig(configClass);
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        List<Class<?>> sortedConfigs = Arrays.stream(configClasses)
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();
        for (var config : sortedConfigs) {
            processConfig(config);
        }
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);

        if (configClasses.isEmpty()) {
            throw new RuntimeException("No config classes found in package: " + packageName);
        }
        List<Class<?>> sortedConfigs = configClasses.stream()
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        for (Class<?> configClass : sortedConfigs) {
            processConfig(configClass);
        }
    }

    private List<Method> getClassMethods(Class<?> configClass) {
        List<Method> result = Arrays.asList(configClass.getDeclaredMethods()).stream()
                .filter(i -> i.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        return result;
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> allConfigMethods = getClassMethods(configClass);

        Object configInstance = null;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (Method method : allConfigMethods) {
            Class<?>[] paramTypes = method.getParameterTypes();

            Object[] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                args[i] = getAppComponent(paramTypes[i]); // уже созданные
            }

            Object component = null;
            try {
                component = method.invoke(configInstance, args);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }

            String name = method.getAnnotation(AppComponent.class).name();
            addComponent(name, component);
        }
    }

    private void addComponent(String componentName, Object component) {
        if (appComponentsByName.containsKey(componentName)) {
            var msg = "Duplicate component name: %s (class: %s)"
                    .formatted(componentName.getClass().getName());
            throw new RuntimeException(msg);
        }

        appComponents.add(component);
        appComponentsByName.put(componentName, component);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> candidates = new ArrayList<>();

        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                candidates.add((C) component);
            }
        }

        if (candidates.isEmpty()) {
            throw new RuntimeException("No component of type: %s ".formatted(componentClass.getName()));
        }

        if (candidates.size() > 1) {
            var msg = "More than one component of type: " + componentClass.getName() + ". Found: " + candidates.size();
            throw new RuntimeException(msg);
        }

        return candidates.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException("Component %s not found: ".formatted(componentName)));
        return (C) component;
    }
}
