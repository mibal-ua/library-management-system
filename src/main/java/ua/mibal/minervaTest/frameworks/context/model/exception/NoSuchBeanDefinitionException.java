package ua.mibal.minervaTest.frameworks.context.model.exception;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class NoSuchBeanDefinitionException extends RuntimeException {

    public NoSuchBeanDefinitionException(Class<?> clazz) {
        super("No such beans for class '" + clazz + "'");
    }
}
