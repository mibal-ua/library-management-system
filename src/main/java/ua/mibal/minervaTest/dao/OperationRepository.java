package ua.mibal.minervaTest.dao;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.Repository;
import ua.mibal.minervaTest.model.Operation;

import javax.sql.DataSource;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Component
public class OperationRepository extends Repository<Operation> {

    public OperationRepository(DataSource dataSource) {
        super(Operation.class, dataSource);
    }
}
