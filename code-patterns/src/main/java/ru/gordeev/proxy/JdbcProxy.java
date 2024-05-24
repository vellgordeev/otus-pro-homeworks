package ru.gordeev.proxy;

import java.sql.SQLException;
import java.util.logging.Logger;

public class JdbcProxy implements UserService {

    private static final Logger LOGGER = Logger.getLogger(JdbcProxy.class.getName());
    private final JdbcUserService jdbcUserService;

    public JdbcProxy(JdbcUserService jdbcUserService) {
        this.jdbcUserService = jdbcUserService;
    }

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) throws SQLException {
        LOGGER.info("Calling getUsernameByLoginAndPassword with login: " + login);
        String result = jdbcUserService.getUsernameByLoginAndPassword(login, password);
        LOGGER.info("Result of getUsernameByLoginAndPassword: " + result);
        return result;
    }

    @Override
    public boolean isUserAlreadyRegistered(String login, String username) throws SQLException {
        LOGGER.info("Calling isUserAlreadyRegistered with login: " + login + ", username: " + username);
        boolean result = jdbcUserService.isUserAlreadyRegistered(login, username);
        LOGGER.info("Result of isUserAlreadyRegistered: " + result);
        return result;
    }

    @Override
    public boolean registerUser(String login, String password, String username) throws SQLException {
        LOGGER.info("Calling registerUser with login: " + login + ", password: [PROTECTED], username: " + username);
        boolean result = jdbcUserService.registerUser(login, password, username);
        LOGGER.info("Result of registerUser: " + result);
        return result;
    }

    @Override
    public UserRole getUserRole(String username) throws SQLException {
        LOGGER.info("Calling getUserRole with username: " + username);
        UserRole result = jdbcUserService.getUserRole(username);
        LOGGER.info("Result of getUserRole: " + result);
        return result;
    }

    public void close() throws SQLException {
        jdbcUserService.close();
    }
}
