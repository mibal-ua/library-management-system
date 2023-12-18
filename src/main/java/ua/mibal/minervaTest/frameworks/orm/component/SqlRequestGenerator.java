package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.model.Entity;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class SqlRequestGenerator<T extends Entity> {

    // TODO

    private final Class<T> clazz;

    public SqlRequestGenerator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String findAll() {
        return null;
    }

    public String save(T entity) {
        return null;
    }

    public String delete(T entity) {
        return null;
    }

    public String findById(Long id) {
        return null;
    }
}
