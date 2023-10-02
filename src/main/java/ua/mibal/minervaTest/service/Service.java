package ua.mibal.minervaTest.service;

import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.window.DataType;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public abstract class Service<T> {

    public static Service<? extends Entity> getInstance(DataType dataType) {
        return switch (dataType) {
            case BOOK -> BookService.getInstance();
            case CLIENT -> ClientService.getInstance();
            case HISTORY -> OperationService.getInstance();
            default -> throw new IllegalArgumentException("Illegal getInstance call for dataType=" + dataType);
        };
    }

    abstract public List<T> searchBy(String[] args);

    abstract public Optional<T> details(Long id);

    abstract public Optional<T> findById(Long id);

    abstract public void update(T edited);

    abstract public void save(T entity);

    abstract public void delete(T entityToDel);

    abstract public Optional<T> findByIdFetchAll(Long id);
}
