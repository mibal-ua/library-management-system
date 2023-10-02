package ua.mibal.minervaTest.dao.book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Book;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Repository
@Transactional
public class CustomBookRepositoryImpl implements CustomBookRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Book getReference(Long id) {
        return entityManager.getReference(Book.class, id);
    }
}
