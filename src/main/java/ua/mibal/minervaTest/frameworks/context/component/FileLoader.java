package ua.mibal.minervaTest.frameworks.context.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.ClassLoader.getSystemClassLoader;
import static ua.mibal.minervaTest.frameworks.context.utils.FileUtils.className;
import static ua.mibal.minervaTest.frameworks.context.utils.FileUtils.getPackageFile;
import static ua.mibal.minervaTest.frameworks.context.utils.FileUtils.isJavaClass;
import static ua.mibal.minervaTest.frameworks.context.utils.FileUtils.validateDirFile;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class FileLoader {

    public List<Class<?>> classesInDir(String rootPackage) {
        File packageDir = getPackageFile(rootPackage);
        return dirClasses(packageDir);
    }

    private List<Class<?>> dirClasses(File parentDir) {
        validateDirFile(parentDir);
        List<Class<?>> classesInDir = new ArrayList<>();
        for (File innerFile : parentDir.listFiles()) {
            if (innerFile.isDirectory()) {
                classesInDir.addAll(dirClasses(innerFile));
            } else if (isJavaClass(innerFile)) {
                Class<?> clazz = classFromFile(innerFile);
                classesInDir.add(clazz);
            }
        }
        return classesInDir;
    }

    private Class<?> classFromFile(File file) {
        String className = className(file);
        try {
            return getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
