package ua.mibal.minervaTest.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.gui.WindowManager;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.service.BookService;
import ua.mibal.minervaTest.service.Service;

import static ua.mibal.minervaTest.utils.StringUtils.isNumber;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class AppropriateBookOperationController {

    private final WindowManager windowManager;
    private final BookService bookService;
    private final Service<Client> clientService;

    public AppropriateBookOperationController(WindowManager windowManager,
                                              BookService bookService,
                                              Service<Client> clientService) {
        this.windowManager = windowManager;
        this.bookService = bookService;
        this.clientService = clientService;
    }

    public void take(final String[] args) {
        if (args.length != 2 || !isNumber(args[1]) || !isNumber(args[0])) {
            windowManager.showToast("You need to enter 'take ${client_id} ${book_id}'");
            return;
        }

        final Long clientId = Long.valueOf(args[0]);
        clientService.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Client with id=" + clientId + " not found"));
        final Long bookId = Long.valueOf(args[1]);
        bookService.findById(bookId).ifPresentOrElse(
                bookToTake -> {
                    if (!bookToTake.isFree()) {
                        windowManager.showToast("Oops, but book with id=" + bookId + " isn't free(");
                        return;
                    }
                    bookService.takeBook(clientId, bookId);
                    windowManager.showToast("Book successfully taken!");
                },
                () -> windowManager.showToast("Oops, but book with id=" + bookId + " doesn't exists")
        );
    }

    public void returnn(final String[] args) {
        if (args.length != 2 || !isNumber(args[1]) || !isNumber(args[0])) {
            windowManager.showToast("You need to enter 'return ${client_id} ${book_id}'");
            return;
        }
        final Long clientId = Long.valueOf(args[0]);
        final Client client = clientService.findByIdLoadAll(clientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Client with id=" + clientId + " not found"));
        final Long bookId = Long.valueOf(args[1]);
        bookService.findById(bookId).ifPresentOrElse(
                bookToReturn -> {
                    if (!client.getBooks().contains(bookToReturn)) {
                        windowManager.showToast("Oops, but user with id=" + clientId +
                                                " doesn't hold this book with id=" + bookId);
                        return;
                    }
                    bookService.returnBook(clientId, bookId);
                    windowManager.showToast("Books successfully returned!");
                },
                () -> windowManager.showToast("Oops, but book with id=" + bookId + " doesn't exists")
        );
    }
}
