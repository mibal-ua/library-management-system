package ua.mibal.minervaTest.service;

import org.springframework.stereotype.Service;
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
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final OperationRepository operationRepository;

    public BookServiceImpl(BookRepository bookRepository,
                           ClientRepository clientRepository,
                           OperationRepository operationRepository) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public List<Book> searchBy(String[] args) {
        List<Book> books = bookRepository.findAllFetchClient();
        List<Book> result = new ArrayList<>();
        for (String arg : args) {
            List<Book> booksToAdd = books.stream()
                    .filter(book -> book.getId().toString().equals(arg) ||
                                    book.getTitle().contains(arg) ||
                                    book.getPublishedDate().toString().contains(arg) ||
                                    book.getAuthor().contains(arg) ||
                                    book.getPublisher().contains(arg) ||
                                    book.getClient().stream()
                                            .anyMatch(cl -> cl.getName().contains(arg)))
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
    public Optional<Book> findByIdLoadAll(Long id) {
        return bookRepository.findByIdFetchClient(id);
    }

    @Override
    public List<Book> search() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findByIdForDeleteChecking(Long id) {
        return findById(id);
    }

    @Transactional
    @Override
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
    @Override
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
