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
import ua.mibal.minervaTest.model.Client;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ClientDao implements Dao<Client> {

    @Override
    public Optional<Client> findById(String id) {
        return null;
    }

    @Override
    public List<Client> find(String[] args) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return null;
    }

    @Override
    public void update(Client e) {

    }

    @Override
    public boolean save(Client e) {
        return false;
    }

    @Override
    public boolean delete(Client e) {
        return false;
    }

    public void returnBooks(Client client, List<String> booksToReturn) {

    }

    public void takeBooks(Client client, List<String> booksToTake) {

    }

    public Optional<Client> findClientByBookId(String bookId) {
        return null;
    }

    public boolean isContainClientId(String id) {
        return false;
    }
}