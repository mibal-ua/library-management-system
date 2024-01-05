package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.SeqTable;
import ua.mibal.minervaTest.frameworks.orm.model.SeqTable.SQLType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Component used to provide Generated Id for annotated Entities
 *
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class IdProvider {

    private final DataSource dataSource;
    private final SeqTable seqTable;

    public IdProvider(DataSource dataSource) {
        this.dataSource = dataSource;
        SQLType sqlType = defineSqlType();
        this.seqTable = sqlType.getSeqTable();
    }

    private SQLType defineSqlType() {
        return Arrays.stream(SQLType.values())
                .filter(type -> existsDriverInClassPath(type.getDriverFullName()))
                .findFirst()
                .orElseThrow();
    }

    private boolean existsDriverInClassPath(String driverFullName) {
        try {
            Class.forName(driverFullName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public Object getId(EntityMetadata entityMetadata) {
        try (Connection connection = dataSource.getConnection()) {
            return seqTable.getId(entityMetadata, connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
