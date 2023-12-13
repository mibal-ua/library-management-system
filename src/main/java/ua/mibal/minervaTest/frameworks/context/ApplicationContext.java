package ua.mibal.minervaTest.frameworks.context;

import ua.mibal.minervaTest.Application;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public interface ApplicationContext {

    Application getBean(Class<Application> applicationClass);

    void close();
}
