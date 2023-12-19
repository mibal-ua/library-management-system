package ua.mibal.minervaTest.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import ua.mibal.minervaTest.frameworks.context.annotations.Configuration;

import javax.sql.DataSource;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Configuration
public class DbConfig {

    public DataSource dataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/book_library");
        dataSource.setUser("root");
        dataSource.setPassword("password");
        return dataSource;
    }

    public String entityPackage() {
        return "ua.mibal.minervaTest.model";
    }
}
