package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.model.Book;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class BookRepository extends Repository<Book> {

    public BookRepository() {
        super(Book.class);
    }
}
