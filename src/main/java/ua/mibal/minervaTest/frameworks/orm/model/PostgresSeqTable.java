package ua.mibal.minervaTest.frameworks.orm.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class PostgresSeqTable extends SeqTable {

    private final static String SELECT_NEXTVAL_FORMATTED_SQL = "select %s('%s')";

    protected PostgresSeqTable() {
        super("%s_seq", "nextval");
    }

    @Override
    public long getId(EntityMetadata entityMetadata, Connection conn) throws SQLException {
        String sql = getSelectValSql(entityMetadata);
        try (
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            resultSet.next();
            return resultSet.getLong(nextValColumn);
        }
    }

    private String getSelectValSql(EntityMetadata entityMetadata) {
        return SELECT_NEXTVAL_FORMATTED_SQL
                .formatted(nextValColumn, getSeqTable(entityMetadata.getTable()));
    }
}
