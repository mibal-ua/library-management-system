package ua.mibal.minervaTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.mibal.minervaTest.dao.client.ClientRepository;
import ua.mibal.minervaTest.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@org.springframework.stereotype.Service
public class ClientService extends Service<Client> {

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
        List<Client> books = clientRepository.findAllFetchBooks();
        List<Client> result = new ArrayList<>();
        for (String arg : args) {
            List<Client> clientsToAdd = books.stream()
                    .filter(client -> client.getId().toString().equals(arg) ||
                                      client.getName().contains(arg) ||
                                      client.getBooks().stream()
                                              .anyMatch(book -> book.getTitle().contains(arg)))
                    .toList();
            result.addAll(clientsToAdd);
        }
        return result.stream()
                .distinct().toList();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public void update(Client edited) {
        clientRepository.save(edited);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Override
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Override
    public Optional<Client> findByIdFetchAll(Long id) {
        return clientRepository.findByIdFetchBooks(id);
    }

    @Override
    public List<Client> search() {
        return clientRepository.findAllFetchBooks();
    }
}
