package ua.mibal.minervaTest.dao.client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Client;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Repository
@Transactional
public class CustomClientRepositoryImpl implements CustomClientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public Client getReference(Long id) {
        return entityManager.getReference(Client.class, id);
    }
}
