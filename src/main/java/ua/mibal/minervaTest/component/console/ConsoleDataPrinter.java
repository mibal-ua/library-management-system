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
import static java.lang.String.format;
import java.util.List;


/**
 * @author Michael Balakhon
 * @link http://t.me/mibal_ua
 */
public class ConsoleDataPrinter implements DataPrinter {

    @Override
    public void printInfoMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void printErrorMessage(final String message) {
        System.err.println(message);
    }

    @Override
    public void printListOfBooks(final List<Book> books) {
        books.stream().forEach(book -> {
            System.out.println((format("""
                    {
                       "id": "%s",
                       "title": "%s",
                       "subtitle": "%s",
                       "author": "%s",
                       "published": "%s",
                       "publisher": "%s",
                       "pages": %s,
                       "description": "%s",
                       "website": "%s",
                       "isFree": %s
                     }""",
                book.getId(),
                book.getTitle(),
                book.getSubtitle(),
                book.getAuthor(),
                book.getPublishedDate(),
                book.getPublisher(),
                book.getPages(),
                book.getDescription(),
                book.getWebsite(),
                book.isFree()
            )));
        });
    }

    @Override
    public void printListOfClients(final List<Client> clients) {
        clients.stream().forEach(client -> {
            System.out.println(format("""
                    {
                      "id": "%s",
                      "name": "%s",
                      "books": [""",
                client.getId(),
                client.getName()
            ));
            client.getBooksIds().stream().forEach((id) -> {
                System.out.println(format("     \"%s\",", id));
            });
            System.out.println("""
                  ]
                },""");
        });
    }

    @Override
    public void printListOfOperations(final List<Operation> operations) {
        operations.stream().forEach(operation -> {
            System.out.println((format("""
                    {
                      "date": "%s",
                      "time": "%s",
                      "clientId": "%s",
                      "operation": "%s",
                      "books": [""",
                operation.getDate(),
                operation.getTime(),
                operation.getClientId(),
                operation.getOperationType()
            )));
            operation.getBooksIds().stream().forEach((id) -> {
                System.out.println(format("     \"%s\",", id));
            });
            System.out.println("""
                      ]
                    },""");
        });
    }
}
