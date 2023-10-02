package ua.mibal.minervaTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.mibal.minervaTest.dao.client.ClientRepository;
import ua.mibal.minervaTest.model.Client;
import ua.mibal.minervaTest.model.Entity;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@org.springframework.stereotype.Service
public class ClientService extends Service<Client> {

    // TODO

    private final static Service<Client> instance = new ClientService();
    @Autowired
    private ClientRepository clientRepository;

    private ClientService() {
    }

    public static Service<Client> getInstance() {
        return instance;
    }

    @Override
    public List<Client> searchBy(String[] args) {
        return null;
    }

    @Override
    public Optional<Client> details(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Entity editedEntity) {

    }

    @Override
    public void save(Entity e) {

    }

    @Override
    public void delete(Entity entityToDel) {

    }

    @Override
    public Optional<Client> findByIdFetchAll(Long id) {
        return Optional.empty();
    }
}
