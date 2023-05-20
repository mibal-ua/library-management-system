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

import static java.util.Collections.unmodifiableList;
import static java.util.List.of;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Library implements Serializable {

    private List<Book> books;

    private List<Client> clients;

    private List<Operation> operations;

    public Library(final List<Book> books, final List<Client> clients, final List<Operation> operations) {
        this.books = books;
        this.clients = clients;
        this.operations = operations;
    }

    public Library() {
    }

    public List<Book> getBooks() {
        return unmodifiableList(books);
    }

    public List<Client> getClients() {
        return unmodifiableList(clients);
    }

    public List<Operation> getOperations() {
        return unmodifiableList(operations);
    }

    public List<Book> findBooks(final String input) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getId().equals(input)) {
                return of(book);
            }
            if (book.getTitle().contains(input) ||
                book.getAuthor().contains(input) ||
                book.getPublisher().contains(input)) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Client> findClients(final String input) {
        List<Client> result = new ArrayList<>();
        for (Client client : clients) {
            if (client.getId().equals(input)) {
                return of(client);
            }
            if (client.getName().contains(input)) {
                result.add(client);
            }
        }
        return result;
    }

    public Optional<Client> findClientByBookId(final String bookId) {
        for (Client client : clients) {
            if (client.getBooksIds().contains(bookId)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    public Optional<Book> findBookById(final String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }

    public Optional<Client> findClientById(final String id) {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    public boolean addOperation(final Operation operation) {
        // TODO implement validation
        operations.add(operation);
        return true;
    }

    public boolean addBook(final Book book) {
        // TODO implement validation
        books.add(book);
        return true;
    }

    public boolean addClient(final Client client) {
        // TODO implement validation
        clients.add(client);
        return true;
    }
}
