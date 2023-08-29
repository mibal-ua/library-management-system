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
import ua.mibal.minervaTest.dao.BookDao;
import ua.mibal.minervaTest.dao.ClientDao;
import ua.mibal.minervaTest.dao.OperationDao;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.window.State;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.LOOK_BOOK;
import static ua.mibal.minervaTest.model.window.State.LOOK_CLIENT;
import static ua.mibal.minervaTest.model.window.State.LOOK_HISTORY;
import static ua.mibal.minervaTest.utils.StringUtils.substringAppend;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ApplicationController {

    private final WindowManager windowManager;

    private final BookDao bookDao;
    private final OperationDao operationDao;
    private final ClientDao clientDao;

    public ApplicationController(final WindowManager windowManager,
                                 final BookDao bookDao,
                                 final OperationDao operationDao,
                                 final ClientDao clientDao) {
        this.windowManager = windowManager;
        this.bookDao = bookDao;
        this.operationDao = operationDao;
        this.clientDao = clientDao;
    }

    public void tab1(final String[] ignored) {
        windowManager.tab1(bookDao.findAll());
    }

    public void tab2(final String[] ignored) {
        windowManager.tab2(clientDao.findAll());
    }

    public void tab3(final String[] ignored) {
        windowManager.tab3(operationDao.findAll(), clientDao);
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
            case BOOK -> windowManager.searchBookTab(bookDao.find(args), args);
            case CLIENT -> windowManager.searchClientTab(clientDao.find(args), args);
            case HISTORY -> windowManager.searchOperationTab(operationDao.find(args), clientDao, args);
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
                final Optional<Book> optionalBook = bookDao.findById(Long.valueOf(id));
                if (optionalBook.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no books with this id '%s'", id));
                    break;
                }
                windowManager.bookDetails(optionalBook.get());
            }
            case CLIENT -> {
                final String id = args[0];
                final Optional<Client> optionalClient = clientDao.findById(Long.valueOf(id));
                if (optionalClient.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with this id '%s'", id));
                    break;
                }
                Client client = optionalClient.get();
                List<Book> booksClientHolds = client.getBooks().stream().toList();
                windowManager.clientDetails(client, booksClientHolds);
            }
            case HISTORY -> {
                final String id = args[0];
                final Optional<Operation> optionalOperation = operationDao.findById(Long.valueOf(id));
                if (optionalOperation.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no operations with this id '%s'", id));
                    break;
                }
                Operation operation = optionalOperation.get();
                Optional<Client> optionalClient = clientDao.findById(operation.getClient().getId());

                if (optionalClient.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with id '%s' that specialized in operation",
                            operation.getClient().getId()));
                    break;
                }
                Client client = optionalClient.get();
                List<Book> booksInOperation = operation.getBooks().stream().toList();
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

                Optional<Book> optionalToEditBook = bookDao.findById(Long.valueOf(id));
                if (optionalToEditBook.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no books with this id '%s'", id));
                    break;
                }

                windowManager.editBook(optionalToEditBook.get())
                        .ifPresent(book -> {
                            bookDao.update(book);
                            if (isDetailsTab) {
                                windowManager.drawPrevTab();
                                windowManager.bookDetails(book);
                            }
                            windowManager.showToast("Book successfully updated!");
                        });
            }
            case CLIENT -> {
                final String id = isDetailsTab
                        ? windowManager.getCachedClientId()
                        : args[0];

                Optional<Client> optionalToEditClient = clientDao.findById(Long.valueOf(id));
                if (optionalToEditClient.isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with this id '%s'", id));
                    break;
                }

                windowManager.editClient(optionalToEditClient.get())
                        .ifPresent(client -> {
                            clientDao.update(client);
                            if (isDetailsTab) {
                                windowManager.drawPrevTab();
                                windowManager.clientDetails(
                                        client,
                                        client.getBooks().stream().toList()
                                );
                            }
                            windowManager.showToast("Client successfully updated!");
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
                Optional<Book> optionalBookToAdd = windowManager.initBookToAdd();
                optionalBookToAdd.ifPresent(book -> {
                    if (bookDao.save(book)) {
                        windowManager.showToast("Book successfully added!");
                    } else {
                        windowManager.showToast("Book doesnt added(");
                    }
                });
            }
            case CLIENT -> {
                Optional<Client> optionalClientToAdd = windowManager.initClientToAdd();
                optionalClientToAdd.ifPresent(client -> {
                    if (clientDao.save(client)) {
                        windowManager.showToast("Client successfully added!");
                    } else {
                        windowManager.showToast("Client doesnt added(");
                    }
                });
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

                if (bookDao.findById(Long.valueOf(id)).isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no books with this id '%s'", id));
                    break;
                }
                final Book bookToDelete = bookDao.findById(Long.valueOf(id)).get();
                final String title = substringAppend(bookToDelete.getTitle(), "..", 14);

                Optional<Client> bookHolder = Optional.ofNullable(bookToDelete.getClient());
                if (bookHolder.isPresent()) {
                    String name = substringAppend(bookHolder.get().getName(), "..", 15);
                    windowManager.showToast(format(
                            "Oops, but client '%s' holds this book '%s'", name, id));
                    break;
                }

                final boolean isConfirmed = windowManager.showDialogueToast(
                        format("You really need to delete book '%s'?", title), "YES", "NO");
                if (isConfirmed) {
                    if (bookDao.delete(bookToDelete)) {
                        windowManager.showToast("Book successfully deleted.");
                        if (isDetailsTab) {
                            windowManager.drawPrevTab();
                        }
                    } else {
                        windowManager.showToast("Book doesnt deleted.");
                    }
                }
            }
            case CLIENT -> {
                final String id = isDetailsTab
                        ? windowManager.getCachedClientId()
                        : args[0];

                if (clientDao.findById(Long.valueOf(id)).isEmpty()) {
                    windowManager.showToast(format(
                            "Oops, there are no clients with this id '%s'", id));
                    break;
                }
                Client clientToDelete = clientDao.findById(Long.valueOf(id)).get();
                String name = substringAppend(clientToDelete.getName(), "..", 15);

                if (clientToDelete.doesHoldBook()) {
                    windowManager.showToast(format(
                            "Oops, client '%s' is holding books, we can't delete him", name));
                    break;
                }

                final boolean isConfirmed = windowManager.showDialogueToast(
                        format("You really need to delete client '%s'?", name), "YES", "NO");
                if (isConfirmed) {
                    if (clientDao.delete(clientToDelete)) {
                        windowManager.showToast("Client successfully deleted.");
                        if (isDetailsTab) {
                            windowManager.drawPrevTab();
                        }
                    } else {
                        windowManager.showToast("Client doesnt deleted.");
                    }
                }
            }
            case NULL -> windowManager.showToast("You can not use command 'delete' in this tab.");
        }
    }

    public void take(final String[] args) {
        if (windowManager.getCurrentTabState() == LOOK_CLIENT) {
            if (args.length == 0) {
                // TODO fix message
                windowManager.showToast("You need to enter 'take' with '${id1} ${id2}..' of books");
                return;
            }

            final Client client = clientDao
                    .findById(Long.valueOf(windowManager.getCachedClientId()))
                    .get();

            // TODO optimize stub
            final List<Book> booksToTake = Arrays.stream(args)
                    .filter(bookId -> {
                        Optional<Book> optionalBook = bookDao.findById(Long.valueOf(bookId));
                        return optionalBook.isPresent() && optionalBook.get().isFree();
                    })
                    .map(bookId -> bookDao.findById(Long.valueOf(bookId)).get())
                    .toList();
            if (booksToTake.isEmpty()) {
                windowManager.showToast("Oops, all books you are enter not free");
            } else {
                clientDao.takeBook(client, booksToTake.get(0));
                windowManager.drawPrevTab();
                windowManager.clientDetails(
                        client,
                        client.getBooks().stream().toList()
                );
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

            final Client client = clientDao
                    .findById(Long.valueOf(windowManager.getCachedClientId()))
                    .get();

            // TODO optimize stub
            final List<Book> booksToReturn = Arrays.stream(args)
                    .filter(bookId -> bookDao.findById(Long.valueOf(bookId)).isPresent() &&
                                      // TODO FIXME stub
                                      client.getBooks().contains(bookId))
                    .map(bookId -> bookDao.findById(Long.valueOf(bookId)).get())
                    .toList();

            if (booksToReturn.isEmpty()) {
                windowManager.showToast("Oops, all books you are enter not yours");
            } else {
                // TODO fixme stub
                clientDao.returnBook(client, booksToReturn.get(0));
                windowManager.drawPrevTab();
                windowManager.clientDetails(
                        client,
                        // TODO fixme
                        client.getBooks().stream().toList()
                );
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
