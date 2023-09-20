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

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.component.DataPrinter;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.utils.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;
import static ua.mibal.minervaTest.component.console.ConsoleConstants.WINDOW_WIDTH;
import static ua.mibal.minervaTest.utils.StringUtils.substringAppend;


/**
 * @author Michael Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ConsoleDataPrinter implements DataPrinter {

    private static final String BOLD = "\033[1m";

    private static final String RESET = "\033[0m";

    public static String bold(final String str) {
        return BOLD + str + RESET;
    }

    @Override
    public void printListOfBooks(final Collection<Book> books) {
        final List<String> tableHeaders = List.of(" ID ", "Title", "Author", "Free");
        final int titleLength = 36;
        final int authorLength = 23;
        final List<Integer> sizes = List.of(4, titleLength, authorLength, 4);
        final List<Function<Book, String>> getters = List.of(
                book -> book.getId().toString(),
                book -> substringAppend(book.getTitle(), "..", titleLength),
                book -> substringAppend(book.getAuthor(), "..", authorLength),
                book -> book.isFree() ? "Yes" : "No"
        );
        printListTable(tableHeaders, sizes, getters, books);
    }

    @Override
    public void printListOfClients(final Collection<Client> clients) {
        final List<String> tableHeaders = List.of(" ID ", "Name", "Books");
        final int nameLength = 35;
        final int booksLength = 31;
        final List<Integer> sizes = List.of(4, nameLength, booksLength);
        // TODO FIXME stub
        final List<Function<Client, String>> getters = List.of(
                client -> client.getId().toString(),
                client -> substringAppend(client.getName(), "..", nameLength),
                client -> StringUtils.min(
                        client.getBooks().stream()
                                .map(book -> book.getId().toString())
                                .reduce("", (str1, str2) -> str1 + str2),
                        booksLength)
        );
        printListTable(tableHeaders, sizes, getters, clients);
    }

    @Override
    public void printListOfOperations(final Collection<Operation> operations) {
        final List<String> tableHeaders = List.of(" ID ", "Date", "Client name", "Operation", "Books");
        final int dateLength = 10;
        final int nameLength = 26;
        final int booksLength = 15;
        final List<Integer> sizes = List.of(4, dateLength, nameLength, 9, booksLength);
        final List<Function<Operation, String>> getters = List.of(
                operation -> operation.getId().toString(),
                operation -> operation.getDate().toString().substring(0, dateLength),
                operation -> StringUtils.min(operation.getClient().getName(), nameLength),
                operation -> operation.getOperationType().toString(),
                operation -> StringUtils.min(operation.getBook().getTitle(), booksLength)
        );
        printListTable(tableHeaders, sizes, getters, operations);
    }

    private <T> void printListTable(final List<String> tableHeaders,
                                    final List<Integer> sizes,
                                    final List<Function<T, String>> getters,
                                    final Collection<T> data) {
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
        valuesMap.put("ID", book.getId().toString());
        valuesMap.put("Title", book.getTitle());
        valuesMap.put("Subtitle", book.getSubtitle());
        valuesMap.put("Author", book.getAuthor());
        valuesMap.put("Publisher", book.getPublisher());
        valuesMap.put("Publish date", book.getPublishedDate().toString());
        valuesMap.put("Free", book.isFree() ? "YES" : "NO");
        printDetailsTable(valuesMap);
    }

    @Override
    public void printClientDetails(final Client client) {
        Map<String, String> valuesMap = new LinkedHashMap<>();
        valuesMap.put("ID", client.getId().toString());
        valuesMap.put("Name", client.getName());
        printDetailsTable(valuesMap);

        System.out.println("""
                                                  
                                            Books that client holds
                """);
        printListOfBooks(client.getBooks());
    }

    @Override
    public void printOperationDetails(final Operation operation) {
        Map<String, String> valuesMap = new LinkedHashMap<>();
        // TODO FIXME stub
        valuesMap.put("ID", operation.getId().toString());
        valuesMap.put("Date", operation.getDate().toString());
        valuesMap.put("Client", operation.getClient().getName());
        // TODO FIXME stub
        valuesMap.put("Type", operation.getOperationType().toString());
        printDetailsTable(valuesMap);

        System.out.println("""
                                                  
                                               Books in operation
                """);
        printListOfBooks(List.of(operation.getBook())); // TODO FIXME
    }

    @Override
    public void clear() {
        ConsoleUtils.clear();
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
