package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class ResultInterpreter {

    public<T> Optional<T> interpret(ResultSet resultSet, EntityMetadata metadata) {
        return Optional.empty();
    }

    public<T> List<T> interpretList(ResultSet resultSet, EntityMetadata metadata) {
        return null;
    }
}
