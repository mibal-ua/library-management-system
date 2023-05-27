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

package ua.mibal.minervaTest;


import ua.mibal.minervaTest.component.DataOperator;
import ua.mibal.minervaTest.component.WindowManager;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Library;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;
import static ua.mibal.minervaTest.utils.StringUtils.substring;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Application {

    private final DataOperator dataOperator;

    private final WindowManager windowManager;

    public Application(final DataOperator dataOperator,
                       final WindowManager windowManager) {
        this.dataOperator = dataOperator;
        this.windowManager = windowManager;
    }

    public void start() {
        Library library = dataOperator.getLibrary();
        windowManager.tab1(library);

        String[] input = windowManager.readCommandLine();
        while (true) {
            final String command = input[0];
            final String[] args = Arrays.copyOfRange(input, 1, input.length);
            switch (command) {
                case "1" -> windowManager.tab1(library);
                case "2" -> windowManager.tab2(library);
                case "3" -> windowManager.tab3(library);
                case "help" -> windowManager.help();
                case "esc" -> windowManager.parentTab();
                case "search", "s" -> {
                    if (args.length == 0) {
                        windowManager.showToast("You need to enter 'search' with ${query}");
                        break;
                    }
                    switch (windowManager.getCurrentDataType()) {
                        case BOOK -> windowManager.searchBookTab(library.findBooks(args), args);
                        case CLIENT -> windowManager.searchClientTab(library.findClients(args), args);
                        case HISTORY ->
                            windowManager.searchOperationTab(library.findOperations(args), library.getClients(), args);
                    }
                }
                case "add" -> {
                    if (windowManager.getCurrentDataType() == HISTORY) {
                        windowManager.showToast("You cannot change history manually");
                        break;
                    }
                    switch (windowManager.getCurrentDataType()) {
                        case BOOK -> {
                            Optional<Book> optionalBookToAdd = windowManager.initBookToAdd(library);
                            optionalBookToAdd.ifPresent(library::addBook);
                            optionalBookToAdd.ifPresent(
                                book -> windowManager.showToast("Book successfully added!"));
                            dataOperator.updateLibrary(library);
                        }
                        case CLIENT -> {
                            Optional<Client> optionalClientToAdd = windowManager.initClientToAdd(library);
                            optionalClientToAdd.ifPresent(library::addClient);
                            optionalClientToAdd.ifPresent(
                                client -> windowManager.showToast("Client successfully added!"));
                            dataOperator.updateLibrary(library);
                        }
                    }
                }
                case "delete", "del" -> {
                    if (windowManager.getCurrentDataType() == HISTORY) {
                        windowManager.showToast("You cannot change history manually");
                        break;
                    }
                    if (args.length == 0) {
                        windowManager.showToast("You need to enter 'delete' with ${id}");
                        break;
                    }
                    switch (windowManager.getCurrentDataType()) {
                        case BOOK -> {
                            final String id = args[0];
                            if (!library.isContainBookId(id)) {
                                windowManager.showToast(format(
                                    "Oops, there are no books with this id '%s'", id));
                                break;
                            }

                            final Book bookToDelete = library.findBookById(id).get();
                            final String title = substring(bookToDelete.getTitle(), 12) + "..";

                            Optional<Client> bookHolder = library.findClientByBookId(id);
                            if (bookHolder.isPresent()) {
                                String name = substring(bookHolder.get().getName(), 13) + "..";
                                windowManager.showToast(format(
                                    "Oops, but client '%s' holds this book '%s'", name, id));
                                break;
                            }

                            final boolean isConfirmed = windowManager.showDialogueToast(
                                format("You really need to delete book '%s'?", title), "YES", "NO");
                            if (isConfirmed) {
                                library.deleteBook(bookToDelete);
                                dataOperator.updateLibrary(library);
                                windowManager.showToast("Book successfully deleted.");
                            }
                        }
                        case CLIENT -> {
                            final String id = args[0];
                            if (!library.isContainClientId(id)) {
                                windowManager.showToast(format(
                                    "Oops, there are no clients with this id '%s'", id));
                                break;
                            }

                            Client clientToDelete = library.findClientById(id).get();
                            String name = substring(clientToDelete.getName(), 13) + "..";

                            if (library.doesClientHoldBook(clientToDelete)) {
                                windowManager.showToast(format(
                                    "Oops, client '%s' is holding books, we can't delete him", name));
                                break;
                            }

                            final boolean isConfirmed = windowManager.showDialogueToast(
                                format("You really need to delete client '%s'?", name), "YES", "NO");
                            if (isConfirmed) {
                                library.deleteClient(clientToDelete);
                                dataOperator.updateLibrary(library);
                                windowManager.showToast("Client successfully deleted.");
                            }
                        }
                    }
                }
                case "exit" -> {
                    final boolean isExit = windowManager.showDialogueToast(
                        "You really need to exit?", "YES", "NO");
                    if (isExit) {
                        return;
                    }
                }
                default -> windowManager.showToast(format("Unrecognizable command '%s'", command));
            }
            input = windowManager.readCommandLine();
        }
    }
}
