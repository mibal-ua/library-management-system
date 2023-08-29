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
public class ClientDao extends Dao<Client> {

    public ClientDao(QueryHelper queryHelper) {
        super(queryHelper, Client.class);
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

    @Override
    protected boolean appropriateSelectingAddingLogic(Client e, String arg, List<Client> result) {
        // TODO
        return false;
    }
}
