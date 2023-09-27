package ua.mibal.minervaTest.dao.operation;

import ua.mibal.minervaTest.model.Operation;

import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface CustomOperationRepository {

    List<Operation> findFetchBookClient(String... args);
}
