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

import ua.mibal.minervaTest.model.exception.DaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public abstract class Dao<T> {

    protected final QueryHelper queryHelper;
    private final Class<T> type;
    private final String entityName;

    protected Dao(QueryHelper queryHelper, Class<T> type) {
        this.queryHelper = queryHelper;
        this.type = type;
        this.entityName = type.getSimpleName();
    }

    public final Optional<T> findById(Long id) {
        return Optional.ofNullable(queryHelper.readWithinTx(
                em -> em.find(type, id),
                "Exception while retrieving " + entityName + " by id"
        ));
    }

    public final List<T> find(String[] args) {
        List<T> result = new ArrayList<>();
        for (String arg : args) {
            for (T e : findAll()) {
                if (!appropriateSelectingAddingLogic(e, arg, result)) {
                    break;
                }
            }
        }
        return result;
    }

    // returns true if continue, false if break
    protected abstract boolean appropriateSelectingAddingLogic(T e, String arg, List<T> result);

    public final List<T> findAll() {
        return queryHelper.readWithinTx(
                entityManager -> entityManager.createQuery("select e from " + entityName + " e", type)
                        .getResultList(),
                "Exception while retrieving all " + entityName + "s"
        );
    }

    public final boolean update(T e) {
        Objects.requireNonNull(e);
        try {
            queryHelper.performWithinTx(entityManager -> entityManager.merge(e),
                    "Exception while updating " + entityName);
            return true;
        } catch (DaoException ex) {
            return false;
        }
    }

    public final boolean save(T e) {
        Objects.requireNonNull(e);
        try {
            queryHelper.performWithinTx(entityManager -> entityManager.persist(e),
                    "Exception while saving " + entityName);
            return true;
        } catch (DaoException ex) {
            return false;
        }
    }

    public final boolean delete(T e) {
        Objects.requireNonNull(e);
        try {
            queryHelper.performWithinTx(entityManager -> {
                T managedEntity = entityManager.merge(e);
                entityManager.remove(managedEntity);
            }, "Exception while deleting " + entityName);
            return true;
        } catch (DaoException ex) {
            return false;
        }
    }
}
