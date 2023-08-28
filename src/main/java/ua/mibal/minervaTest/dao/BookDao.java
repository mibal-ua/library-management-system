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
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Operation;

import java.util.List;
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
        return null;
    }

    @Override
    public List<Book> find(String[] args) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public boolean update(Book updated) {
        return false;

    }

    @Override
    public boolean save(Book e) {
        return false;
    }

    @Override
    public boolean delete(Book e) {
        return false;
    }

    public List<Book> getBooksClientHolds(Client client) {
        return null;
    }

    public List<Book> getBooksInOperation(Operation operation) {
        return null;
    }

    public boolean isContainBookId(String id) {
        return false;
    }
}
