package ua.mibal.minervaTest.dao.operation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Operation;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Repository
@Transactional
public class CustomOperationRepositoryImpl implements CustomOperationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<Operation> findFetchBookClient(String... args) {
        return entityManager.createQuery("select o from Operation o " +
                                         "left join fetch o.book " +
                                         "left join fetch o.client", Operation.class)
                .getResultStream()
                .filter(operation -> {
                    for (String arg : args) {
                        if (operation.getId().toString().equals(arg) ||
                            operation.getClient().getId().toString().equals(arg) ||
                            operation.getDate().toString().contains(arg) ||
                            operation.getOperationType().toString().equalsIgnoreCase(arg) ||
                            operation.getClient().getName().contains(arg))
                            return true;
                    }
                    return false;
                })
                .toList();
    }
}
