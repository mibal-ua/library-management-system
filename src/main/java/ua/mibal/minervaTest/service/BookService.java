package ua.mibal.minervaTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.dao.book.BookRepository;
import ua.mibal.minervaTest.dao.client.ClientRepository;
import ua.mibal.minervaTest.dao.operation.OperationRepository;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ua.mibal.minervaTest.model.OperationType.RETURN;
import static ua.mibal.minervaTest.model.OperationType.TAKE;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@org.springframework.stereotype.Service
public class BookService extends Service<Book> {

    private final static BookService instance = new BookService();

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private OperationRepository operationRepository;

    private BookService() {
    }

    public static BookService getInstance() {
        return instance;
    }

    @Override
    public List<Book> searchBy(String[] args) {
        List<Book> books = bookRepository.findAllFetchClient();
        List<Book> result = new ArrayList<>();
        for (String arg : args) {
            List<Book> booksToAdd = books.stream()
                    .filter(book -> book.getId().toString().equals(arg) ||
                                    book.getTitle().contains(arg) || // TODO add search by Date
                                    book.getAuthor().contains(arg) ||
                                    book.getPublisher().contains(arg) ||
                                    book.getClient()
                                            .map(cl -> cl.getName().contains(arg))
                                            .orElse(false))
                    .toList();
            result.addAll(booksToAdd);
        }
        return result.stream()
                .distinct().toList();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void update(Book edited) {
        bookRepository.save(edited);
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public Optional<Book> findByIdFetchAll(Long id) {
        return bookRepository.findByIdFetchClient(id);
    }

    @Transactional
    public void takeBook(Long clientId, Long bookId) {
        Book managedBook = bookRepository.getReference(bookId);
        Client managedClient = clientRepository.getReference(clientId);
        managedBook.setClient(managedClient);
        operationRepository.save(new Operation(
                LocalDateTime.now(),
                managedClient,
                TAKE,
                managedBook
        ));
    }

    @Transactional
    public void returnBook(Long clientId, Long bookId) {
        Book managedBook = bookRepository.getReference(bookId);
        Client managedClient = clientRepository.getReference(clientId);
        managedBook.setClient(null);
        operationRepository.save(new Operation(
                LocalDateTime.now(),
                managedClient,
                RETURN,
                managedBook
        ));
    }
}
