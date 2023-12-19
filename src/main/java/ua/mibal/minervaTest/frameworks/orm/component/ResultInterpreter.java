package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class ResultInterpreter<T> {

    private final EntityMetadata metadata;

    public ResultInterpreter(EntityMetadata metadata, Class<T> entityClass) {
        this.metadata = metadata;
    }

    public Optional<T> interpret(ResultSet resultSet) {
        return Optional.empty();
    }

    public List<T> interpretList(ResultSet resultSet) {
        return null;
    }
}
