package ua.mibal.minervaTest.frameworks.context.utils;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemClassLoader;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class FileUtils {

    private static final String COMPILED_CLASSES_BASE_DIR = "target/classes/";

    public static boolean isJavaClass(File file) {
        return file.getName().endsWith(".class");
    }

    public static String className(File file) {
        String pathRelativeToClasspath = pathRelativeToClasspath(file);
        return pathRelativeToClasspath
                .replace("/", ".")
                .replace(".class", "");
    }

    private static String pathRelativeToClasspath(File file) {
        String path = file.getAbsolutePath();

        int javaPackage = path.indexOf(COMPILED_CLASSES_BASE_DIR) + COMPILED_CLASSES_BASE_DIR.length();
        return path.substring(javaPackage);
    }

    public static File getPackageFile(String dir) {
        String dirPath = dir.replace(".", "/");
        ClassLoader classLoader = getSystemClassLoader();
        URL url = Optional.ofNullable(classLoader.getResource(dirPath))
                .orElseThrow();
        return new File(url.getPath());
    }

    public static void validateDirFile(File parentDir) {
        if (!parentDir.isDirectory())
            throw new RuntimeException("File with path=" + parentDir.getAbsolutePath() + " inst directory");
    }
}
