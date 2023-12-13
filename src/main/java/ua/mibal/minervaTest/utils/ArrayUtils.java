package ua.mibal.minervaTest.utils;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class ArrayUtils {

    public static boolean contains(Annotation[] a, Class<?> annotation) {
        return Arrays.asList(a).contains(annotation);
    }
}
