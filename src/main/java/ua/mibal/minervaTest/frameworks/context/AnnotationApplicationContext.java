package ua.mibal.minervaTest.frameworks.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class AnnotationApplicationContext implements ApplicationContext {

    private final Map<Class<?>, Object> container = new HashMap<>();


    public AnnotationApplicationContext() {
        initContext();
    }

    private void initContext() {
        List<Class<?>> classesToInit = classesToInit();
        initBeans(classesToInit);
    }

    private void initBeans(List<Class<?>> classes) {
    }

    private List<Class<?>> classesToInit() {
        return null;
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
