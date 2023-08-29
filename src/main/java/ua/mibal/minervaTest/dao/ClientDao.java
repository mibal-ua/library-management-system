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

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ClientDao extends Dao<Client> {

    public ClientDao(QueryHelper queryHelper) {
        super(queryHelper, Client.class);
    }

    public void returnBook(Client client, Book book) {
        queryHelper.performWithinTx(entityManager -> {
            Client managedClient = entityManager.merge(client);
            Book managedBook = entityManager.merge(book);
            managedClient.removeBook(managedBook);
        }, "Error while returning Books");
    }

    public void takeBook(Client client, Book book) {
        queryHelper.performWithinTx(entityManager -> {
            Client managedClient = entityManager.merge(client);
            Book managedBook = entityManager.merge(book);
            managedClient.addBook(managedBook);
        }, "Error while taking Books");
    }

    @Override
    protected boolean appropriateSelectingAddingLogic(Client client, String arg, List<Client> result) {
        if (client.getId().toString().equals(arg)) {
            result.add(client);
            return false;
        }
        if (client.getName().contains(arg)) {
            result.add(client);
        }
        return true;
    }
}
