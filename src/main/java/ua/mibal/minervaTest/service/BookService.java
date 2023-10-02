package ua.mibal.minervaTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.mibal.minervaTest.dao.book.BookRepository;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@org.springframework.stereotype.Service
public class BookService extends Service<Book> {

    // TODO

    private final static BookService instance = new BookService();

    @Autowired
    private BookRepository bookRepository;

    private BookService() {
    }

    public static BookService getInstance() {
        return instance;
    }


    @Override
    public List<Book> searchBy(String[] args) {
        return null;
    }

    @Override
    public Optional<Book> details(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Book edited) {
    }

    @Override
    public void save(Book book) {
    }

    @Override
    public void delete(Book book) {
    }

    @Override
    public Optional<Book> findByIdFetchAll(Long id) {
        return Optional.empty();
    }

    public void takeBook(Long clientId, Long bookId) {

    }

    public void returnBook(Long clientId, Long bookId) {

    }
}
