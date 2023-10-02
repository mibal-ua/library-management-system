package ua.mibal.minervaTest.dao.book;

import ua.mibal.minervaTest.model.Book;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface CustomBookRepository {

    Book getReference(Long id);
}
