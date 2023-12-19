package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class SqlRequestGenerator {

    private final EntityMetadata metadata;

    public SqlRequestGenerator(EntityMetadata metadata) {
        this.metadata = metadata;
    }

    public String findAll() {
        return null;
    }

    public String save(Object entity) {
        return null;
    }

    public String delete(Object entity) {
        return null;
    }

    public String findById(Long id) {
        return null;
    }
}
