package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.model.Client;

import javax.sql.DataSource;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ClientRepository extends Repository<Client> {

    public ClientRepository(DataSource dataSource) {
        super(Client.class, dataSource);
    }
}
