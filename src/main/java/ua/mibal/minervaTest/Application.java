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
import ua.mibal.minervaTest.model.window.State;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;
import static ua.mibal.minervaTest.model.window.State.SEARCH_BOOK;
import static ua.mibal.minervaTest.model.window.State.SEARCH_CLIENT;
import static ua.mibal.minervaTest.model.window.State.SEARCH_HISTORY;
import static ua.mibal.minervaTest.model.window.State.TAB_1;
import static ua.mibal.minervaTest.model.window.State.TAB_2;
import static ua.mibal.minervaTest.model.window.State.TAB_3;
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

    // FIXME delete var
    private State currentTab = TAB_1;

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
                case "1" -> {
                    windowManager.tab1(library);
                    currentTab = TAB_1;
                }
                case "2" -> {
                    windowManager.tab2(library);
                    currentTab = TAB_2;
                }
                case "3" -> {
                    windowManager.tab3(library);
                    currentTab = TAB_3;
                }
                case "help" -> windowManager.help();
                case "search", "s" -> {
                    if (args.length == 0) {
                        windowManager.showToast("You need to enter 'search' with ${query}", currentTab);
                        break;
                    }
                    switch (currentTab.getDataType()) {
                        case BOOK -> {
                            windowManager.searchBookTab(library.findBooks(args), args);
                            currentTab = SEARCH_BOOK;
                        }
                        case CLIENT -> {
                            windowManager.searchClientTab(library.findClients(args), args);
                            currentTab = SEARCH_CLIENT;
                        }
                        case HISTORY -> {
                            windowManager.searchOperationTab(library.findOperations(args), library.getClients(), args);
                            currentTab = SEARCH_HISTORY;
                        }
                    }
                }
                case "add" -> {
                    if (currentTab.getDataType() == HISTORY) {
                        windowManager.showToast("You cannot change history manually", currentTab);
                        break;
                    }
                    switch (currentTab.getDataType()) {
                        case BOOK -> {
                            Optional<Book> optionalBookToAdd = windowManager.initBookToAdd(library, currentTab);
                            optionalBookToAdd.ifPresent(library::addBook);
                            optionalBookToAdd.ifPresent(
                                book -> windowManager.showToast("Book successfully added!", currentTab));
                            dataOperator.updateLibrary(library);
                        }
                        case CLIENT -> {
                            Optional<Client> optionalClientToAdd = windowManager.initClientToAdd(library, currentTab);
                            optionalClientToAdd.ifPresent(library::addClient);
                            optionalClientToAdd.ifPresent(
                                client -> windowManager.showToast("Client successfully added!", currentTab));
                            dataOperator.updateLibrary(library);
                        }
                    }
                }
                case "delete", "del" -> {
                    if (currentTab.getDataType() == HISTORY) {
                        windowManager.showToast("You cannot change history manually", currentTab);
                        break;
                    }
                    if (args.length == 0) {
                        windowManager.showToast("You need to enter 'delete' with ${id}", currentTab);
                        break;
                    }
                    switch (currentTab.getDataType()) {
                        case BOOK -> {
                            final String id = args[0];
                            Optional<Book> optionalBookToDelete = library.findBookById(id);
                            if (optionalBookToDelete.isEmpty()) {
                                windowManager.showToast(format(
                                    "Oops, there are no books with this id '%s'", id), currentTab);
                                break;
                            }
                            Book bookToDelete = optionalBookToDelete.get();
                            Optional<Client> optionalClient = library.findClientByBookId(id);
                            if (optionalClient.isPresent()) {
                                Client client = optionalClient.get();
                                String name = client.getName().substring(0, 8) + "...";
                                windowManager.showToast(format(
                                    "Oops, but client '%s' holds this book '%s'", name, id), currentTab);
                                break;
                            }
                            String title = bookToDelete.getTitle().substring(0, 12) + "...";
                            final boolean isConfirmed = windowManager.showDialogueToast(
                                format("You really need to delete book '%s'?", title), "YES", "NO", currentTab);
                            if (isConfirmed) {
                                library.deleteBook(bookToDelete);
                                dataOperator.updateLibrary(library);
                                windowManager.showToast("Book successfully deleted.", currentTab);
                            }
                        }
                        case CLIENT -> {
                            final String id = args[0];
                            if (!library.isContainClientId(id)) {
                                windowManager.showToast(format(
                                    "Oops, there are no clients with this id '%s'", id), currentTab);
                                break;
                            }

                            Client clientToDelete = library.findClientById(id).get();
                            String name = substring(clientToDelete.getName(), 13) + "..";

                            if (library.doesClientHoldBook(clientToDelete)) {
                                windowManager.showToast(format(
                                    "Oops, client '%s' is holding books, we can't delete him", name), currentTab);
                                break;
                            }

                            final boolean isConfirmed = windowManager.showDialogueToast(
                                format("You really need to delete client '%s'?", name), "YES", "NO", currentTab);
                            if (isConfirmed) {
                                library.deleteClient(clientToDelete);
                                dataOperator.updateLibrary(library);
                                windowManager.showToast("Client successfully deleted.", currentTab);
                            }
                        }
                    }
                }
                case "exit" -> {
                    final boolean isExit = windowManager.showDialogueToast(
                        "You really need to exit?", "YES", "NO", currentTab);
                    if (isExit) {
                        return;
                    }
                }
                default -> windowManager.showToast(format("Unrecognizable command '%s'", command), currentTab);
            }
            input = windowManager.readCommandLine();
        }
    }
}
