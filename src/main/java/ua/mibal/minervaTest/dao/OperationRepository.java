package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.Repository;
import ua.mibal.minervaTest.frameworks.orm.component.EntityManager;
import ua.mibal.minervaTest.model.Operation;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class OperationRepository extends Repository<Operation> {

    public OperationRepository(EntityManager entityManager) {
        super(Operation.class, entityManager);
    }
}
