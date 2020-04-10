package ru.otus.hw09.orm;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public String getInsertQuery() {
        return queryGenerator.insert(entityInfo);
    }

    public void addInsertQueryParams(PreparedStatement pst, T obj) throws SQLException {
        try {
            List<Field> fields = entityInfo.getFields();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                addParam(pst, i + 1, field, obj);
            }
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JdbcMapperException("Error add params of " + obj.toString(), ex);
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

    public String getUpdateQuery() {
        return queryGenerator.update(entityInfo);
    }

    public void addUpdateQueryParams(PreparedStatement pst, T obj) throws SQLException {
        try {
            List<Field> fields = entityInfo.getFields();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                addParam(pst, i + 1, field, obj);
            }
            addParam(pst, fields.size() + 1, entityInfo.getKeyField(), obj);
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JdbcMapperException("Error add params of " + obj.toString(), ex);
        }
    }

    public String getSelectByIdQuery() {
        return queryGenerator.select(entityInfo);
    }

    public T readObject(ResultSet resultSet) throws SQLException {
        try {
            if (resultSet.next()) {
                T obj = (T) entityInfo.getEntityClass().getDeclaredConstructor().newInstance();
                readField(resultSet, entityInfo.getKeyField(), obj);
                for (var field : entityInfo.getFields()) {
                    readField(resultSet, field, obj);
                }
                return obj;
            }
            return null;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new JdbcMapperException("Error reading object " + entityInfo.getEntityClass().getName(), ex);
        }
    }
}
