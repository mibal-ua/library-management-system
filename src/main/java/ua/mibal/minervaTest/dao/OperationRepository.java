package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.model.Operation;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class OperationRepository extends Repository<Operation> {

    protected OperationRepository() {
        super(Operation.class);
    }
}
