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
import ua.mibal.minervaTest.model.Request;
import ua.mibal.minervaTest.model.command.CommandType;
import ua.mibal.minervaTest.model.command.DataType;
import static java.lang.String.format;
import static ua.mibal.minervaTest.model.command.CommandType.GET;
import static ua.mibal.minervaTest.model.command.CommandType.PATCH;
import static ua.mibal.minervaTest.model.command.CommandType.POST;
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

    private boolean update = false;

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

        if (commandType == GET) {
            if (dataType == BOOK) {
                dataPrinter.printInfoMessage("Enter bookID/title/author:");
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
                dataPrinter.printInfoMessage("Enter clientID/name:");
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
                dataPrinter.printListOfOperations(library.getOperations());
            }
        }

        if (commandType == POST) {
            if (dataType == BOOK) {
                Book newBook = initNewBook();
                if (dataOperator.addBook(newBook)) {
                    dataPrinter.printInfoMessage("Book successfully added!");
                }
                update = true;
            }
            if (dataType == CLIENT) {
                Client newClient = initNewClient();
                if (dataOperator.addClient(newClient)) {
                    dataPrinter.printInfoMessage("Client successfully added!");
                }
                update = true;
            }
        }

        if (commandType == PATCH) {
            // TODO
        }
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
        dataPrinter.printInfoMessage("Enter page count:");
        final String pageCount = inputReader.getUserInput();
        dataPrinter.printInfoMessage("Enter book description:");
        final String description = inputReader.getUserInput();
        dataPrinter.printInfoMessage("Enter linked website:");
        final String website = inputReader.getUserInput();
        return new Book(
            String.valueOf(title.hashCode()),
            title,
            subtitle,
            author,
            publishDate,
            publisher,
            Integer.parseInt(pageCount),
            description,
            website,
            true
        );
    }

    private Client initNewClient() {
        dataPrinter.printInfoMessage("Enter client name:");
        final String name = inputReader.getUserInput();
        final List<String> clientBooks = new ArrayList<>();
        while (true) {
            dataPrinter.printInfoMessage("Enter book id/name that client took:");
            dataPrinter.printInfoMessage("If client dont took book, just click Enter");

            final String input = inputReader.getUserInput();
            if (input.equals("")) {
                break;
            }
            final List<Book> foundBooks = library.findBooks(input);
            if (foundBooks.size() == 0) {
                dataPrinter.printInfoMessage("There are no books in library with this data ((");
                continue;
            }
            dataPrinter.printInfoMessage("OK, this is books we found:");
            dataPrinter.printListOfBooks(foundBooks);
            int index = -1;
            while (index == -1) {
                dataPrinter.printInfoMessage(format(
                    "Select one of this books, enter it index ('1' to '%s') \n" +
                    "Or '0' if there are no books you are looking for.", foundBooks.size()
                ));
                index = Integer.parseInt(inputReader.getUserInput());
                if (index == 0) {
                    break;
                }
                if (!(1 <= index && index <= foundBooks.size())) {
                    dataPrinter.printInfoMessage(format(
                        "Index '%d' dont belong range [1, %s]", foundBooks.size()));
                    continue;
                }
                Book book = foundBooks.get(index - 1);
                clientBooks.add(book.getId());
            }
        }
        return new Client(
            String.valueOf(name.hashCode()),
            name,
            clientBooks
        );
    }

    public boolean isExit() {
        return exit;
    }

    public boolean isUpdate() {
        return update;
    }
}
