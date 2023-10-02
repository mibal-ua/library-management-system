package ua.mibal.minervaTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import ua.mibal.minervaTest.dao.operation.OperationRepository;
import ua.mibal.minervaTest.model.Operation;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class OperationService extends Service<Operation> {

    // TODO

    private final static Service<Operation> instance = new OperationService();
    @Autowired
    private OperationRepository operationRepository;

    private OperationService() {
    }

    public static Service<Operation> getInstance() {
        return instance;
    }

    @Override
    public List<Operation> searchBy(String[] args) {
        return null;
    }

    @Override
    public Optional<Operation> details(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Operation> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Operation edited) {

    }

    @Override
    public void save(Operation operation) {

    }

    @Override
    public void delete(Operation operation) {

    }

    @Override
    public Optional<Operation> findByIdFetchAll(Long id) {
        return Optional.empty();
    }
}
