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

import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.model.Book;
import ua.mibal.minervaTest.model.exception.DaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class BookDao implements Dao<Book> {

    private final QueryHelper helper;

    public BookDao(QueryHelper queryHelper) {
        this.helper = queryHelper;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(helper.readWithinTx(
                em -> em.find(Book.class, id),
                "Exception while retrieving Book by id")
        );
    }

    @Override
    public List<Book> find(String[] args) {
        List<Book> result = new ArrayList<>();
        for (String arg : args) {
            for (Book book : findAll()) {
                if (book.getId().toString().equals(arg)) {
                    result.add(book);
                    break;
                }
                // TODO add search by Date
                if (book.getTitle().contains(arg) ||
                    book.getAuthor().contains(arg) ||
                    book.getPublisher().contains(arg)) {
                    result.add(book);
                }
            }
        }
        return result;
    }

    @Override
    public List<Book> findAll() {
        return helper.readWithinTx(
                em -> em.createQuery("select b from Book b", Book.class)
                        .getResultList(),
                "Exception while retrieving all Books"
        );
    }

    @Override
    public boolean update(Book updated) {
        Objects.requireNonNull(updated);
        try {
            helper.performWithinTx(em -> em.merge(updated),
                    "Exception while updating Book");
            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public boolean save(Book book) {
        Objects.requireNonNull(book);
        try {
            helper.performWithinTx(em -> em.persist(book),
                    "Exception while saving Book");
            return true;
        } catch (DaoException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Book book) {
        Objects.requireNonNull(book);
        try {
            helper.performWithinTx(em -> {
                Book managedBook = em.merge(book);
                em.remove(managedBook);
            }, "Exception while deleting Book");
            return true;
        } catch (DaoException e) {
            return false;
        }
    }
}
