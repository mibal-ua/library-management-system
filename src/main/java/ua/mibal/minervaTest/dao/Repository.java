package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public abstract class Repository<T extends Entity> {

    public Optional<T> findById(Long id) {
        return Optional.empty();
    }

    public List<T> findAll() {
        return List.of();
    }


    public void save(T entity) {

    }

    public void delete(T entity) {

    }
}
