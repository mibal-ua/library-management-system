/*
 * Copyright (c) 2022. http://t.me/mibal_ua
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
 *
 */

package ua.mibal.minervaTest.component.console;

import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import java.util.List;


/**
 * @author Michael Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleDataPrinter implements DataPrinter {

    public static String BOLD = "\033[1m";

    public static String RESET = "\033[0m";

    @Override
    public void printInfoMessage(final String message) {
        System.out.print(message);
    }

    @Override
    public void printlnInfoMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void printErrorMessage(final String message) {
        System.err.println(message);
    }

    @Override
    public void printListOfBooks(final List<Book> books) {
        if (books.size() == 0) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                 List is empty                                |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }

        System.out.format("+------+--------------------------------------+-------------------------+------+%n");
        System.out.format("|  ID  | Title                                | Author                  | Free |%n");
        System.out.format("+------+--------------------------------------+-------------------------+------+%n");

        final int titleLength = 36;
        final int authorLength = 23;
        final String leftAlignFormat = "| %-4s | %-" + titleLength + "s | %-" + authorLength + "s | %-4s |%n";

        for (final Book book : books) {
            String title = book.getTitle();
            if (title.length() > titleLength) {
                title = title.substring(0, titleLength - 3) + "...";
            }
            String author = book.getAuthor();
            if (author.length() > authorLength) {
                author = author.substring(0, authorLength - 3) + "...";
            }
            System.out.format(leftAlignFormat, book.getId(), title, author, book.isFree() ? "Yes" : "No");
        }
        System.out.format("+------+--------------------------------------+-------------------------+------+%n");
    }

    @Override
    public void printListOfClients(final List<Client> clients) {
        if (clients.size() == 0) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                List is empty                                 |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }
        System.out.format("+------+-------------------------------------+---------------------------------+%n");
        System.out.format("|  ID  | Name                                | Books                           |%n");
        System.out.format("+------+-------------------------------------+---------------------------------+%n");

        final int nameLength = 35;
        final int booksLength = 31;
        final String leftAlignFormat = "| %-4s | %-" + nameLength + "s | %-" + booksLength + "s |%n";

        for (final Client client : clients) {
            String name = client.getName();
            if (name.length() > nameLength) {
                name = name.substring(0, nameLength - 3) + "...";
            }
            String books = String.join(" ", client.getBooksIds());
            if (books.length() > booksLength) {
                books = books.substring(0, booksLength - 3) + "...";
            }
            System.out.format(leftAlignFormat, client.getId(), name, books);
        }
        System.out.format("+------+-------------------------------------+---------------------------------+%n");
    }

    @Override
    public void printListOfOperations(final List<Operation> operations, final List<Client> clients) {
        if (operations.size() == 0) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                List is empty                                 |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }

        System.out.format("+------------------+-----------------------------+-----------+-----------------+%n");
        System.out.format("|       Date       | Client name                 | Operation | Books           |%n");
        System.out.format("+------------------+-----------------------------+-----------+-----------------+%n");

        final int booksLength = 15;
        final int nameLength = 27;
        final String leftAlignFormat = "| %-16s | %-" + nameLength + "s | %-9s | %-" + booksLength + "s |%n";

        for (final Operation operation : operations) {
            Client client = null;
            for (final Client cl : clients) {
                if (cl.getId().equals(operation.getClientId())) {
                    client = cl;
                    break;
                }
            }
            String name = client == null
                ? "NONE"
                : client.getName();
            if (name.length() > nameLength) {
                name = name.substring(0, nameLength - 3) + "...";
            }
            String books = String.join(" ", operation.getBooksIds());
            if (books.length() > booksLength) {
                books = books.substring(0, booksLength - 3) + "...";
            }
            System.out.format(leftAlignFormat, operation.getDate(), name, operation.getOperationType(), books);
        }
        System.out.format("+------------------+-----------------------------+-----------+-----------------+%n");
    }

    @Override
    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
