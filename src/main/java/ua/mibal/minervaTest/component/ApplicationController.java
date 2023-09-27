/*
 * Copyright (c) 2023. http://t.me/mibal_ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.mibal.minervaTest.component;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.dao.book.BookRepository;
import ua.mibal.minervaTest.dao.client.ClientRepository;
import ua.mibal.minervaTest.dao.operation.OperationRepository;
import ua.mibal.minervaTest.gui.WindowManager;
import ua.mibal.minervaTest.model.Client;

import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.utils.StringUtils.isNumber;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ApplicationController {

    private final WindowManager windowManager;

    private final BookRepository bookRepository;
    private final OperationRepository operationRepository;
    private final ClientRepository clientRepository;

    public ApplicationController(WindowManager windowManager,
                                 BookRepository bookRepository,
                                 OperationRepository operationRepository,
                                 ClientRepository clientRepository) {
        this.windowManager = windowManager;
        this.bookRepository = bookRepository;
        this.operationRepository = operationRepository;
        this.clientRepository = clientRepository;
    }

    public void tab1(final String[] ignored) {
        windowManager.tab1(bookRepository::findAll);
    }

    public void tab2(final String[] ignored) {
        windowManager.tab2(clientRepository::findAllFetchBooks);
    }

    public void tab3(final String[] ignored) {
        windowManager.tab3(operationRepository::findAllFetchBookClient);
    }

    public void esc(final String[] ignored) {
        windowManager.drawPrevTab();
    }

    public void help(final String[] ignored) {
        windowManager.help();
    }

    public void search(final String[] args) {
        if (args.length == 0) {
            windowManager.showToast("You need to enter 'search' with ${query}");
            return;
        }
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> windowManager.searchBookTab(() -> bookRepository.find(args), args);
            case CLIENT -> windowManager.searchClientTab(() -> clientRepository.findFetchBooks(args), args);
            case HISTORY -> windowManager.searchOperationTab(() -> operationRepository.findFetchBookClient(args), args);
            case NULL -> windowManager.showToast("You can not use command 'search' in this tab.");
        }
    }

    public void look(final String[] args) {
        if (args.length == 0 || !isNumber(args[0])) {
            windowManager.showToast("You need to enter 'look' with ${id}");
            return;
        }
        final Long id = Long.valueOf(args[0]);
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> windowManager.bookDetails(() -> bookRepository.findByIdFetchClient(id));
            case CLIENT -> windowManager.clientDetails(() -> clientRepository.findByIdFetchBooks(id));
            case HISTORY -> windowManager.operationDetails(() -> operationRepository.findByIdFetchBookClient(id));
            case NULL -> windowManager.showToast("You can not use command 'look' in this tab.");
        }
    }

    public void edit(final String[] args) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }
        final boolean isDetailsTab = windowManager.getCurrentTabState().isDetailsTab();
        if (!isDetailsTab && (args.length == 0 || !isNumber(args[0]))) {
            windowManager.showToast("You need to enter 'edit' with ${id}");
            return;
        }
        final Long id = isDetailsTab
                ? windowManager.getCurrentEntityId()
                : Long.valueOf(args[0]);
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> bookRepository.findById(id).ifPresentOrElse(
                    book -> windowManager.editBook(book).ifPresent(editedBook -> {
                        bookRepository.save(editedBook);
                        windowManager.showToast("Book successfully updated!");
                    }),
                    () -> windowManager.showToast("Oops, there are no books with this id=" + id)
            );
            case CLIENT -> clientRepository.findByIdFetchBooks(id).ifPresentOrElse(
                    client -> windowManager.editClient(client).ifPresent(editedClient -> {
                        clientRepository.save(editedClient);
                        windowManager.showToast("Client successfully updated!");
                    }),
                    () -> windowManager.showToast("Oops, there are no clients with this id=" + id)
            );
            case NULL -> windowManager.showToast("You can not use command 'edit' in this tab.");
        }
    }

    public void add(final String[] ignored) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> windowManager.initBookToAdd().ifPresent(
                    book -> {
                        bookRepository.save(book);
                        windowManager.showToast("Book successfully added!");
                    });
            case CLIENT -> windowManager.initClientToAdd().ifPresent(
                    client -> {
                        clientRepository.save(client);
                        windowManager.showToast("Client successfully added!");
                    });
            case NULL -> windowManager.showToast("You can not use command 'add' in this tab.");
        }
    }

    public void delete(final String[] args) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }
        final boolean isDetailsTab = windowManager.getCurrentTabState().isDetailsTab();
        if (!isDetailsTab && (args.length == 0 || !isNumber(args[0]))) {
            windowManager.showToast("You need to enter 'delete' with ${id}");
            return;
        }
        final Long id = isDetailsTab
                ? windowManager.getCurrentEntityId()
                : Long.valueOf(args[0]);
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> bookRepository.findById(id).ifPresentOrElse(
                    bookToDel -> {
                        if (!bookToDel.isFree()) {
                            windowManager.showToast("Book isn't free",
                                    "Oops, but book '" + bookToDel.getTitle() + "' isn't free");
                            return;
                        }
                        final boolean isConfirmed = windowManager.showDialogueToast(
                                "You really need to delete book '" + bookToDel.getTitle() + "'?",
                                "YES", "NO");
                        if (isConfirmed) {
                            bookRepository.delete(bookToDel);
                            windowManager.showToast("Book successfully deleted.");
                        }
                    },
                    () -> windowManager.showToast("Oops, there are no books with this id=" + id)
            );
            case CLIENT -> clientRepository.findByIdFetchBooks(id).ifPresentOrElse(
                    clientToDel -> {
                        if (clientToDel.doesHoldBook()) {
                            windowManager.showToast("Client is holding books",
                                    "Oops, but client " + clientToDel.getName() + " is holding books, we can't delete him(");
                            return;
                        }
                        final boolean isConfirmed = windowManager.showDialogueToast(
                                "You really need to delete client '" + clientToDel.getName() + "'?",
                                "YES", "NO");
                        if (isConfirmed) {
                            clientRepository.delete(clientToDel);
                            windowManager.showToast("Client successfully deleted.");
                        }
                    },
                    () -> windowManager.showToast("Oops, there are no clients with this id=" + id)
            );
            case NULL -> windowManager.showToast("You can not use command 'delete' in this tab.");
        }
    }

    public void take(final String[] args) {
        if (args.length != 2 || !isNumber(args[1]) || !isNumber(args[0])) {
            windowManager.showToast("You need to enter 'take ${client_id} ${book_id}'");
            return;
        }

        final Long clientId = Long.valueOf(args[0]);
        clientRepository.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Client with id=" + clientId + " not found"));
        final Long bookId = Long.valueOf(args[1]);
        bookRepository.findById(bookId).ifPresentOrElse(
                bookToTake -> {
                    if (!bookToTake.isFree()) {
                        windowManager.showToast("Oops, but book with id=" + bookId + " isn't free(");
                        return;
                    }
                    bookRepository.takeBook(clientId, bookId);
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
        final Client client = clientRepository.findByIdFetchBooks(clientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Client with id=" + clientId + " not found"));
        final Long bookId = Long.valueOf(args[1]);
        bookRepository.findById(bookId).ifPresentOrElse(
                bookToReturn -> {
                    if (!client.getBooks().contains(bookToReturn)) {
                        windowManager.showToast("Oops, but user with id=" + clientId +
                                                " doesn't hold this book with id=" + bookId);
                        return;
                    }
                    bookRepository.returnBook(clientId, bookId);
                    windowManager.showToast("Books successfully returned!");
                },
                () -> windowManager.showToast("Oops, but book with id=" + bookId + " doesn't exists")
        );
    }

    @FunctionalInterface
    public interface ArrayConsumer<T> {

        void apply(T... args);
    }
}
