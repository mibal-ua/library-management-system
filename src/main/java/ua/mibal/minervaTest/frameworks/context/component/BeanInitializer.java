package ua.mibal.minervaTest.frameworks.context.component;

import ua.mibal.minervaTest.frameworks.context.model.exception.ContextDuplicateException;
import ua.mibal.minervaTest.frameworks.context.model.exception.NoSuchBeanDefinitionException;
import ua.mibal.minervaTest.frameworks.context.model.exception.SuitableBeanNotFoundException;

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
    private List<Class<?>> classesToInit;

    public Map<Class<?>, Object> initBeans(List<Class<?>> classesToInit) {
        this.classesToInit = classesToInit;
        classesToInit.forEach(this::initBean);
        return beansContainer;
    }

    private void initBean(Class<?> clazz) {
        if (beansContainer.containsKey(clazz)) {
            return;
        }
        try {
            Object bean;
            if (clazz.isInterface()) {
                Class<?> implementation = getImplementation(clazz);
                initBean(implementation);
                return;
            }
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

    private Class<?> getImplementation(Class<?> anInterface) {
        List<Class<?>> implementations = classesToInit.stream()
                .filter(c -> stream(c.getInterfaces()).toList().contains(anInterface))
                .toList();
        if (implementations.isEmpty()) {
            throw new NoSuchBeanDefinitionException(anInterface);
        }
        if (implementations.size() > 1) {
            throw new SuitableBeanNotFoundException(anInterface, implementations);
        }
        return implementations.get(0);
    }

    private void registerBean(Object bean) {
        stream(bean.getClass().getInterfaces())
                .forEach(inter -> register(inter, bean));
        beansContainer.put(bean.getClass(), bean);
    }

    private void register(Class<?> clazz, Object bean) {
        if (beansContainer.containsKey(clazz))
            throw new ContextDuplicateException(clazz, beansContainer.get(clazz), bean);
        beansContainer.put(clazz, bean);
    }

    private boolean beanWantsDependencies(Class<?> clazz) {
        return stream(clazz.getDeclaredConstructors())
                .noneMatch(constructor -> constructor.getParameterCount() == 0);
    }

    private Class<?>[] getDependenciesForBean(Class<?> clazz) {
        return clazz.getDeclaredConstructors()[0].getParameterTypes();
    }

    private Object[] getBeans(Class<?>[] beanClasses) {
        return stream(beanClasses)
                .map(beansContainer::get)
                .toArray();
    }
}
