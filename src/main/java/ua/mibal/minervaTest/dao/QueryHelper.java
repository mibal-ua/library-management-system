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
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import ua.mibal.minervaTest.model.exception.DaoException;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class QueryHelper {

    private final EntityManagerFactory entityManagerFactory;

    public QueryHelper(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> T readWithinTx(Function<EntityManager, T> fn, String exceptionMessage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Session session = em.unwrap(Session.class);
        session.setDefaultReadOnly(true);
        try {
            T result = fn.apply(em);
            em.getTransaction().commit();
            return result;
        } catch (Throwable cause) {
            em.getTransaction().rollback();
            throw new DaoException(cause, exceptionMessage);
        } finally {
            em.close();
        }
    }

    public void performWithinTx(Consumer<EntityManager> fn, String exceptionMessage) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        try {
            fn.accept(em);
            em.getTransaction().commit();
        } catch (Throwable cause) {
            em.getTransaction().rollback();
            throw new DaoException(cause, exceptionMessage);
        } finally {
            em.close();
        }
    }
}
