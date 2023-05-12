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

package ua.mibal.minervaTest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Library {

    private List<Book> books;

    private List<Client> clients;

    private List<Operation> operations;

    public Library(final List<Book> books, final List<Client> clients, final List<Operation> operations) {
        this.books = books;
        this.clients = clients;
        this.operations = operations;
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public List<Book> findBooks(final String input) {
        List<Book> result = new ArrayList<>();
        books.forEach((book) -> {
            if (book.getId().contains(input) ||
                book.getTitle().contains(input) ||
                book.getSubtitle().contains(input) ||
                book.getAuthor().contains(input) ||
                book.getPublishedDate().contains(input) ||
                book.getPublisher().contains(input) ||
                book.getDescription().contains(input) ||
                book.getWebsite().contains(input)) {
                result.add(book);
            }
        });
        return result;
    }

    public List<Client> findClients(final String input) {
        List<Client> result = new ArrayList<>();
        clients.forEach((client) -> {
            if (client.getId().contains(input) ||
                client.getName().contains(input)) {
                result.add(client);
            }
        });
        return result;
    }

    public Book findBookById(final String id) {
        final AtomicReference<Book> result = new AtomicReference<>();
        books.forEach((book) -> {
            if (book.getId().equals(id)) {
                result.set(book);
            }
        });
        return result.get();
    }
}
