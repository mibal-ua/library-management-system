package ua.mibal.minervaTest.frameworks.context;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public interface ApplicationContext {

    <T> T getBean(Class<T> beanClass);

    void close();
}
