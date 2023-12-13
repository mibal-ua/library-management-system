package ua.mibal.minervaTest.dao.client;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.model.Client;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class ClientRepository implements CustomClientRepository {

    //    @Transactional(readOnly = true)
//    @Query("select c from Client c left join fetch c.books where c.id = :id")
    public Optional<Client> findByIdFetchBooks(Long id) {
        return Optional.empty();
    }

    //    @Transactional(readOnly = true)
//    @Query("select c from Client c left join fetch c.books")
    public List<Client> findAllFetchBooks() {
        return List.of();
    }

    @Override
    public Client getReference(Long id) {
        return null;
    }

    public Optional<Client> findById(Long id) {
        return null;
    }

    public void save(Client edited) {

    }

    public void delete(Client client) {

    }
}
