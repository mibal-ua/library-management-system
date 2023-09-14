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

package ua.mibal.minervaTest.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.mibal.minervaTest.dao.Dao;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.OperationType;

import java.time.LocalDateTime;
import java.util.function.BiConsumer;

import static ua.mibal.minervaTest.model.OperationType.RETURN;
import static ua.mibal.minervaTest.model.OperationType.TAKE;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Service
@Transactional
public class LibraryService {

    private final Dao<Book> bookDao;
    private final Dao<Operation> operationDao;
    private final Dao<Client> clientDao;

    public LibraryService(Dao<Book> bookDao,
                          Dao<Operation> operationDao,
                          Dao<Client> clientDao) {
        this.bookDao = bookDao;
        this.operationDao = operationDao;
        this.clientDao = clientDao;
    }

    public void takeBook(Client client, Book book) {
        operationWrapper((cl, b) -> b.setClient(cl), client, book, TAKE);
    }

    public void returnBook(Client client, Book book) {
        operationWrapper((ign, b) -> b.setClient(null), client, book, RETURN);
    }

    private void operationWrapper(BiConsumer<Client, Book> clientBookConsumer,
                                  Client client,
                                  Book book,
                                  OperationType operationType) {
        Book managedBook = bookDao.getReference(book.getId());
        Client managedClient = clientDao.getReference(client.getId());
        clientBookConsumer.accept(managedClient, managedBook);
        operationDao.save(new Operation(
                LocalDateTime.now(),
                managedClient,
                operationType,
                managedBook
        ));
    }
}
