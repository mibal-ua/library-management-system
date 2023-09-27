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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.exception.DaoException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Transactional
public abstract class Dao<T> {

    private final Class<T> type;
    private final String entityName;

    @PersistenceContext
    protected EntityManager entityManager;

    protected Dao(Class<T> type) {
        this.type = type;
        this.entityName = type.getSimpleName();
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) throws DaoException {
        return Optional.ofNullable(entityManager.find(type, id));
    }

    @Transactional(readOnly = true)
    public List<T> findAll() throws DaoException {
        return entityManager.createQuery("select e from " + entityName + " e", type)
                .getResultList();
    }

    public boolean update(T e) {
        entityManager.merge(Objects.requireNonNull(e));
        return true;
    }

    public boolean save(T e) {
        entityManager.persist(Objects.requireNonNull(e));
        return true;
    }

    public boolean delete(T e) {
        T managedEntity = entityManager.merge(Objects.requireNonNull(e));
        entityManager.remove(managedEntity);
        return true;
    }
}
