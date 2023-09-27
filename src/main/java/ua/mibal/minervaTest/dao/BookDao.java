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

package ua.mibal.minervaTest.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Repository
@Transactional
public class BookDao extends Dao<Book> {

    public BookDao() {
        super(Book.class);
    }

    public void takeBook(Long clientId, Long bookId) {
        Book managedBook = entityManager.getReference(Book.class, bookId);
        Client managedClient = entityManager.getReference(Client.class, clientId);
        managedBook.setClient(managedClient);
    }

    public void returnBook(Long clientId, Long bookId) {
        Book managedBook = entityManager.getReference(Book.class, bookId);
        managedBook.setClient(null);
    }

    @Transactional(readOnly = true)
    public List<Book> find(String... args) {
        return findAll().stream()
                .filter(book -> {
                    for (String arg : args) {
                        if (book.getId().toString().equals(arg) ||
                            book.getTitle().contains(arg) || // TODO add search by Date
                            book.getAuthor().contains(arg) ||
                            book.getPublisher().contains(arg))
                            return true;
                    }
                    return false;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Book> findByIdFetchClient(Long id) throws DaoException {
        return Optional.ofNullable(entityManager.createQuery("select b from Book b " +
                                                             "left join fetch b.client " +
                                                             "where b.id = :id", Book.class)
                .setParameter("id", id)
                .getSingleResult());
    }
}
