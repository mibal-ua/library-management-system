package ua.mibal.minervaTest.service;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface Service<T> {

    List<T> searchBy(String[] args);

    Optional<T> findById(Long id);

    void update(T edited);

    void save(T entity);

    void delete(T entityToDel);

    Optional<T> findByIdFetchAll(Long id);

    List<T> search();
}
