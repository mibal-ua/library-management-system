package ua.mibal.minervaTest.dao.book;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;

import java.util.List;

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
    public List<Book> find(String[] queryArgs) {
        return entityManager.createQuery("select b from Book b", Book.class)
                .getResultStream()
                .filter(book -> {
                    for (String arg : queryArgs) {
                        if (book.getId().toString().equals(arg) ||
                            book.getTitle().contains(arg) || // TODO add search by Date
                            book.getAuthor().contains(arg) ||
                            book.getPublisher().contains(arg))
                            return true;
                    }
                    return false;
                })
                .toList();
    }

    @Override
    public void takeBook(Long clientId, Long bookId) {
        Book managedBook = entityManager.getReference(Book.class, bookId);
        Client managedClient = entityManager.getReference(Client.class, clientId);
        managedBook.setClient(managedClient);
    }

    @Override
    public void returnBook(Long clientId, Long bookId) {
        Book managedBook = entityManager.getReference(Book.class, bookId);
        managedBook.setClient(null);
    }
}
