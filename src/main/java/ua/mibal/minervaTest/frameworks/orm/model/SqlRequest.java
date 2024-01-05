package ua.mibal.minervaTest.frameworks.orm.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class SqlRequest<T> {

    private final String sql;
    private final List<Function<T, Object>> sqlInsertByIndexGeneratorList;

    public SqlRequest(String sql,
                      List<Function<T, Object>> sqlInsertByIndexGeneratorList) {
        this.sql = sql;
        this.sqlInsertByIndexGeneratorList = sqlInsertByIndexGeneratorList;
    }

    public String getSql() {
        return sql;
    }

    public void insertValues(PreparedStatement preparedStatement, T entity) throws SQLException {
        for (int i = 0; i < sqlInsertByIndexGeneratorList.size(); i++) {
            Function<T, Object> insertValueProvider = sqlInsertByIndexGeneratorList.get(i);
            preparedStatement.setObject(i, insertValueProvider.apply(entity));
        }
    }

    public void insertValues(PreparedStatement preparedStatement, Long id) throws SQLException {
        preparedStatement.setObject(1, id);
    }

    public List<Function<T, Object>> getSqlInsertByIndexGeneratorList() {
        return sqlInsertByIndexGeneratorList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SqlRequest{");
        sb.append("sql='").append(sql).append('\'');
        sb.append(", sqlInsertByIndexGeneratorList=").append(sqlInsertByIndexGeneratorList);
        sb.append('}');
        return sb.toString();
    }
}
