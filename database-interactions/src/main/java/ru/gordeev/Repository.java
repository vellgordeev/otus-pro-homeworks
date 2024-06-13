package ru.gordeev;

import ru.gordeev.exceptions.RepositoryException;
import ru.gordeev.entities.EntityField;
import ru.gordeev.entities.EntityMetaInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Repository<T> {

    private final Class<T> cls;
    private final EntityMetaInfo entityMetaInfo;
    private final DataSource dataSource;
    private final String tableName;
    private String preparedUpdateAllFields;
    private String preparedInsert;

    public Repository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;

        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new RepositoryException("Class " + cls.getName() + " must be annotated with @RepositoryTable");
        }

        try {
            cls.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RepositoryException("Class " + cls.getName() + " must have a default constructor", e);
        }

        this.tableName = cls.getAnnotation(RepositoryTable.class).title();
        this.entityMetaInfo = getEntityMetaInfo(cls);
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
            throw new RepositoryException("Error finding entity by ID", e);
        }
    }

    public void create(T entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareInsert(connection)) {
            for (int i = 0; i < entityMetaInfo.getFields().size(); i++) {
                EntityField entityField = entityMetaInfo.getFields().get(i);
                ps.setObject(i + 1, requireNonNull(entityField.getGetter().invoke(entity)));
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
        if (values.length != entityMetaInfo.getFields().size()) {
            throw new IllegalArgumentException("The number of values must match the number of fields");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = prepareUpdateAllFields(connection)) {
            for (int i = 0; i < entityMetaInfo.getFields().size(); i++) {
                ps.setObject(i + 1, requireNonNull(values[i]));
            }
            ps.setObject(entityMetaInfo.getFields().size() + 1, requireNonNull(id));
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

            for (EntityField field : entityMetaInfo.getFields()) {
                query.append(field.getActualFieldName()).append(" = ?, ");
            }

            query.setLength(query.length() - 2);
            query.append(" WHERE id = ?;");

            this.preparedUpdateAllFields = query.toString();
        }

        return connection.prepareStatement(preparedUpdateAllFields);
    }

    private PreparedStatement prepareUpdate(String field, Connection connection) throws SQLException {
        boolean fieldExists = entityMetaInfo.getFields().stream()
                .anyMatch(entityField -> entityField.getActualFieldName().equals(field));

        if (!fieldExists) {
            throw new RepositoryException("There is no such field in the table");
        }

        String query = "UPDATE " + tableName + " SET " + field + " = ? WHERE id = ?";
        return connection.prepareStatement(query);
    }

    private PreparedStatement prepareInsert(Connection connection) throws SQLException {
        if (preparedInsert == null) {
            StringBuilder query = new StringBuilder("INSERT INTO ");
            query.append(tableName).append(" (");

            for (EntityField field : entityMetaInfo.getFields()) {
                query.append(field.getActualFieldName()).append(", ");
            }
            query.setLength(query.length() - 2);
            query.append(") VALUES (");
            query.append("?, ".repeat(entityMetaInfo.getFields().size()));
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

    private EntityMetaInfo getEntityMetaInfo(Class<T> cls) {
        Field idField = null;
        List<EntityField> fields = new ArrayList<>();

        for (Field field : cls.getDeclaredFields()) {
            if (field.isAnnotationPresent(RepositoryIdField.class)) {
                idField = field;
            } else if (field.isAnnotationPresent(RepositoryField.class)) {
                String actualFieldName = field.getAnnotation(RepositoryField.class).name();
                if (actualFieldName.isEmpty()) {
                    actualFieldName = field.getName();
                }

                Method getter = getMethod(cls, "get" + capitalize(field.getName()));
                Method setter = getMethod(cls, "set" + capitalize(field.getName()), field.getType());

                fields.add(new EntityField(field, actualFieldName, getter, setter));
            }
        }

        if (idField == null) {
            throw new RepositoryException("No field with @RepositoryIdField annotation found");
        }

        String idFieldName = idField.getName();

        Method idGetter = getMethod(cls, "get" + capitalize(idField.getName()));
        Method idSetter = getMethod(cls, "set" + capitalize(idField.getName()), idField.getType());

        EntityField idEntityField = new EntityField(idField, idFieldName, idGetter, idSetter);

        return new EntityMetaInfo(idEntityField, fields);
    }

    private Method getMethod(Class<T> cls, String methodName) {
        try {
            return cls.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RepositoryException("No method found: " + methodName, e);
        }
    }

    private Method getMethod(Class<T> cls, String methodName, Class<?> parameterType) {
        try {
            return cls.getMethod(methodName, parameterType);
        } catch (NoSuchMethodException e) {
            throw new RepositoryException("No method found: " + methodName, e);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private T createEntityFromResultSet(ResultSet rs) throws Exception {
        T entity = cls.getDeclaredConstructor().newInstance();
        for (EntityField entityField : entityMetaInfo.getFields()) {
            String fieldName = entityField.getActualFieldName();
            Method setter = entityField.getSetter();
            setter.invoke(entity, rs.getObject(fieldName));
        }
        entityMetaInfo.getId().getSetter().invoke(entity, rs.getLong("id"));
        return entity;
    }
}
