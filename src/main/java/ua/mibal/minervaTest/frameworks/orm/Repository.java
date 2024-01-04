package ua.mibal.minervaTest.frameworks.orm;

import ua.mibal.minervaTest.frameworks.orm.component.EntityManager;
import ua.mibal.minervaTest.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public abstract class Repository<T extends Entity> {

    private final Class<T> entityClazz;
    private final EntityManager entityManager;

    protected Repository(Class<T> entityClazz, EntityManager entityManager) {
        this.entityClazz = entityClazz;
        this.entityManager = entityManager;
    }

    public Optional<T> findById(Long id) {
        return entityManager.findById(id, entityClazz);
    }

    public List<T> findAll() {
        return entityManager.findAll(entityClazz);
    }

    public boolean save(T entity) {
        return entityManager.save(entity);
    }

    public boolean delete(T entity) {
        return entityManager.delete(entity);
    }
}
