package ua.mibal.minervaTest.dao.client;

import ua.mibal.minervaTest.model.Client;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface CustomClientRepository {

    List<Client> findFetchBooks(String... args);
}
