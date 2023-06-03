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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static ua.mibal.minervaTest.component.console.ConsoleWindowManager.WINDOW_WIDTH;
import static ua.mibal.minervaTest.utils.StringUtils.substring;


/**
 * @author Michael Balakhon
 * @link t.me/mibal_ua
 */
public class ConsoleDataPrinter implements DataPrinter {

    public static String BOLD = "\033[1m";

    public static String RESET = "\033[0m";

    public static String bold(final String str) {
        return BOLD + str + RESET;
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

        System.out.format("+------+------------+----------------------------+-----------+-----------------+%n");
        System.out.format("|  ID  | Date       | Client name                | Operation | Books           |%n");
        System.out.format("+------+------------+----------------------------+-----------+-----------------+%n");

        final int booksLength = 15;
        final int nameLength = 26;
        final String leftAlignFormat = "| %-4s | %-10s | %-" + nameLength + "s | %-9s | %-" + booksLength + "s |%n";

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
                    : substring(client.getName(), nameLength);
            final String books = substring(String.join(" ", operation.getBooksIds()), booksLength);
            final String date = substring(operation.getDate(), 10); // only "yyyy-MM-dd"
            System.out.format(leftAlignFormat, operation.getId(), date, name, operation.getOperationType(), books);
        }
        System.out.format("+------+------------+----------------------------+-----------+-----------------+%n");
    }

    @Override
    public void printBookDetails(final Book book) {
        Map<String, String> valuesMap = new LinkedHashMap<>();
        valuesMap.put("ID", book.getId());
        valuesMap.put("Title", book.getTitle());
        valuesMap.put("Subtitle", book.getSubtitle());
        valuesMap.put("Publisher", book.getPublisher());
        valuesMap.put("Publish date", book.getPublishedDate());
        valuesMap.put("Free", book.isFree() ? "YES" : "NO");
        printDetailsTable(valuesMap);
    }

    @Override
    public void printClientDetails(final Client client, final List<Book> booksThatClientHolds) {
        Map<String, String> valuesMap = new LinkedHashMap<>();
        valuesMap.put("ID", client.getId());
        valuesMap.put("Name", client.getName());
        printDetailsTable(valuesMap);

        System.out.println("""
                                                  
                                            Books that client holds
                """);
        printListOfBooks(booksThatClientHolds);
    }

    @Override
    public void printOperationDetails(final Operation operation, final Client clientInOperation, final List<Book> booksInOperation) {
        Map<String, String> valuesMap = new LinkedHashMap<>();
        valuesMap.put("ID", operation.getId());
        valuesMap.put("Date", operation.getDate());
        valuesMap.put("Client", clientInOperation.getName());
        valuesMap.put("Type", operation.getOperationType());
        printDetailsTable(valuesMap);

        System.out.println("""
                                                  
                                               Books in operation
                """);
        printListOfBooks(booksInOperation);
    }

    @Override
    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printDetailsTable(final Map<String, String> valuesMap) {
        if (valuesMap.isEmpty()) {
            throw new RuntimeException("Map with values is empty.");
        }

        int keyMaxLength = -1;
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            int keyLength = entry.getKey().length();
            if (keyLength > keyMaxLength) {
                keyMaxLength = keyLength;
            }
        }

        int valueMaxLength = (WINDOW_WIDTH - keyMaxLength - 7);
        final String template = "| %-" + keyMaxLength + "s | %-" + valueMaxLength + "s |%n";
        final String divider = format("+-%s-+-%s-+%n", "-".repeat(keyMaxLength), "-".repeat(valueMaxLength));

        System.out.print(divider);
        for (Map.Entry<String, String> entry : valuesMap.entrySet()) {
            System.out.format(template, entry.getKey(), entry.getValue());
            System.out.print(divider);
        }
    }
}
