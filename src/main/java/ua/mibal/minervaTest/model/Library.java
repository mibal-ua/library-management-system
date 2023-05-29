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
import static java.util.Objects.requireNonNull;
import static ua.mibal.minervaTest.model.OperationType.RETURN;
import static ua.mibal.minervaTest.model.OperationType.TAKE;
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

    public List<Book> findBooks(final String[] input) {
        List<Book> result = new ArrayList<>();
        for (final String arg : input) {
            for (Book book : books) {
                if (book.getId().equals(arg)) {
                    result.add(book);
                    break;
                }
                if (book.getTitle().contains(arg) ||
                    book.getAuthor().contains(arg) ||
                    book.getPublisher().contains(arg)) {
                    result.add(book);
                }
            }
        }
        return unmodifiableList(result);
    }

    public List<Client> findClients(final String[] input) {
        List<Client> result = new ArrayList<>();
        for (final String arg : input) {
            for (Client client : clients) {
                if (client.getId().equals(arg)) {
                    result.add(client);
                    break;
                }
                if (client.getName().contains(arg) ||
                    client.getBooksIds().contains(arg)) {
                    result.add(client);
                }
            }
        }
        return unmodifiableList(result);
    }

    public List<Operation> findOperations(final String[] input) {
        List<Operation> result = new ArrayList<>();
        for (final String arg : input) {
            for (Operation operation : operations) {
                if (operation.getId().equals(arg)){
                    result.add(operation);
                    break;
                }
                if (operation.getClientId().equals(arg) ||
                    operation.getDate().contains(arg) ||
                    operation.getOperationType().equalsIgnoreCase(arg) ||
                    operation.getBooksIds().contains(arg)) {
                    result.add(operation);
                }
                // find by username
                String id = operation.getClientId();
                findClientById(id).ifPresent(client -> {
                    if (client.getName().contains(arg)) {
                        result.add(operation);
                    }
                });
            }
        }
        return unmodifiableList(result);
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

    public Optional<Operation> findOperationById(final String id) {
        for (Operation operation : operations) {
            if (operation.getId().equals(id)) {
                return Optional.of(operation);
            }
        }
        return Optional.empty();
    }

    private boolean addOperation(final Operation operation) {
        if (findOperationById(operation.getId()).isPresent()) {
            return false;
        }
        operations.add(requireNonNull(operation));
        return true;
    }

    public boolean addBook(final Book book) {
        if (findBookById(book.getId()).isPresent()) {
            return false;
        }
        books.add(requireNonNull(book));
        return true;
    }

    public boolean addClient(final Client client) {
        if (findClientById(client.getId()).isPresent()) {
            return false;
        }
        clients.add(requireNonNull(client));
        return true;
    }

    public void takeBooks(final Client client, final List<String> bookIds,
                          final Lambda ifNotFree, final Lambda ifNotExists) {
        List<String> filteredList = bookIds.stream().filter((bookId) -> {
            Optional<Book> optionalBook = findBookById(bookId);
            if (optionalBook.isEmpty()) {
                ifNotExists.apply(bookId);
                return false;
            }
            Book book = optionalBook.get();
            if (!book.isFree()) {
                ifNotFree.apply(bookId);
                return false;
            }
            book.setFree(false);
            return true;
        }).toList();
        if (filteredList.size() == 0) {
            return;
        }
        Operation operation = new Operation(
            client.getId(),
            TAKE.toString(),
            filteredList
        );
        addOperation(operation);
    }

    public void returnBooks(final Client client, final List<String> bookIds,
                            final Lambda ifFree, final Lambda ifNotExists,
                            final UserDontHaveBookLambda ifUserDontHaveBook) {
        List<String> filteredList = bookIds.stream().filter((bookId) -> {
            Optional<Book> optionalBook = findBookById(bookId);
            if (optionalBook.isEmpty()) {
                ifNotExists.apply(bookId);
                return false;
            }
            Book book = optionalBook.get();
            if (book.isFree()) {
                ifFree.apply(bookId);
                return false;
            }
            if (!client.getBooksIds().contains(bookId)) {
                ifUserDontHaveBook.apply(client, book);
                return false;
            }
            book.setFree(true);
            return true;
        }).toList();
        if (filteredList.size() == 0) {
            return;
        }
        Operation operation = new Operation(
            client.getId(),
            RETURN.toString(),
            filteredList
        );
        addOperation(operation);
    }

    public void deleteBook(final Book bookToDelete) {
        // TODO
        books.remove(requireNonNull(bookToDelete));
    }

    public void deleteClient(final Client clientToDelete) {
        // TODO
        clients.remove(requireNonNull(clientToDelete));
    }

    public boolean isContainClientId(final String id) {
        return findClientById(id).isPresent();
    }

    public boolean doesClientHoldBook(final Client client) {
        return client.getBooksIds().size() != 0;
    }

    public boolean isContainBookId(final String id) {
        return findBookById(id).isPresent();
    }

    public List<Book> getBooksClientHolds(final Client client) {
        final List<Book> result = new ArrayList<>();
        for (final String bookId : client.getBooksIds()) {
            findBookById(bookId).ifPresent(result::add);
        }
        return result;
    }

    public List<Book> getBooksInOperation(final Operation operation) {
        final List<Book> result = new ArrayList<>();
        for (final String bookId : operation.getBooksIds()) {
            findBookById(bookId).ifPresent(result::add);
        }
        return result;
    }

    @FunctionalInterface
    public interface Lambda {

        void apply(String id);
    }

    @FunctionalInterface
    public interface UserDontHaveBookLambda {

        void apply(Client client, Book book);
    }
}
