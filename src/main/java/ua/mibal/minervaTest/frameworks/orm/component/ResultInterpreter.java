package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.model.Entity;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class ResultInterpreter<T extends Entity> {

    // TODO

    private final Class<T> clazz;

    public ResultInterpreter(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Optional<T> interpret(ResultSet resultSet) {
        return null;
    }

    public List<T> interpretList(ResultSet resultSet) {
        return null;
    }
}
