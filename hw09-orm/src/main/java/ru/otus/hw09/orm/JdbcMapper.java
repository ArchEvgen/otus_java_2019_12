package ru.otus.hw09.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcMapper<T> {
    private final EntityInfo entityInfo;
    private final QueryGenerator queryGenerator;

    public JdbcMapper(EntityInfo entityInfo, QueryGenerator queryGenerator) {
        this.entityInfo = entityInfo;
        this.queryGenerator = queryGenerator;
    }

    public long insert(Connection connection, T obj) throws SQLException, IllegalAccessException {
        Savepoint savePoint = connection.setSavepoint();
        var sql = queryGenerator.insert(entityInfo);
        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            List<Field> fields = entityInfo.getFields();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                addParam(pst, i + 1, field, obj);
            }
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException | IllegalAccessException ex) {
            connection.rollback(savePoint);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private void addParam(PreparedStatement pst, int index, Field field, T obj)
            throws IllegalAccessException, SQLException {
        field.setAccessible(true);
        if (field.getType() == int.class) {
            pst.setInt(index, (int)field.get(obj));
        } else if (field.getType() == long.class) {
            pst.setLong(index, (long)field.get(obj));
        } else if (field.getType() == double.class) {
            pst.setDouble(index, (double)field.get(obj));
        } else if (field.getType() == BigDecimal.class) {
            pst.setBigDecimal(index, (BigDecimal)field.get(obj));
        } else if (field.getType() == boolean.class) {
            pst.setBoolean(index, (boolean)field.get(obj));
        } else {
            Object value = field.get(obj);
            pst.setString(index, value == null ? null : value.toString());
        }
        field.setAccessible(false);
    }

    private void readField(ResultSet resultSet, Field field, T obj)
            throws IllegalAccessException, SQLException {
        field.setAccessible(true);
        if (field.getType() == int.class) {
            field.setInt(obj, resultSet.getInt(field.getName()));
        } else if (field.getType() == long.class) {
            field.setLong(obj, resultSet.getLong(field.getName()));
        } else if (field.getType() == double.class) {
            field.setDouble(obj, resultSet.getDouble(field.getName()));
        } else if (field.getType() == BigDecimal.class) {
            field.set(obj, resultSet.getBigDecimal(field.getName()));
        } else if (field.getType() == boolean.class) {
            field.setBoolean(obj, resultSet.getBoolean(field.getName()));
        } else {
            field.set(obj, resultSet.getString(field.getName()));
        }
        field.setAccessible(false);
    }

    public void update(Connection connection, T obj) throws IllegalAccessException, SQLException {
        Savepoint savePoint = connection.setSavepoint();
        var sql = queryGenerator.update(entityInfo);
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            List<Field> fields = entityInfo.getFields();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                addParam(pst, i + 1, field, obj);
            }
            addParam(pst, fields.size() + 1, entityInfo.getKeyField(), obj);
            pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savePoint);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    public T getById(Connection connection, long id) throws IllegalAccessException, SQLException, NoSuchMethodException,
            InvocationTargetException, InstantiationException {
        var sql = queryGenerator.select(entityInfo);
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setLong(1, id);
            try (ResultSet resultSet = pst.executeQuery()) {
                try {
                    if (resultSet.next()) {
                        T obj = (T) entityInfo.getEntityClass().getDeclaredConstructor().newInstance();
                        entityInfo.getKeyField().setAccessible(true);
                        entityInfo.getKeyField().setLong(obj, id);
                        entityInfo.getKeyField().setAccessible(false);
                        for (var field : entityInfo.getFields()) {
                            readField(resultSet, field, obj);
                        }
                        return obj;
                    }
                } catch (SQLException e) {
                    log.error(e.getMessage(), e);
                }
                return null;
            }
        }
    }
}
