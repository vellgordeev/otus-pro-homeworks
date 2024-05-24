package ru.gordeev.proxy;

import java.sql.*;
import java.util.List;

public class JdbcUserService implements UserService {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    private final Connection connection;

    public JdbcUserService() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        this.connection.setAutoCommit(false);
        createTable();
        insertInitialData();
    }

    private void createTable() throws SQLException {
        String sql = """
                CREATE TABLE users (
                    login VARCHAR(255) PRIMARY KEY,
                    password VARCHAR(255) NOT NULL,
                    username VARCHAR(255) NOT NULL,
                    userRole VARCHAR(50) NOT NULL
                );
                """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void insertInitialData() throws SQLException {
        List<User> initialUsers = List.of(
                new User("admin", "admin", "Administrator", UserRole.ADMIN),
                new User("login1", "pass1", "John", UserRole.USER),
                new User("login2", "pass2", "Vell", UserRole.USER),
                new User("login3", "pass3", "Max", UserRole.USER)
        );

        String sql = "INSERT INTO users (login, password, username, userRole) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (User user : initialUsers) {
                pstmt.setString(1, user.login());
                pstmt.setString(2, user.password());
                pstmt.setString(3, user.username());
                pstmt.setString(4, user.userRole().name());
                pstmt.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) throws SQLException {
        String sql = "SELECT username FROM users WHERE login = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        }
        return null;
    }

    @Override
    public boolean registerUser(String login, String password, String username) throws SQLException {
        if (!isUserAlreadyRegistered(login, username)) {
            String sql = "INSERT INTO users (login, password, username, userRole) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, login);
                pstmt.setString(2, password);
                pstmt.setString(3, username);
                pstmt.setString(4, UserRole.USER.name());
                pstmt.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
        return false;
    }

    @Override
    public UserRole getUserRole(String username) throws SQLException {
        String sql = "SELECT userRole FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return UserRole.valueOf(rs.getString("userRole"));
            }
        }
        return null;
    }

    @Override
    public boolean isUserAlreadyRegistered(String login, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ? OR username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        }
        return false;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
