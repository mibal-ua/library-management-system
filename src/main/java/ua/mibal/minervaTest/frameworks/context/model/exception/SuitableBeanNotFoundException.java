package ua.mibal.minervaTest.frameworks.context.model.exception;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class SuitableBeanNotFoundException extends RuntimeException {

    public SuitableBeanNotFoundException(Class<?> wanted, List<Class<?>> classes) {
        super(String.format(
                "Excepted 1 instance of class '%s' but found %d instances: %s",
                wanted, classes.size(), classes
        ));
    }
}
