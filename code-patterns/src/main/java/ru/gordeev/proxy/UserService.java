package ru.gordeev.proxy;

import java.sql.SQLException;

public interface UserService {

    String getUsernameByLoginAndPassword(String login, String password) throws SQLException;

    boolean isUserAlreadyRegistered(String login, String username) throws SQLException;

    boolean registerUser(String login, String password, String username) throws SQLException;

    UserRole getUserRole(String username) throws SQLException;
}
