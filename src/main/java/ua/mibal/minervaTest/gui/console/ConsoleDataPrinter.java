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
import ua.mibal.minervaTest.model.Entity;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.utils.StringUtils;

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

    private final static Map<Class<? extends Entity>, DataBundle<? extends Entity>> map = Map.of(
            Book.class, new DataBundle<Book>(
                    List.of(" ID ", "Title", "Author", "Free"),
                    List.of(4, 36, 23, 4),
                    List.of(
                            Book::getId,
                            Book::getTitle,
                            Book::getAuthor,
                            book -> book.isFree() ? "Yes" : "No")),
            Client.class, new DataBundle<Client>(
                    List.of(" ID ", "Name", "Books"),
                    List.of(4, 35, 31),
                    List.of(
                            Client::getId,
                            Client::getName,
                            client -> client.getBooks().stream()
                                    .map(book -> book.getId().toString())
                                    .reduce("", (str1, str2) -> str1 + str2 + " "))),
            Operation.class, new DataBundle<Operation>(
                    List.of(" ID ", "Date", "Client name", "Operation", "Books"),
                    List.of(4, 10, 26, 9, 15),
                    List.of(
                            Operation::getId,
                            operation -> operation.getDate().toLocalDate(),
                            operation -> operation.getClient().getName(),
                            Operation::getOperationType,
                            operation -> operation.getBook().getTitle()))
    );


    @Override
    public void printEntityDetails(Entity entity) {
        // TODO FIXME
        if (entity instanceof Book book) {
            printBookDetails(book);
        } else if (entity instanceof Client client) {
            printClientDetails(client);
        } else if (entity instanceof Operation operation) {
            printOperationDetails(operation);
        } else {
            throw new IllegalStateException("Illegal entity class " + entity.getClass());
        }
    }

    @Override
    public <T extends Entity> void printListOfEntities(Collection<T> data) {
        if (data.isEmpty()) {
            System.out.format("+------------------------------------------------------------------------------+%n");
            System.out.format("|                                List is empty                                 |%n");
            System.out.format("+------------------------------------------------------------------------------+%n");
            return;
        }
        DataBundle<T> dataBundle = (DataBundle<T>) map.get(data.iterator().next().getClass());

        if (dataBundle.sizes.isEmpty())
            throw new IllegalArgumentException("Sizes list is empty");

        final StringBuilder dataTemplateBuilder = new StringBuilder("|");
        final StringBuilder dividerBuilder = new StringBuilder("+");
        for (int i = 0; i < dataBundle.sizes.size(); i++) {
            Integer size = dataBundle.sizes.get(i);

            Function<Object, Object> trimStrAppendDotsFn = obj -> StringUtils.min(obj.toString(), size);
            dataBundle.fields.set(i, trimStrAppendDotsFn.compose(dataBundle.fields.get(i)));

            dataTemplateBuilder.append(" %-").append(size).append("s |");
            dividerBuilder.append("-").append("-".repeat(size)).append("-+");
        }

        final String dataTemplate = dataTemplateBuilder.append("%n").toString();
        final String divider = dividerBuilder.append("\n").toString();

        System.out.print(divider);
        System.out.printf(dataTemplate, dataBundle.headers.toArray());
        System.out.print(divider);
        for (T el : data) {
            final List<String> fields = dataBundle.fields.stream()
                    .map(getter -> getter.apply(el).toString())
                    .toList();
            System.out.printf(dataTemplate, fields.toArray());
        }
        System.out.print(divider);
    }

    private void printBookDetails(final Book book) {
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

    private void printClientDetails(final Client client) {
        Map<String, Object> dataRows = new LinkedHashMap<>();
        dataRows.put("ID", client.getId());
        dataRows.put("Name", client.getName());
        printDetailsTable(dataRows);

        System.out.println("""
                                                  
                                            Books that client holds
                """);
        printListOfEntities(client.getBooks());
    }

    private void printOperationDetails(final Operation operation) {
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

    @Override
    public void printHelp() {
        System.out.println("""

                                                  MAIN CONTROL
                --------------------------------------------------------------------------------

                                               1, 2, 3 - open tab

                                                  exit - to exit


                                                    IN TABS
                --------------------------------------------------------------------------------

                          search(s) ${query} - search element in current tab

                                  look ${id} - look at concrete item in list in current tab

                                         add - to add element into list in current tab

                           delete(del) ${id} - to delete element in list in current tab


                                             IN CONCRETE BOOK/CLIENT
                --------------------------------------------------------------------------------

                                           edit - to edit this book/client

                                    delete(del) - to delete this book/client

                                            esc - go to previous window


                                               IN CONCRETE CLIENT
                --------------------------------------------------------------------------------

                                            take ${id} - to take book

                                          return ${id} - to return book

                 """);
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

    private record DataBundle<T extends Entity>(
            List<String> headers,
            List<Integer> sizes,
            List<Function<T, Object>> fields
    ) {
    }
}
