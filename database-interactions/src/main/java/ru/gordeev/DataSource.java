package ru.gordeev;

import ru.gordeev.exceptions.ApplicationInitializationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    private Connection connection;
    private Statement statement;

    private static final String JDBC_URL = "jdbc:h2:mem:testdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    public Connection getConnection() { // можем легко подменить на коннект из пула клннектов
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            statement = connection.createStatement();
            init();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationInitializationException();
        }
    }

    public void disconnect() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // И тут ему не место, но деваться некуда
    public void init() {
        try {
            statement.executeUpdate(
                    "" +
                            "create table if not exists users (" +
                            "    id          bigserial primary key," +
                            "    login       varchar(255)," +
                            "    password    varchar(255)," +
                            "    nickname    varchar(255)" +
                            ")"
            );

            statement.executeUpdate(
                    "" +
                            "create table if not exists accounts (" +
                            "    id            bigserial primary key," +
                            "    amount        bigint," +
                            "    tp            varchar(255)," +
                            "    status        varchar(255)" +
                            ")"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationInitializationException();
        }
    }
}
