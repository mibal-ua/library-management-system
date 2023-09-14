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
import ua.mibal.minervaTest.dao.Dao;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.utils.StringUtils;

import static java.lang.String.format;
import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.LOOK_CLIENT;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ApplicationController {

    private final WindowManager windowManager;

    private final BookDao bookDao;
    private final Dao<Operation> operationDao;
    private final Dao<Client> clientDao;

    public ApplicationController(WindowManager windowManager,
                                 BookDao bookDao,
                                 Dao<Operation> operationDao,
                                 Dao<Client> clientDao) {
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
        windowManager.tab3(operationDao.findAll());
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
            case HISTORY -> windowManager.searchOperationTab(operationDao.find(args), args);
            case NULL -> windowManager.showToast("You can not use command 'search' in this tab.");
        }
    }

    public void look(final String[] args) {
        if (args.length == 0) {
            windowManager.showToast("You need to enter 'look' with ${query}");
            return;
        }
        final Long id = Long.valueOf(args[0]);
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> bookDao.findById(id).ifPresentOrElse(
                    windowManager::bookDetails,
                    () -> windowManager.showToast("Oops, there are no books with this id=" + id)
            );
            case CLIENT -> clientDao.findById(id).ifPresentOrElse(
                    windowManager::clientDetails,
                    () -> windowManager.showToast("Oops, there are no clients with this id=" + id)
            );
            case HISTORY -> operationDao.findById(id).ifPresentOrElse(
                    windowManager::operationDetails,
                    () -> windowManager.showToast("Oops, there are no operations with this id=" + id)
            );
            case NULL -> windowManager.showToast("You can not use command 'look' in this tab.");
        }
    }

    public void edit(final String[] args) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }

        final boolean isDetailsTab = windowManager.getCurrentTabState().isDetailsTab();
        if (!isDetailsTab && args.length == 0) {
            windowManager.showToast("You need to enter 'edit' with ${id}");
            return;
        }

        switch (windowManager.getCurrentDataType()) {
            case BOOK -> {
                final Long id = Long.valueOf(isDetailsTab
                        ? windowManager.getCachedBookId()
                        : args[0]
                );
                bookDao.findById(id)
                        .flatMap(windowManager::editBook).ifPresentOrElse(
                                book -> {
                                    bookDao.update(book);
                                    if (isDetailsTab) {
                                        windowManager.drawPrevTab(); // FIXME
                                        windowManager.bookDetails(book);
                                    }
                                    windowManager.showToast("Book successfully updated!");
                                },
                                () -> windowManager.showToast("Oops, there are no books with this id=" + id)
                        );
            }
            case CLIENT -> {
                final Long id = Long.valueOf(isDetailsTab
                        ? windowManager.getCachedClientId()
                        : args[0]
                );
                clientDao.findById(id)
                        .flatMap(windowManager::editClient).ifPresentOrElse(
                                client -> {
                                    clientDao.update(client);
                                    if (isDetailsTab) {
                                        windowManager.drawPrevTab(); // FIXME
                                        windowManager.clientDetails(client);
                                    }
                                    windowManager.showToast("Client successfully updated!");
                                },
                                () -> windowManager.showToast("Oops, there are no clients with this id=" + id)
                        );
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
            case BOOK -> windowManager.initBookToAdd().ifPresent(book -> {
                if (bookDao.save(book))
                    windowManager.showToast("Book successfully added!");
                else
                    windowManager.showToast("Book doesnt added(");
            });
            case CLIENT -> windowManager.initClientToAdd().ifPresent(client -> {
                if (clientDao.save(client))
                    windowManager.showToast("Client successfully added!");
                else
                    windowManager.showToast("Client doesnt added(");
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
        if (!isDetailsTab && args.length == 0) {
            windowManager.showToast("You need to enter 'delete' with ${id}");
            return;
        }

        switch (windowManager.getCurrentDataType()) {
            case BOOK -> {
                final Long id = Long.valueOf(isDetailsTab
                        ? windowManager.getCachedBookId()
                        : args[0]
                );
                bookDao.findById(id).ifPresentOrElse(
                        bookToDel -> {
                            final String title = StringUtils.min(bookToDel.getTitle(), 14);
                            if (!bookToDel.isFree()) {
                                windowManager.showToast("Oops, but book " + title + " isn't free");
                                return;
                            }
                            final boolean isConfirmed = windowManager.showDialogueToast(
                                    "You really need to delete book '" + title + "'?",
                                    "YES", "NO");
                            if (isConfirmed) {
                                if (bookDao.delete(bookToDel)) {
                                    windowManager.showToast("Book successfully deleted.");
                                    if (isDetailsTab) { // TODO FIXME
                                        windowManager.drawPrevTab();
                                    }
                                } else {
                                    windowManager.showToast("Book isn't deleted.");
                                }
                            }
                        },
                        () -> windowManager.showToast("Oops, there are no books with this id=" + id)
                );
            }
            case CLIENT -> {
                final Long id = Long.valueOf(isDetailsTab
                        ? windowManager.getCachedClientId()
                        : args[0]
                );
                clientDao.findById(id).ifPresentOrElse(
                        clientToDel -> {
                            final String name = StringUtils.min(clientToDel.getName(), 15);
                            if (clientToDel.doesHoldBook()) {
                                windowManager.showToast(format(
                                        "Oops, but client '%s' is holding books, we can't delete him(", name));
                                return;
                            }
                            final boolean isConfirmed = windowManager.showDialogueToast(
                                    "You really need to delete client '" + name + "'?",
                                    "YES", "NO");
                            if (isConfirmed) {
                                if (clientDao.delete(clientToDel)) {
                                    windowManager.showToast("Client successfully deleted.");
                                    if (isDetailsTab) { // TODO FIXME
                                        windowManager.drawPrevTab();
                                    }
                                } else {
                                    windowManager.showToast("Client isn't deleted.");
                                }
                            }
                        },
                        () -> windowManager.showToast("Oops, there are no clients with this id=" + id)
                );
            }
            case NULL -> windowManager.showToast("You can not use command 'delete' in this tab.");
        }
    }

    public void take(final String[] args) {
        if (windowManager.getCurrentTabState() != LOOK_CLIENT) {
            windowManager.showToast("You can not use command 'take' in this tab");
            return;
        }
        if (args.length == 0) {
            windowManager.showToast("You need to enter 'take' with ${id} of book");
            return;
        }

        final Long clientId = Long.valueOf(windowManager.getCachedClientId());
        final Client client = clientDao.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Internal error: client by cached id=" + clientId + " not found"));

        final Long bookId = Long.valueOf(args[0]);
        bookDao.findById(bookId).ifPresentOrElse(
                bookToTake -> {
                    bookDao.takeBook(clientId, bookId);
                    windowManager.drawPrevTab();
                    windowManager.clientDetails(client);
                    windowManager.showToast("Books successfully taken!");
                },
                () -> windowManager.showToast("Oops, all books you are enter not free")
        );
    }

    public void returnn(final String[] args) {
        if (windowManager.getCurrentTabState() != LOOK_CLIENT) {
            windowManager.showToast("You can not use command 'return' in this tab");
            return;
        }
        if (args.length == 0) {
            windowManager.showToast("You need to enter 'return' with ${id} of book");
            return;
        }

        final Long clientId = Long.valueOf(windowManager.getCachedClientId());
        final Client client = clientDao.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Internal error: client by cached id=" + clientId + " not found"));

        final Long bookId = Long.valueOf(args[0]);
        bookDao.findById(bookId).ifPresentOrElse(
                bookToReturn -> {
                    bookDao.returnBook(clientId, bookId);
                    windowManager.drawPrevTab();
                    windowManager.clientDetails(client);
                    windowManager.showToast("Books successfully returned!");
                },
                () -> windowManager.showToast("Oops, all books you are enter not yours")
        );
    }

    @FunctionalInterface
    public interface OwnFunction {

        void apply(String[] args);
    }
}
