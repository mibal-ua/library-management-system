package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.Repository;
import ua.mibal.minervaTest.frameworks.orm.model.EntityManager;
import ua.mibal.minervaTest.model.Book;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class BookRepository extends Repository<Book> {

    public BookRepository(EntityManager entityManager) {
        super(Book.class, entityManager);
    }
}
