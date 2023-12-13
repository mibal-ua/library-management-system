package ua.mibal.minervaTest.frameworks.context.component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class BeanInitializer {
    private final Map<Class<?>, Object> beansContainer = new HashMap<>();

    public Map<Class<?>, Object> initBeans(List<Class<?>> classesToInit) {
        classesToInit.forEach(this::initBean);
        return beansContainer;
    }

    private void initBean(Class<?> clazz) {
        if (beansContainer.containsKey(clazz)) {
            return;
        }
        try {
            Object bean;
            if (beanWantsDependencies(clazz)) {
                Class<?>[] dependenciesForBean = getDependenciesForBean(clazz);
                stream(dependenciesForBean).forEach(this::initBean);
                Object[] dependencies = getBeans(dependenciesForBean);
                bean = clazz.getConstructor(dependenciesForBean)
                        .newInstance(dependencies);
            } else {
                bean = clazz.getConstructor().newInstance();
            }
            registerBean(bean);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getBeans(Class<?>[] beanClasses) {
        return null;
    }

    private Class<?>[] getDependenciesForBean(Class<?> clazz) {
        return null;
    }

    private boolean beanWantsDependencies(Class<?> clazz) {
        return false;
    }

    private void registerBean(Object bean) {

    }
}
