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
import java.util.function.Function;

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
        final List<String> tableHeaders = List.of(" ID ", "Title", "Author", "Free");
        final int titleLength = 36;
        final int authorLength = 23;
        final List<Integer> sizes = List.of(4, titleLength, authorLength, 4);
        final List<Function<Book, String>> getters = List.of(
                book -> book.getId(),
                book -> substring(book.getTitle(), titleLength),
                book -> substring(book.getAuthor(), authorLength),
                book -> book.isFree() ? "Yes" : "No"
        );
        printListTable(tableHeaders, sizes, getters, books);
    }

    @Override
    public void printListOfClients(final List<Client> clients) {
        final List<String> tableHeaders = List.of(" ID ", "Name", "Books");
        final int nameLength = 35;
        final int booksLength = 31;
        final List<Integer> sizes = List.of(4, nameLength, booksLength);
        final List<Function<Client, String>> getters = List.of(
                client -> client.getId(),
                client -> substring(client.getName(), nameLength),
                client -> substring(String.join(" ", client.getBooksIds()), booksLength)
        );
        printListTable(tableHeaders, sizes, getters, clients);
    }

    @Override
    public void printListOfOperations(final List<Operation> operations, final List<Client> clients) {
        final List<String> tableHeaders = List.of(" ID ", "Date", "Client name", "Operation", "Books");
        final int dateLength = 10;
        final int nameLength = 26;
        final int booksLength = 15;
        final List<Integer> sizes = List.of(4, dateLength, nameLength, 9, booksLength);
        final List<Function<Operation, String>> getters = List.of(
                operation -> operation.getId(),
                operation -> substring(operation.getDate(), dateLength),
                operation -> {
                    // FIXME use library instead of searching
                    Client client = null;
                    for (final Client cl : clients) {
                        if (cl.getId().equals(operation.getClientId())) {
                            client = cl;
                            break;
                        }
                    }
                    return client == null
                            ? "NONE"
                            : substring(client.getName(), nameLength);
                },
                operation -> operation.getOperationType(),
                operation -> substring(String.join(" ", operation.getBooksIds()), booksLength)
        );
        printListTable(tableHeaders, sizes, getters, operations);
    }

    private <T> void printListTable(final List<String> tableHeaders,
                                    final List<Integer> sizes,
                                    final List<Function<T, String>> getters,
                                    final List<T> data) {
        if (data.size() == 0) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                List is empty                                 |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }
        if (tableHeaders.size() != getters.size()) {
            throw new IllegalArgumentException(format(
                    "Number of table names (%d) differs from number of getters (%d)",
                    tableHeaders.size(), getters.size()));
        }
        if (sizes.isEmpty()) {
            throw new IllegalArgumentException("Sizes list is empty");
        }

        final StringBuilder dataTemplateBuilder = new StringBuilder("|");
        final StringBuilder dividerBuilder = new StringBuilder("+");
        for (Integer size : sizes) {
            dataTemplateBuilder.append(" %-").append(size).append("s |");
            dividerBuilder.append("-").append("-".repeat(size)).append("-+");
        }

        final String dataTemplate = dataTemplateBuilder.append("%n").toString();
        final String divider = dividerBuilder.append("\n").toString();

        System.out.print(divider);
        System.out.printf(dataTemplate, tableHeaders.toArray());
        System.out.print(divider);
        for (T el : data) {
            final List<String> fields = getters.stream()
                    .map(getter -> getter.apply(el))
                    .toList();
            System.out.printf(dataTemplate, fields.toArray());
        }
        System.out.print(divider);
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
