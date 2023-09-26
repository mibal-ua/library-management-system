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

package ua.mibal.minervaTest.gui.console;

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.gui.DataPrinter;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.lang.String.format;


/**
 * @author Michael Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ConsoleDataPrinter implements DataPrinter {

    @Override
    public void printListOfBooks(final Collection<Book> books) {
        printListTable(
                List.of(" ID ", "Title", "Author", "Free"),
                List.of(4, 36, 23, 4),
                new ArrayList<>(List.of(
                        Book::getId,
                        Book::getTitle,
                        Book::getAuthor,
                        book -> book.isFree() ? "Yes" : "No"
                )), books
        );
    }

    @Override
    public void printListOfClients(final Collection<Client> clients) {
        printListTable(
                List.of(" ID ", "Name", "Books"),
                List.of(4, 35, 31),
                new ArrayList<>(List.of(
                        Client::getId,
                        Client::getName,
                        client -> client.getBooks().stream()
                                .map(book -> book.getId().toString())
                                .reduce("", (str1, str2) -> str1 + str2 + " ")
                )), clients
        );
    }

    @Override
    public void printListOfOperations(final Collection<Operation> operations) {
        printListTable(
                List.of(" ID ", "Date", "Client name", "Operation", "Books"),
                List.of(4, 10, 26, 9, 15),
                new ArrayList<>(List.of(
                        Operation::getId,
                        operation -> operation.getDate().toLocalDate(),
                        operation -> operation.getClient().getName(),
                        Operation::getOperationType,
                        operation -> operation.getBook().getTitle()
                )), operations);
    }

    private <T> void printListTable(final List<String> tableHeaders,
                                    final List<Integer> sizes,
                                    final List<Function<T, Object>> getters,
                                    final Collection<T> data) {
        if (data.isEmpty()) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                List is empty                                 |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }
        if (tableHeaders.size() != getters.size())
            throw new IllegalArgumentException(format(
                    "Number of table names (%d) differs from number of getters (%d)",
                    tableHeaders.size(), getters.size()));
        if (sizes.isEmpty())
            throw new IllegalArgumentException("Sizes list is empty");

        final StringBuilder dataTemplateBuilder = new StringBuilder("|");
        final StringBuilder dividerBuilder = new StringBuilder("+");
        for (int i = 0; i < sizes.size(); i++) {
            Integer size = sizes.get(i);

            Function<Object, Object> trimStrAppendDotsFn = obj -> StringUtils.min(obj.toString(), size);
            getters.set(i, trimStrAppendDotsFn.compose(getters.get(i)));

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
                    .map(getter -> getter.apply(el).toString())
                    .toList();
            System.out.printf(dataTemplate, fields.toArray());
        }
        System.out.print(divider);
    }

    @Override
    public void printBookDetails(final Book book) {
        Map<String, Object> dataRows = new LinkedHashMap<>();
        dataRows.put("ID", book.getId());
        dataRows.put("Title", book.getTitle());
        dataRows.put("Subtitle", book.getSubtitle());
        dataRows.put("Author", book.getAuthor());
        dataRows.put("Publisher", book.getPublisher());
        dataRows.put("Publish date", book.getPublishedDate());
        dataRows.put("Free", book.isFree() ? "YES" : "NO");
        if (!book.isFree())
            dataRows.put("Client", book.getClient()
                    .map(Client::getName)
                    .orElse("NONE"));
        printDetailsTable(dataRows);
    }

    @Override
    public void printClientDetails(final Client client) {
        Map<String, Object> dataRows = new LinkedHashMap<>();
        dataRows.put("ID", client.getId());
        dataRows.put("Name", client.getName());
        printDetailsTable(dataRows);

        System.out.println("""
                                                  
                                            Books that client holds
                """);
        printListOfBooks(client.getBooks());
    }

    @Override
    public void printOperationDetails(final Operation operation) {
        Map<String, Object> dataRows = new LinkedHashMap<>();
        dataRows.put("ID", operation.getId());
        dataRows.put("Date", operation.getDate());
        dataRows.put("Client", operation.getClient().getName());
        dataRows.put("Operation", operation.getOperationType());
        dataRows.put("Book", operation.getBook().getTitle());
        printDetailsTable(dataRows);
    }

    @Override
    public void clear() {
        ConsoleUtils.clear();
    }

    private void printDetailsTable(final Map<String, Object> dataRows) {
        if (dataRows.isEmpty()) {
            throw new RuntimeException("Map with values is empty.");
        }

        int keyMaxLength = -1;
        for (Map.Entry<String, Object> entry : dataRows.entrySet()) {
            int keyLength = entry.getKey().length();
            if (keyLength > keyMaxLength) {
                keyMaxLength = keyLength;
            }
        }

        int valueMaxLength = (ConsoleConstants.WINDOW_WIDTH - keyMaxLength - 7);
        final String template = "| %-" + keyMaxLength + "s | %-" + valueMaxLength + "s |%n";
        final String divider = format("+-%s-+-%s-+%n", "-".repeat(keyMaxLength), "-".repeat(valueMaxLength));

        System.out.print(divider);
        for (Map.Entry<String, Object> entry : dataRows.entrySet()) {
            System.out.format(template, entry.getKey(), entry.getValue());
            System.out.print(divider);
        }
    }
}
