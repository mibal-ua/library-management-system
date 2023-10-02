package ua.mibal.minervaTest.dao.operation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Operation;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Transactional(readOnly = true)
    @Query("select o from Operation o left join fetch o.book left join fetch o.client")
    List<Operation> findAllFetchBookClient();

    @Transactional(readOnly = true)
    @Query("select o from Operation o left join fetch o.book left join fetch o.client where o.id = :id")
    Optional<Operation> findByIdFetchBookClient(@Param("id") Long id);
}
