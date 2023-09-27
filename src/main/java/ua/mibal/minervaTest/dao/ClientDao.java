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
public class ClientDao extends Dao<Client> {

    public ClientDao() {
        super(Client.class);
    }

    @Transactional(readOnly = true)
    public List<Client> findAllFetchBooks() throws DaoException {
        return entityManager.createQuery("select c from Client c " +
                                         "left join fetch c.books", Client.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public Optional<Client> findByIdFetchBooks(Long id) throws DaoException {
        return Optional.ofNullable(entityManager.createQuery("select c from Client c " +
                                                             "left join fetch c.books " +
                                                             "where c.id = :id", Client.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    @Transactional(readOnly = true)
    public List<Client> findFetchBooks(String... args) {
        return findAllFetchBooks().stream()
                .filter(client -> {
                    for (String arg : args) {
                        if (client.getId().toString().equals(arg) ||
                            client.getName().contains(arg))
                            return true;
                    }
                    return false;
                })
                .toList();
    }
}
