package ru.gordeev;

import lombok.Getter;
import ru.gordeev.exceptions.RepositoryException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class AbstractRepository<T> {

    private final Class<T> cls;
    private final ClassEntities classEntities;
    private final DataSource dataSource;
    private final String tableName;
    private String preparedUpdateAllFields;
    private String preparedInsert;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        this.classEntities = new ClassEntities();

        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new RepositoryException("Class " + cls.getName() + " must be annotated with @RepositoryTable");
        }

        try {
            cls.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RepositoryException("Class " + cls.getName() + " must have a default constructor", e);
        }

        this.tableName = cls.getAnnotation(RepositoryTable.class).title();
        getGettersSettersAndFieldNames(cls);
    }

    public List<T> findAll() {
        List<T> resultList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareFindAll(connection);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                T entity = createEntityFromResultSet(rs);
                resultList.add(entity);
            }
        } catch (Exception e) {
            throw new RepositoryException("Error finding all entities", e);
        }
        return resultList;
    }

    public T findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareFindById(connection)) {
            ps.setObject(1, requireNonNull(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return createEntityFromResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RepositoryException("Error finding all entities", e);
        }
    }

    public void create(T entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareInsert(connection)) {
            for (int i = 0; i < classEntities.getCachedGetters().size(); i++) {
                ps.setObject(i + 1, requireNonNull(classEntities.getCachedGetters().get(i).invoke(entity)));
            }
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error creating entity", e);
        }
    }

    public void update(Long id, String field, String value) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareUpdate(field, connection)) {
            ps.setObject(1, requireNonNull(value));
            ps.setObject(2, requireNonNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error updating entity", e);
        }
    }

    public void updateAllFields(Long id, String... values) {
        if (values.length != classEntities.getFieldNames().size()) {
            throw new IllegalArgumentException("The number of values must match the number of fields");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareUpdateAllFields(connection)) {
            for (int i = 0; i < classEntities.getFieldNames().size(); i++) {
                ps.setObject(i + 1, requireNonNull(values[i]));
            }
            ps.setObject(classEntities.getFieldNames().size() + 1, requireNonNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error updating entity", e);
        }
    }

    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareDeleteById(connection)) {
            ps.setObject(1, requireNonNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error deleting entity by ID", e);
        }
    }

    public void deleteAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareDeleteAll(connection)) {
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error deleting all entities", e);
        }
    }

    private PreparedStatement prepareFindAll(Connection connection) throws SQLException {
        return connection.prepareStatement(String.format("SELECT * FROM %s", tableName));
    }

    private PreparedStatement prepareFindById(Connection connection) throws SQLException {
        return connection.prepareStatement(String.format("SELECT * FROM %s WHERE id = ?", tableName));
    }

    private PreparedStatement prepareUpdateAllFields(Connection connection) throws SQLException {
        if (preparedUpdateAllFields == null) {
            StringBuilder query = new StringBuilder("UPDATE ");
            query.append(tableName).append(" SET ");

            for (String field : classEntities.getFieldNames()) {
                query.append(field).append(" = ?, ");
            }

            query.setLength(query.length() - 2);
            query.append(" WHERE id = ?;");

            this.preparedUpdateAllFields = query.toString();
        }

        return connection.prepareStatement(preparedUpdateAllFields);
    }

    private PreparedStatement prepareUpdate(String field, Connection connection) throws SQLException {
        if (!classEntities.getFieldNames().contains(field)) {
            throw new RepositoryException("There is no such field in the table");
        }

        String query = "UPDATE " + tableName + " SET " + field + " = ? WHERE id = ?";
        return connection.prepareStatement(query);
    }

    private PreparedStatement prepareInsert(Connection connection) throws SQLException {
        if (preparedInsert == null) {
            StringBuilder query = new StringBuilder("INSERT INTO ");
            query.append(tableName).append(" (");

            for (String fieldName : classEntities.getFieldNames()) {
                query.append(fieldName).append(", ");
            }
            query.setLength(query.length() - 2);
            query.append(") VALUES (");
            query.append("?, ".repeat(classEntities.getFieldNames().size()));
            query.setLength(query.length() - 2);
            query.append(");");

            this.preparedInsert = query.toString();
        }

        return connection.prepareStatement(preparedInsert);
    }

    private PreparedStatement prepareDeleteById(Connection connection) throws SQLException {
        return connection.prepareStatement(String.format("DELETE FROM %s WHERE id = ?", tableName));

    }

    private PreparedStatement prepareDeleteAll(Connection connection) throws SQLException {
        return connection.prepareStatement(String.format("DELETE FROM %s", tableName));
    }

    private void getGettersSettersAndFieldNames(Class<T> cls) {
        var fields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .toList();

        if (fields.isEmpty()) {
            throw new RepositoryException("No fields with @RepositoryField annotation found");
        }

        classEntities.getCachedGetters()
                .addAll(fields.stream()
                        .map(f -> {
                            String getterName = "get" + capitalize(f.getName());
                            try {
                                return cls.getMethod(getterName);
                            } catch (NoSuchMethodException e) {
                                throw new RepositoryException("No getter found for field: " + f.getName(), e);
                            }
                        }).toList());

        classEntities.getCachedSetters()
                .addAll(fields.stream()
                        .map(f -> {
                            String setterName = "set" + capitalize(f.getName());
                            try {
                                return cls.getMethod(setterName, f.getType());
                            } catch (NoSuchMethodException e) {
                                throw new RepositoryException("No setter found for field: " + f.getName(), e);
                            }
                        }).toList());

        if (classEntities.getCachedGetters().isEmpty()) {
            throw new RepositoryException("No valid getters found for fields with @RepositoryField annotation");
        }

        if (classEntities.getCachedSetters().isEmpty()) {
            throw new RepositoryException("No valid setters found for fields with @RepositoryField annotation");
        }

        classEntities.getFieldNames()
                .addAll(fields.stream()
                        .map(f -> {
                            String fieldName = f.getAnnotation(RepositoryField.class).name();
                            if (fieldName.isEmpty()) {
                                return f.getName();
                            }
                            return fieldName;
                        })
                        .toList());
    }


    private T createEntityFromResultSet(ResultSet rs) throws Exception {
        T entity = cls.getDeclaredConstructor().newInstance();
        for (int i = 0; i < classEntities.getFieldNames().size(); i++) {
            String fieldName = classEntities.getFieldNames().get(i);
            Method setter = classEntities.getCachedSetters().get(i);
            setter.invoke(entity, rs.getObject(fieldName));
        }
        Method setId = cls.getMethod("setId", Long.class);
        setId.invoke(entity, rs.getLong("id"));
        return entity;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Getter
    private static class ClassEntities {
        private final List<Method> cachedGetters;
        private final List<Method> cachedSetters;
        private final List<String> fieldNames;

        public ClassEntities() {
            this.cachedGetters = new ArrayList<>();
            this.cachedSetters = new ArrayList<>();
            this.fieldNames = new ArrayList<>();
        }
    }
}