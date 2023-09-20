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
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.utils.StringUtils;

import static ua.mibal.minervaTest.model.window.DataType.HISTORY;
import static ua.mibal.minervaTest.utils.StringUtils.isNumber;

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

    public ApplicationController(WindowManager windowManager,
                                 BookDao bookDao,
                                 OperationDao operationDao,
                                 ClientDao clientDao) {
        this.windowManager = windowManager;
        this.bookDao = bookDao;
        this.operationDao = operationDao;
        this.clientDao = clientDao;
    }

    public void tab1(final String[] ignored) {
        windowManager.tab1(bookDao::findAll);
    }

    public void tab2(final String[] ignored) {
        windowManager.tab2(clientDao::findAllFetchBooks);
    }

    public void tab3(final String[] ignored) {
        windowManager.tab3(operationDao::findAllFetchBookClient);
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
            case BOOK -> windowManager.searchBookTab(() -> bookDao.find(args), args);
            case CLIENT -> windowManager.searchClientTab(() -> clientDao.findFetchBooks(args), args);
            case HISTORY -> windowManager.searchOperationTab(() -> operationDao.findFetchBookClient(args), args);
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
            case BOOK -> windowManager.bookDetails(() -> bookDao.findById(id));
            case CLIENT -> windowManager.clientDetails(() -> clientDao.findByIdFetchBooks(id));
            case HISTORY -> windowManager.operationDetails(() -> operationDao.findByIdFetchBookClient(id));
            case NULL -> windowManager.showToast("You can not use command 'look' in this tab.");
        }
    }

    public void edit(final String[] args) {
        if (windowManager.getCurrentDataType() == HISTORY) {
            windowManager.showToast("You cannot change history manually");
            return;
        }
        if (args.length == 0 || !isNumber(args[0])) {
            windowManager.showToast("You need to enter 'edit' with ${id}");
            return;
        }
        final Long id = Long.valueOf(args[0]);
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> bookDao.findById(id)
                    .flatMap(windowManager::editBook).ifPresentOrElse(
                            book -> {
                                bookDao.update(book);
                                windowManager.refresh()
                                        .showToast("Book successfully updated!");
                            },
                            () -> windowManager.showToast("Oops, there are no books with this id=" + id)
                    );
            case CLIENT -> clientDao.findById(id)
                    .flatMap(windowManager::editClient).ifPresentOrElse(
                            client -> {
                                clientDao.update(client);
                                windowManager.refresh()
                                        .showToast("Client successfully updated!");
                            },
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
                    book -> windowManager.showToast(bookDao.save(book)
                            ? "Book successfully added!"
                            : "Book doesnt added("));
            case CLIENT -> windowManager.initClientToAdd().ifPresent(
                    client -> windowManager.showToast(clientDao.save(client)
                            ? "Client successfully added!"
                            : "Client doesnt added("));
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
        final Long id = Long.valueOf(args[0]);
        switch (windowManager.getCurrentDataType()) {
            case BOOK -> bookDao.findById(id).ifPresentOrElse(
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
                            if (bookDao.delete(bookToDel))
                                windowManager.refresh()
                                        .showToast("Book successfully deleted.");
                            else
                                windowManager.showToast("Book isn't deleted.");
                        }
                    },
                    () -> windowManager.showToast("Oops, there are no books with this id=" + id)
            );
            case CLIENT -> clientDao.findByIdFetchBooks(id).ifPresentOrElse(
                    clientToDel -> {
                        final String name = StringUtils.min(clientToDel.getName(), 15);
                        if (clientToDel.doesHoldBook()) {
                            windowManager.showToast(
                                    "Oops, but client " + name + " is holding books, we can't delete him(");
                            return;
                        }
                        final boolean isConfirmed = windowManager.showDialogueToast(
                                "You really need to delete client '" + name + "'?",
                                "YES", "NO");
                        if (isConfirmed) {
                            if (clientDao.delete(clientToDel)) {
                                windowManager.refresh()
                                        .showToast("Client successfully deleted.");
                            } else {
                                windowManager.showToast("Client isn't deleted.");
                            }
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
        clientDao.findById(clientId).orElseThrow(
                () -> new IllegalArgumentException("Client with id=" + clientId + " not found"));
        final Long bookId = Long.valueOf(args[1]);
        bookDao.findById(bookId).ifPresentOrElse(
                bookToTake -> {
                    if (!bookToTake.isFree()) {
                        windowManager.showToast("Oops, but book with id=" + bookId + " isn't free(");
                        return;
                    }
                    bookDao.takeBook(clientId, bookId);
                    windowManager.refresh()
                            .showToast("Book successfully taken!");
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
        final Client client = clientDao.findByIdFetchBooks(clientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Client with id=" + clientId + " not found"));
        final Long bookId = Long.valueOf(args[1]);
        bookDao.findById(bookId).ifPresentOrElse(
                bookToReturn -> {
                    if (!client.getBooks().contains(bookToReturn)) {
                        windowManager.showToast("Oops, but user with id=" + client +
                                                " doesn't hold this book with id=" + bookId);
                        return;
                    }
                    bookDao.returnBook(clientId, bookId);
                    windowManager.refresh()
                            .showToast("Books successfully returned!");
                },
                () -> windowManager.showToast("Oops, but book with id=" + bookId + " doesn't exists")
        );
    }

    @FunctionalInterface
    public interface ArrayConsumer<T> {

        void apply(T... args);
    }
}
