package ru.gordeev.proxy;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            JdbcProxy userService = new JdbcProxy(new JdbcUserService());

            userService.getUsernameByLoginAndPassword("admin", "admin");
            userService.registerUser("newLogin", "newPass", "New User");
            userService.getUsernameByLoginAndPassword("newLogin", "newPass");

            userService.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
