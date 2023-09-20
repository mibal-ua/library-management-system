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
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.exception.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class OperationDao extends Dao<Operation> {

    public OperationDao(QueryHelper queryHelper) {
        super(queryHelper, Operation.class);
    }

    public List<Operation> findAllFetchBookClient() throws DaoException {
        return queryHelper.readWithinTx(
                entityManager -> entityManager.createQuery("select o from Operation o " +
                                                           "left join fetch o.book " +
                                                           "left join fetch o.client", Operation.class)
                        .getResultList(),
                "Exception while retrieving all Operations"
        );
    }

    public Optional<Operation> findByIdFetchBookClient(Long id) throws DaoException {
        return Optional.ofNullable(queryHelper.readWithinTx(
                entityManager -> entityManager.createQuery("select o from Operation o " +
                                                           "left join fetch o.book " +
                                                           "left join fetch o.client " +
                                                           "where o.id = :id", Operation.class)
                        .setParameter("id", id)
                        .getSingleResult(),
                "Exception while retrieving Operation"
        ));
    }

    public List<Operation> findFetchBookClient(String... args) {
        return findAllFetchBookClient().stream()
                .filter(operation -> {
                    for (String arg : args) {
                        if (operation.getId().toString().equals(arg) ||
                            operation.getClient().getId().toString().equals(arg) ||
                            operation.getDate().toString().contains(arg) ||
                            operation.getOperationType().toString().equalsIgnoreCase(arg) ||
                            operation.getClient().getName().contains(arg))
                            return true;
                    }
                    return false;
                })
                .toList();
    }
}
