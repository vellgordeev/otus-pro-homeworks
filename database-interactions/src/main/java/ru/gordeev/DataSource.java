package ru.gordeev;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private HikariDataSource dataSource;

    private static final String JDBC_URL = "jdbc:h2:mem:testdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(JDBC_USER);
        config.setPassword(JDBC_PASSWORD);
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void disconnect() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}