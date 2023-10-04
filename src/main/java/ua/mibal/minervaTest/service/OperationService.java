package ua.mibal.minervaTest.service;

import ua.mibal.minervaTest.dao.operation.OperationRepository;
import ua.mibal.minervaTest.model.Operation;
import ua.mibal.minervaTest.model.exception.IllegalRepositoryAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@org.springframework.stereotype.Service
public class OperationService implements Service<Operation> {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public List<Operation> searchBy(String[] args) {
        List<Operation> operations = operationRepository.findAllFetchBookClient();
        List<Operation> result = new ArrayList<>();
        for (String arg : args) {
            List<Operation> operationsToAdd = operations.stream()
                    .filter(operation -> operation.getId().toString().equals(arg) ||
                                         operation.getClient().getId().toString().equals(arg) ||
                                         operation.getDate().toString().contains(arg) ||
                                         operation.getOperationType().toString().equalsIgnoreCase(arg) ||
                                         operation.getClient().getName().contains(arg))
                    .toList();
            result.addAll(operationsToAdd);
        }
        return result.stream()
                .distinct().toList();
    }

    @Override
    public Optional<Operation> findById(Long id) {
        return operationRepository.findById(id);
    }

    @Override
    public void update(Operation ignored) {
        throwAccessException();
    }

    @Override
    public void save(Operation ignored) {
        throwAccessException();
    }

    @Override
    public void delete(Operation ignored) {
        throwAccessException();
    }

    @Override
    public Optional<Operation> findByIdLoadAll(Long id) {
        return operationRepository.findByIdFetchBookClient(id);
    }

    @Override
    public List<Operation> search() {
        return operationRepository.findAllFetchBookClient();
    }

    @Override
    public Optional<Operation> findByIdForDeleteChecking(Long id) {
        throwAccessException();
        return Optional.empty();
    }

    private void throwAccessException() throws IllegalRepositoryAccessException {
        throw new IllegalRepositoryAccessException("You are trying to change history manually. You have no access for this action");
    }
}
