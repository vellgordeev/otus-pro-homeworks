package ru.gordeev;

import ru.gordeev.exceptions.ApplicationInitializationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DbMigrator {

    private final DataSource dataSource;

    public DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate() {
        try (Connection connection = dataSource.getConnection();
             Statement st = connection.createStatement()) {
            String sql = readSqlFile("init.sql");
            st.execute(sql);
        } catch (SQLException | IOException e) {
            throw new ApplicationInitializationException(e.getMessage(), e);
        }
    }

    private String readSqlFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
