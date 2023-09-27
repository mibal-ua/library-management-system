package ua.mibal.minervaTest.dao.client;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Client;

import java.util.List;

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
    public List<Client> findFetchBooks(String... args) {
        return entityManager.createQuery("select c from Client c left join c.books", Client.class)
                .getResultStream()
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
