package ua.mibal.minervaTest.dao.operation;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.model.Operation;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class OperationRepository {

    public List<Operation> findAllFetchBookClient() {
        return List.of();
    }

    public Optional<Operation> findByIdFetchBookClient(Long id) {
        return Optional.empty();
    }

    public void save(Operation operation) {

    }

    public Optional<Operation> findById(Long id) {
        return null;
    }
}
