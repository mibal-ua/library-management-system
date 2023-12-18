package ua.mibal.minervaTest.dao.book;

import jakarta.persistence.EntityManager;
import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class BookRepository {

    private EntityManager entityManager;

    public Optional<Book> findByIdFetchClientLong(Long id) {
        return Optional.empty();
    }

    public List<Book> findAllFetchClient() {
        return List.of();
    }

    public Optional<Book> findById(Long id) {
        return null;
    }

    public void save(Book edited) {

    }

    public void delete(Book book) {

    }

    public Optional<Book> findByIdFetchClient(Long id) {
        return null;
    }

    public List<Book> findAll() {
        return null;
    }
}
