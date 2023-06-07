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
                if (operation.getId().equals(arg)) {
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
        return findById(books, id);
    }

    public Optional<Client> findClientById(final String id) {
        return findById(clients, id);
    }

    public Optional<Operation> findOperationById(final String id) {
        return findById(operations, id);
    }

    private<T extends HaveId> Optional<T> findById(final List<T> list, final String id) {
        for (T el: list) {
            if (el.getId().equals(id)) {
                return Optional.of(el);
            }
        }
        return Optional.empty();
    }

    private boolean addOperation(final Operation operation) {
        return add(operations, operation);
    }

    public boolean addBook(final Book book) {
        return add(books, book);
    }

    public boolean addClient(final Client client) {
        return add(clients, client);
    }

    private<T extends HaveId> boolean add(final List<T> list, final T obj) {
        if (findById(list, obj.getId()).isPresent()) {
            return false;
        }
        list.add(requireNonNull(obj));
        return true;
    }

    public void takeBooks(final Client client, final List<String> bookIdsToTake) {
        if (bookIdsToTake.isEmpty()) {
            return;
        }

        bookIdsToTake.forEach(
            bookId -> findBookById(bookId)
                .ifPresent(book -> book.setFree(false))
        );

        client.addBooks(bookIdsToTake);

        Operation operation = new Operation(
            client.getId(),
            TAKE.toString(),
            bookIdsToTake
        );
        addOperation(operation);
    }

    public void returnBooks(final Client client, final List<String> bookIdsToReturn) {
        if (bookIdsToReturn.isEmpty()) {
            return;
        }

        bookIdsToReturn.forEach(
            bookId -> findBookById(bookId)
                .ifPresent(book -> book.setFree(true))
        );

        client.removeBooks(bookIdsToReturn);

        Operation operation = new Operation(
            client.getId(),
            RETURN.toString(),
            bookIdsToReturn
        );
        addOperation(operation);
    }

    public boolean deleteBook(final Book bookToDelete) {
        return books.remove(requireNonNull(bookToDelete));
    }

    public boolean deleteClient(final Client clientToDelete) {
        return clients.remove(requireNonNull(clientToDelete));
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

    public boolean updateBook(final Book bookToUpdate) {
        return updateById(books, bookToUpdate);
    }

    public boolean updateClient(final Client clientToUpdate) {
        return updateById(clients, clientToUpdate);
    }

    private <T extends HaveId> boolean updateById(final List<T> list, final T obj) {
        final String objId = obj.getId();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(objId)) {
                list.set(i, obj);
                return true;
            }
        }
        return false;
    }

    public interface HaveId {

        String getId();
    }
}
