package ua.mibal.minervaTest.frameworks.context.model.exception;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class ContextDuplicateException extends RuntimeException {

    public ContextDuplicateException(Class<?> key, Object existing, Object duplicate) {
        super(String.format(
                "Trying to add duplicate of key=%s. Existing: %s, tried to add: %s.",
                key, existing, duplicate
        ));
    }
}
