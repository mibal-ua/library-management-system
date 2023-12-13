package ua.mibal.minervaTest.frameworks.context;

import ua.mibal.minervaTest.Application;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class AnnotationApplicationContext implements ApplicationContext {
    // TODO
    @Override
    public Application getBean(Class<Application> applicationClass) {
        return null;
    }

    @Override
    public void close() {

    }
}
