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
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.State;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.*;
import static ua.mibal.minervaTest.utils.StringUtils.substringAppend;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ApplicationController {

    private final WindowManager windowManager;

    private final DataOperator dataOperator;

    private final Library library;

    public ApplicationController(final WindowManager windowManager,
                                 final DataOperator dataOperator) {
        this.windowManager = windowManager;
        this.dataOperator = dataOperator;
        this.library = dataOperator.getLibrary();
    }

    public void tab1(final String[] ignored) {
        windowManager.tab1(library);
    }

    public void tab2(final String[] ignored) {
        windowManager.tab2(library);
    }

    public void tab3(final String[] ignored) {
        windowManager.tab3(library);
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
            case BOOK -> windowManager.searchBookTab(library.findBooks(args), args);
            case CLIENT -> windowManager.searchClientTab(library.findClients(args), args);
            case HISTORY -> windowManager.searchOperationTab(library.findOperations(args), library, args);
            case NULL -> windowManager.showToast("You can not use command 'search' in this tab.");
        }
    }

    public void look(final String[] args) {
        if (args.length == 0) {
            windowManager.showToast("You need to enter 'look' with ${query}");
            return;
        }
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> {
                final String id = args[0];
                final Optional<Book> optionalBook = library.findBookById(id);
                if (optionalBook.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no books with this id '%s'", id));
                    break;
                }
                windowManager.bookDetails(optionalBook.get());
            }
            case CLIENT -> {
                final String id = args[0];
                final Optional<Client> optionalClient = library.findClientById(id);
                if (optionalClient.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with this id '%s'", id));
                    break;
                }
                Client client = optionalClient.get();
                List<Book> booksClientHolds = library.getBooksClientHolds(client);
                windowManager.clientDetails(client, booksClientHolds);
            }
            case HISTORY -> {
                final String id = args[0];
                final Optional<Operation> optionalOperation = library.findOperationById(id);
                if (optionalOperation.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no operations with this id '%s'", id));
                    break;
                }
                Operation operation = optionalOperation.get();
                Optional<Client> optionalClient = library.findClientById(operation.getClientId());

                if (optionalClient.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with id '%s' that specialized in operation",
                            operation.getClientId()));
                    break;
                }
                Client client = optionalClient.get();
                List<Book> booksInOperation = library.getBooksInOperation(operation);
                windowManager.operationDetails(operation, client, booksInOperation);
            }
            case NULL -> windowManager.showToast("You can not use command 'look' in this tab.");
        }
    }

    public void edit(final String[] args) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }

        final State tab = windowManager.getCurrentTabState();
        final boolean isDetailsTab = tab == LOOK_BOOK || tab == LOOK_CLIENT || tab == LOOK_HISTORY;

        if (!isDetailsTab && args.length == 0) {
            windowManager.showToast("You need to enter 'edit' with ${id}");
            return;
        }

        switch (windowManager.getCurrentDataType()) {
            case BOOK -> {
                final String id = isDetailsTab
                        ? windowManager.getCachedBookId()
                        : args[0];

                Optional<Book> optionalToEditBook = library.findBookById(id);
                if (optionalToEditBook.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no books with this id '%s'", id));
                    break;
                }

                windowManager.editBook(optionalToEditBook.get())
                        .ifPresent(book -> {
                            library.updateBook(book);
                            if (isDetailsTab) {
                                windowManager.drawPrevTab();
                                windowManager.bookDetails(book);
                            }
                            windowManager.showToast("Book successfully updated!");
                            dataOperator.updateLibrary(library);
                        });
            }
            case CLIENT -> {
                final String id = isDetailsTab
                        ? windowManager.getCachedClientId()
                        : args[0];

                Optional<Client> optionalToEditClient = library.findClientById(id);
                if (optionalToEditClient.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with this id '%s'", id));
                    break;
                }

                windowManager.editClient(optionalToEditClient.get())
                        .ifPresent(client -> {
                            library.updateClient(client);
                            if (isDetailsTab) {
                                windowManager.drawPrevTab();
                                windowManager.clientDetails(
                                        client,
                                        library.getBooksClientHolds(client)
                                );
                            }
                            windowManager.showToast("Client successfully updated!");
                            dataOperator.updateLibrary(library);
                        });
            }
            case NULL -> windowManager.showToast("You can not use command 'edit' in this tab.");
        }

    }

    public void add(final String[] ignored) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> {
                Optional<Book> optionalBookToAdd = windowManager.initBookToAdd(library);
                optionalBookToAdd.ifPresent(book -> {
                    if (library.addBook(book)) {
                        windowManager.showToast("Book successfully added!");
                    } else {
                        windowManager.showToast("Book doesnt added(");
                    }
                });
                dataOperator.updateLibrary(library);
            }
            case CLIENT -> {
                Optional<Client> optionalClientToAdd = windowManager.initClientToAdd(library);
                optionalClientToAdd.ifPresent(client -> {
                    if (library.addClient(client)) {
                        windowManager.showToast("Client successfully added!");
                    } else {
                        windowManager.showToast("Client doesnt added(");
                    }
                });
                dataOperator.updateLibrary(library);
            }
            case NULL -> windowManager.showToast("You can not use command 'add' in this tab.");
        }
    }

    public void delete(final String[] args) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }

        final State tab = windowManager.getCurrentTabState();
        final boolean isDetailsTab = tab == LOOK_BOOK || tab == LOOK_CLIENT || tab == LOOK_HISTORY;

        if (!isDetailsTab && args.length == 0) {
            windowManager.showToast("You need to enter 'delete' with ${id}");
            return;
        }

        switch (windowManager.getCurrentDataType()) {
            case BOOK -> {
                final String id = isDetailsTab
                        ? windowManager.getCachedBookId()
                        : args[0];

                if (!library.isContainBookId(id)) {
                    windowManager.showToast(format(
                            "Oops, there are no books with this id '%s'", id));
                    break;
                }
                final Book bookToDelete = library.findBookById(id).get();
                final String title = substringAppend(bookToDelete.getTitle(), "..", 14);

                Optional<Client> bookHolder = library.findClientByBookId(id);
                if (bookHolder.isPresent()) {
                    String name = substringAppend(bookHolder.get().getName(), "..", 15);
                    windowManager.showToast(format(
                            "Oops, but client '%s' holds this book '%s'", name, id));
                    break;
                }

                final boolean isConfirmed = windowManager.showDialogueToast(
                        format("You really need to delete book '%s'?", title), "YES", "NO");
                if (isConfirmed) {
                    if (library.deleteBook(bookToDelete)) {
                        windowManager.showToast("Book successfully deleted.");
                        if (isDetailsTab) {
                            windowManager.drawPrevTab();
                        }
                    } else {
                        windowManager.showToast("Book doesnt deleted.");
                    }
                    dataOperator.updateLibrary(library);
                }
            }
            case CLIENT -> {
                final String id = isDetailsTab
                        ? windowManager.getCachedClientId()
                        : args[0];

                if (!library.isContainClientId(id)) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with this id '%s'", id));
                    break;
                }
                Client clientToDelete = library.findClientById(id).get();
                String name = substringAppend(clientToDelete.getName(), "..", 15);

                if (library.doesClientHoldBook(clientToDelete)) {
                    windowManager.showToast(format(
                            "Oops, client '%s' is holding books, we can't delete him", name));
                    break;
                }

                final boolean isConfirmed = windowManager.showDialogueToast(
                        format("You really need to delete client '%s'?", name), "YES", "NO");
                if (isConfirmed) {
                    if (library.deleteClient(clientToDelete)) {
                        windowManager.showToast("Client successfully deleted.");
                        if (isDetailsTab) {
                            windowManager.drawPrevTab();
                        }
                    } else {
                        windowManager.showToast("Client doesnt deleted.");
                    }
                    dataOperator.updateLibrary(library);
                }
            }
            case NULL -> windowManager.showToast("You can not use command 'delete' in this tab.");
        }
    }

    public void take(final String[] args) {
        if (windowManager.getCurrentTabState() == LOOK_CLIENT) {
            if (args.length == 0) {
                windowManager.showToast("You need to enter 'take' with '${id1} ${id2}..' of books");
                return;
            }

            final Client client = library
                    .findClientById(windowManager.getCachedClientId())
                    .get();

            final List<String> booksToTake = Arrays.stream(args)
                    .filter(bookId -> {
                                Optional<Book> optionalBook = library.findBookById(bookId);
                                return optionalBook.isPresent() && optionalBook.get().isFree();
                            }
                    ).toList();
            if (booksToTake.isEmpty()) {
                windowManager.showToast("Oops, all books you are enter not free");
            } else {
                library.takeBooks(client, booksToTake);
                windowManager.drawPrevTab();
                windowManager.clientDetails(
                        client,
                        library.getBooksClientHolds(client)
                );
                dataOperator.updateLibrary(library);
                windowManager.showToast("Books successfully taken!");
            }
        } else {
            windowManager.showToast("You can not use command 'take' in this tab.");
        }
    }

    public void returnn(final String[] args) {
        if (windowManager.getCurrentTabState() == LOOK_CLIENT) {
            if (args.length == 0) {
                windowManager.showToast("You need to enter 'return' with '${id1} ${id2}..'  of books");
                return;
            }

            final Client client = library
                    .findClientById(windowManager.getCachedClientId())
                    .get();

            final List<String> booksToReturn = Arrays.stream(args)
                    .filter(bookId -> library.findBookById(bookId).isPresent() &&
                            client.getBooksIds().contains(bookId))
                    .toList();

            if (booksToReturn.isEmpty()) {
                windowManager.showToast("Oops, all books you are enter not yours");
            } else {
                library.returnBooks(client, booksToReturn);
                windowManager.drawPrevTab();
                windowManager.clientDetails(
                        client,
                        library.getBooksClientHolds(client)
                );
                dataOperator.updateLibrary(library);
                windowManager.showToast("Books successfully returned!");
            }
        } else {
            windowManager.showToast("You can not use command 'return' in this tab.");
        }
    }

    @FunctionalInterface
    public interface OwnFunction {

        void apply(String[] args);
    }
}
