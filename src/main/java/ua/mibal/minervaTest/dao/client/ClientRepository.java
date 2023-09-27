package ua.mibal.minervaTest.dao.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Client;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface ClientRepository extends JpaRepository<Client, Long>, CustomClientRepository {

    @Transactional(readOnly = true)
    @Query("select c from Client c left join fetch c.books where c.id = :id")
    Optional<Client> findByIdFetchBooks(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Query("select c from Client c left join fetch c.books")
    List<Client> findAllFetchBooks();
}
