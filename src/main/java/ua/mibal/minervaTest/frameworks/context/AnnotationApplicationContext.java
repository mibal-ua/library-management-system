package ua.mibal.minervaTest.frameworks.context;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.context.component.BeanInitializer;
import ua.mibal.minervaTest.frameworks.context.component.FileLoader;

import java.util.List;
import java.util.Map;

import static ua.mibal.minervaTest.frameworks.context.utils.ArrayUtils.contains;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class AnnotationApplicationContext implements ApplicationContext {

    private final Map<Class<?>, Object> container;

    public AnnotationApplicationContext(String basePackage) {
        List<Class<?>> classesToInit = classesWithAnnotation(basePackage, Component.class);
        this.container = new BeanInitializer().initBeans(classesToInit);
    }

    private List<Class<?>> classesWithAnnotation(String rootPackage, Class<?> annotation) {
        return new FileLoader().classesInDir(rootPackage).stream()
                .filter(clazz -> contains(clazz.getAnnotations(), annotation))
                .toList();
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(container.get(beanClass));
    }

    @Override
    public void close() {
        container.clear();
    }
}
