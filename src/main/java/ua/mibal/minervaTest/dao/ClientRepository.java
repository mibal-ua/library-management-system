package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.Repository;
import ua.mibal.minervaTest.frameworks.orm.component.EntityManager;
import ua.mibal.minervaTest.model.Client;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ClientRepository extends Repository<Client> {

    public ClientRepository(EntityManager entityManager) {
        super(Client.class, entityManager);
    }
}
