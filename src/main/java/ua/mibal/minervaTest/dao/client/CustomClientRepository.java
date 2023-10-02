package ua.mibal.minervaTest.dao.client;

import ua.mibal.minervaTest.model.Client;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface CustomClientRepository {

    Client getReference(Long id);
}
