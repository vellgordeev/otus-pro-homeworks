package ru.gordeev;

import ru.gordeev.exceptions.RepositoryException;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class AbstractRepository<T> {

    private final Class<T> cls;
    private final DataSource dataSource;
    private final String tableName;
    private List<Method> cachedGetters;
    private List<Method> cachedSetters;
    private List<String> fieldNames;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        tableName = cls.getAnnotation(RepositoryTable.class).title();
        getGettersSettersAndFieldNames(cls);
    }

    public List<T> findAll() {
        List<T> resultList = new ArrayList<>();
        try (PreparedStatement ps = prepareFindAll();
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
        try (PreparedStatement ps = prepareFindById()) {
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
        try (PreparedStatement ps = prepareInsert()) {
            for (int i = 0; i < cachedGetters.size(); i++) {
                ps.setObject(i + 1, requireNonNull(cachedGetters.get(i).invoke(entity)));
            }
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error creating entity", e);
        }
    }

    public void update(Long id, String field, String value) {
        try (PreparedStatement ps = prepareUpdate(field)) {
            ps.setObject(1, requireNonNull(value));
            ps.setObject(2, requireNonNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error updating entity", e);
        }
    }

    public void updateAllFields(Long id, String... values) {
        if (values.length != fieldNames.size()) {
            throw new IllegalArgumentException("The number of values must match the number of fields");
        }

        try (PreparedStatement ps = prepareUpdateAllFields()) {
            for (int i = 0; i < fieldNames.size(); i++) {
                ps.setObject(i + 1, requireNonNull(values[i]));
            }
            ps.setObject(fieldNames.size() + 1, requireNonNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error updating entity", e);
        }
    }

    public void deleteById(Long id) {
        try (PreparedStatement ps = prepareDeleteById()) {
            ps.setObject(1, requireNonNull(id));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error deleting entity by ID", e);
        }
    }

    public void deleteAll() {
        try (PreparedStatement ps = prepareDeleteAll()) {
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RepositoryException("Error deleting all entities", e);
        }
    }

    private PreparedStatement prepareFindAll() throws SQLException {
       return dataSource.getConnection().prepareStatement(String.format("SELECT * FROM %s", tableName));
    }

    private PreparedStatement prepareFindById() throws SQLException {
        return dataSource.getConnection().prepareStatement(String.format("SELECT * FROM %s WHERE id = ?", tableName));
    }

    private PreparedStatement prepareUpdateAllFields() throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");

        for (String field : fieldNames) {
            query.append(field).append(" = ?, ");
        }

        query.setLength(query.length() - 2);
        query.append(" WHERE id = ?;");
       return dataSource.getConnection().prepareStatement(query.toString());
    }

    private PreparedStatement prepareUpdate(String field) throws SQLException {
        if (!fieldNames.contains(field)) {
            throw new RepositoryException("There is no such field in the table");
        }

        String query = "UPDATE " + tableName + " SET " + field + " = ? WHERE id = ?";
        return dataSource.getConnection().prepareStatement(query);
    }

    private PreparedStatement prepareInsert() throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        for (String fieldName : fieldNames) {
            query.append(fieldName).append(", ");
        }
        query.setLength(query.length() - 2);
        query.append(") VALUES (");
        query.append("?, ".repeat(fieldNames.size()));
        query.setLength(query.length() - 2);
        query.append(");");

        return dataSource.getConnection().prepareStatement(query.toString());
    }

    private PreparedStatement prepareDeleteById() throws SQLException {
        return dataSource.getConnection().prepareStatement(String.format("DELETE FROM %s WHERE id = ?", tableName));

    }

    private PreparedStatement prepareDeleteAll() throws SQLException {
        return dataSource.getConnection().prepareStatement(String.format("DELETE FROM %s", tableName));
    }

    private void getGettersSettersAndFieldNames(Class<T> cls) {
        var fields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .toList();

        if (fields.isEmpty()) {
            throw new RepositoryException("No fields with @RepositoryField annotation found");
        }

        cachedGetters = fields.stream()
                .map(f -> {
                    String getterName = "get" + capitalize(f.getName());
                    try {
                        return cls.getMethod(getterName);
                    } catch (NoSuchMethodException e) {
                        throw new RepositoryException("No getter found for field: " + f.getName(), e);
                    }
                }).toList();

        cachedSetters = fields.stream()
                .map(f -> {
                    String setterName = "set" + capitalize(f.getName());
                    try {
                        return cls.getMethod(setterName, f.getType());
                    } catch (NoSuchMethodException e) {
                        throw new RepositoryException("No setter found for field: " + f.getName(), e);
                    }
                }).toList();

        if (cachedGetters.isEmpty()) {
            throw new RepositoryException("No valid getters found for fields with @RepositoryField annotation");
        }

        if (cachedSetters.isEmpty()) {
            throw new RepositoryException("No valid setters found for fields with @RepositoryField annotation");
        }

        fieldNames = fields.stream()
                .map(f -> {
                    String fieldName = f.getAnnotation(RepositoryField.class).name();
                    if (fieldName.isEmpty()) {
                        throw new RepositoryException("Field name in @RepositoryField annotation cannot be empty for field: " + f.getName());
                    }
                    return fieldName;
                })
                .toList();
    }


    private T createEntityFromResultSet(ResultSet rs) throws Exception {
        T entity = cls.getDeclaredConstructor().newInstance();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            Method setter = cachedSetters.get(i);
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
}