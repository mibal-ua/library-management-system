package ua.mibal.minervaTest.frameworks.orm.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class MySqlSeqTable extends SeqTable {

    private final static String SELECT_NEXT_VAL_FORMATTED_SQL = "select %s from %s";
    private final static String UPDATE_NEXT_VAL_FORMATTED_SQL = "update %s set %s = ? where true";

    public MySqlSeqTable() {
        super("%s_SEQ", "next_val");
    }

    @Override
    public long getId(EntityMetadata entityMetadata, Connection conn) throws SQLException {
        long id = getIdFromTable(entityMetadata, conn);
        updateIdTable(id, entityMetadata, conn);
        return id;
    }

    private void updateIdTable(long prevId, EntityMetadata entityMetadata, Connection conn) throws SQLException {
        String updateSql = getUpdateValSql(entityMetadata);
        try (PreparedStatement preparedStatement = conn.prepareStatement(updateSql)) {
            preparedStatement.setLong(1, prevId + 1);
            preparedStatement.executeUpdate();
        }
    }

    private long getIdFromTable(EntityMetadata entityMetadata, Connection conn) throws SQLException {
        String selectSql = getSelectValSql(entityMetadata);
        try (
                PreparedStatement preparedStatement = conn.prepareStatement(selectSql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            resultSet.next();
            return resultSet.getLong(nextValColumn);
        }
    }

    private String getSelectValSql(EntityMetadata entityMetadata) {
        return SELECT_NEXT_VAL_FORMATTED_SQL
                .formatted(nextValColumn, getSeqTable(entityMetadata.getTable()));
    }

    private String getUpdateValSql(EntityMetadata entityMetadata) {
        return UPDATE_NEXT_VAL_FORMATTED_SQL
                .formatted(getSeqTable(entityMetadata.getTable()), nextValColumn);
    }
}
