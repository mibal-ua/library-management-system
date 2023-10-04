package ua.mibal.minervaTest.service;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.DataType;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ServiceFactory {

    private final Service<Book> bookService;
    private final Service<Client> clientService;
    private final Service<Operation> operationService;

    public ServiceFactory(Service<Book> bookService,
                          Service<Client> clientService,
                          Service<Operation> operationService) {
        this.bookService = bookService;
        this.clientService = clientService;
        this.operationService = operationService;
    }

    public Service<? extends Entity> getInstance(DataType dataType) {
        return switch (dataType) {
            case BOOK -> bookService;
            case CLIENT -> clientService;
            case HISTORY -> operationService;
            default -> throw new IllegalArgumentException("Illegal getInstance call for dataType=" + dataType);
        };
    }
}
