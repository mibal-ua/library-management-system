package ua.mibal.minervaTest.frameworks.context;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.context.annotations.Configuration;
import ua.mibal.minervaTest.frameworks.context.component.BeanContainer;
import ua.mibal.minervaTest.frameworks.context.component.FileLoader;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class AnnotationApplicationContext implements ApplicationContext {

    private final BeanContainer beanContainer = new BeanContainer();

    public AnnotationApplicationContext(String basePackage) {
        List<Class<?>> configurations = classesWithAnnotation(basePackage, Configuration.class);
        beanContainer.initBeansViaConfigurations(configurations);

        List<Class<?>> beans = classesWithAnnotation(basePackage, Component.class);
        beanContainer.initBeans(beans);
    }

    private List<Class<?>> classesWithAnnotation(String rootPackage, Class<? extends Annotation> annotation) {
        return new FileLoader().classesInDir(rootPackage).stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotation))
                .toList();
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        return beanClass.cast(beanContainer.get(beanClass));
    }

    @Override
    public void close() {
        beanContainer.clear();
    }
}
