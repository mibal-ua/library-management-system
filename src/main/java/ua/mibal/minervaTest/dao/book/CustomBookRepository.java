package ua.mibal.minervaTest.dao.book;

import ua.mibal.minervaTest.model.Book;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface CustomBookRepository {

    List<Book> find(String[] queryArgs);

    void takeBook(Long clientId, Long bookId);

    void returnBook(Long clientId, Long bookId);
}
