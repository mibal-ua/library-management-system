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

package ua.mibal.minervaTest.component.request;

import ua.mibal.minervaTest.component.DataOperator;
import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.component.UserInputReader;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Library;
import ua.mibal.minervaTest.model.OperationType;
import ua.mibal.minervaTest.model.Request;
import ua.mibal.minervaTest.model.command.CommandType;
import ua.mibal.minervaTest.model.command.DataType;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.OperationType.RETURN;
import static ua.mibal.minervaTest.model.OperationType.TAKE;
import static ua.mibal.minervaTest.model.command.CommandType.ADD;
import static ua.mibal.minervaTest.model.command.CommandType.DEL;
import static ua.mibal.minervaTest.model.command.CommandType.EXIT;
import static ua.mibal.minervaTest.model.command.DataType.BOOK;
import static ua.mibal.minervaTest.model.command.DataType.CLIENT;
import static ua.mibal.minervaTest.model.command.DataType.HISTORY;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class RequestProcessor {

    private final DataPrinter dataPrinter;

    private final UserInputReader inputReader;

    private final DataOperator dataOperator;

    private Library library;

    private boolean exit = false;

    public RequestProcessor(final DataPrinter dataPrinter, final UserInputReader inputReader,
                            final DataOperator dataOperator) {
        this.dataPrinter = dataPrinter;
        this.inputReader = inputReader;
        this.dataOperator = dataOperator;
    }

    public void process(final Library library, final Request request) {
        final CommandType commandType = request.getCommandType();
        final DataType dataType = request.getDataType();
        this.library = library;

        if (commandType == ADD) {
            if (dataType == BOOK) {
                dataPrinter.printInfoMessage("Enter book ID/title/author:");
                dataPrinter.printInfoMessage("Or enter '/all' to see all.");
                String input = inputReader.getUserInput();
                List<Book> books;
                if (input.equalsIgnoreCase("/all")) {
                    books = library.getBooks();
                } else {
                    books = library.findBooks(input);
                }
                dataPrinter.printListOfBooks(books);
            }
            if (dataType == CLIENT) {
                dataPrinter.printInfoMessage("Enter client ID/name/ID of book they took:");
                dataPrinter.printInfoMessage("Or enter '/all' to see all.");
                String input = inputReader.getUserInput();
                List<Client> clients;
                if (input.equalsIgnoreCase("/all")) {
                    clients = library.getClients();
                } else {
                    clients = library.findClients(input);
                }
                dataPrinter.printListOfClients(clients);
            }
            if (dataType == HISTORY) {
                dataPrinter.printListOfOperations(library.getOperations(), library.getClients());
            }
        }

        if (commandType == DEL) {
            if (dataType == BOOK) {
                // TODO
            }
            if (dataType == CLIENT) {
                Client client = null;
                while (client == null) {
                    dataPrinter.printInfoMessage("Enter client ID/name:");
                    String input = inputReader.getUserInput();
                    client = findConcretClient(input);
                }

                OperationType operationType = null;
                while (operationType == null) {
                    dataPrinter.printInfoMessage("""
                        Enter what you need:
                                            
                        /TAKE
                        /RETURN
                        """);
                    String input = inputReader.getUserInput();
                    if (!OperationType.contains(input.substring(1))) {
                        dataPrinter.printInfoMessage("Incorrect argument.");
                        continue;
                    }
                    operationType = OperationType.valueOf(
                        input.substring(1).toUpperCase());
                }
                dataPrinter.printInfoMessage("Enter book id you need to operate:");

                List<String> booksToOperate;
                if (operationType == TAKE) {
                    booksToOperate = initBooksToTake();
                    library.takeBooks(client, booksToOperate,
                        (bookId) -> {
                            throw new IllegalArgumentException(
                                "Oops, book '" + bookId + "' isn't  free");
                        }, (bookId) -> {
                            throw new IllegalArgumentException(
                                "Oops, book '" + bookId + "' doesn't  exists");
                        });
                }
                if (operationType == RETURN) {
                    booksToOperate = initBooksToReturn(client);
                    library.returnBooks(client, booksToOperate,
                        (bookId) -> {
                            throw new IllegalArgumentException(
                                "Oops, book '" + bookId + "' is  free");
                        }, (bookId) -> {
                            throw new IllegalArgumentException(
                                "Oops, book '" + bookId + "' doesn't  exists");
                        }, (clientt, book) -> {
                            throw new IllegalArgumentException(
                                "Oops, client '" + clientt.getName() + "' dont took book '" + book.getTitle() + "'");
                        });
                }
                dataOperator.updateLibrary(library);
            }
        }
    }

    private List<String> initBooksToReturn(final Client client) {
        List<String> books = new ArrayList<>();
        while (true) {
            dataPrinter.printInfoMessage("Enter book id/name that need to return:");
            String input = inputReader.getUserInput();
            if (client.getBooksIds().contains(input)) {
                dataPrinter.printInfoMessage("OK, book added to return list!");
                books.add(input);
                return books;
                // TODO add multiple books
            }
        }
    }

    private Client findConcretClient(final String input) {
        List<Client> clients = library.findClients(input);
        dataPrinter.printInfoMessage("OK, this is clients we found by request:");
        dataPrinter.printListOfClients(clients);
        int index = -1;
        while (index == -1) {
            dataPrinter.printInfoMessage(format(
                "Select one of this clients, enter it index ('1' to '%s') \n" +
                "Or '0' if there are no books you are looking for.", clients.size()
            ));
            index = Integer.parseInt(inputReader.getUserInput());
            if (index == 0) {
                break;
            }
            if (!(1 <= index && index <= clients.size())) {
                dataPrinter.printInfoMessage(format(
                    "Index '%d' dont belong range [1, %s]", clients.size()));
                continue;
            }
            return clients.get(index - 1);
        }
        return null;
    }

    private Book initNewBook() {
        dataPrinter.printInfoMessage("Enter book title:");
        final String title = inputReader.getUserInput();
        dataPrinter.printInfoMessage("Enter subtitle:");
        final String subtitle = inputReader.getUserInput();
        dataPrinter.printInfoMessage("Enter author:");
        final String author = inputReader.getUserInput();
        dataPrinter.printInfoMessage("Enter publish date (in format '2018-12-04'):");
        final String publishDate = inputReader.getUserInput();
        dataPrinter.printInfoMessage("Enter book publisher:");
        final String publisher = inputReader.getUserInput();
        return new Book(
            title,
            subtitle,
            author,
            publishDate,
            publisher,
            true
        );
    }

    private Client initNewClient() {
        dataPrinter.printInfoMessage("Enter client name:");
        final String name = inputReader.getUserInput();
        final List<String> clientBooks = initBooksToTake();
        return new Client(
            name,
            clientBooks
        );
    }

    private List<String> initBooksToTake() {
        List<String> books = new ArrayList<>();
        while (true) {
            dataPrinter.printInfoMessage("Enter book id/name that client took:");
            dataPrinter.printInfoMessage("If client dont took book, just click Enter");

            final String input = inputReader.getUserInput();
            if (input.equals("")) {
                return books;
            }
            final List<Book> foundBooks = library.findBooks(input);
            if (foundBooks.size() == 0) {
                dataPrinter.printInfoMessage("There are no books in library with this data ((");
                continue;
            }
            dataPrinter.printInfoMessage("OK, this books we found:");
            dataPrinter.printListOfBooks(foundBooks);
            int index = -1;
            while (index == -1) {
                dataPrinter.printInfoMessage(format(
                    "Select one of this books, enter it index ('1' to '%s') \n" +
                    "Or '0' if there are no books you are looking for.", foundBooks.size()
                ));
                index = Integer.parseInt(inputReader.getUserInput());
                if (index == 0) {
                    return books;
                }
                if (!(1 <= index && index <= foundBooks.size())) {
                    dataPrinter.printInfoMessage(format(
                        "Index '%d' dont belong range [1, %s]", foundBooks.size()));
                    index = -1;
                    continue;
                }
                Book book = foundBooks.get(index - 1);
                books.add(book.getId());
            }
        }
    }

    public boolean isExit() {
        return exit;
    }
}
