package ua.mibal.minervaTest.frameworks.orm.model;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public abstract class SeqTable {

    protected final String tableFormat;
    protected final String nextValColumn;

    protected SeqTable(String tableFormat, String nextValColumn) {
        this.tableFormat = tableFormat;
        this.nextValColumn = nextValColumn;
    }

    protected String getSeqTable(String entityTable) {
        return tableFormat.formatted(entityTable);
    }

    public abstract long getId(EntityMetadata entityClass, Connection connection) throws SQLException;

    public enum SQLType {

        MySql("com.mysql.cj.jdbc.MysqlDataSource", new MySqlSeqTable()),
        Postgres("org.postgresql.ds.PGSimpleDataSource", new PostgresSeqTable());

        private final String driverFullName;
        private final SeqTable seqTable;

        SQLType(String driverFullName, SeqTable seqTable) {
            this.driverFullName = driverFullName;
            this.seqTable = seqTable;
        }

        public SeqTable getSeqTable() {
            return seqTable;
        }

        public String getDriverFullName() {
            return driverFullName;
        }
    }
}
