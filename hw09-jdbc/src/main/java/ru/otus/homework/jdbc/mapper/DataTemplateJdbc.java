package ru.otus.homework.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;

    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createInstance(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var entityList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            entityList.add(createInstance(rs));
                        }
                        return entityList;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new DataTemplateException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), getValues(entity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), getValues(entity));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T createInstance(ResultSet rs) {
        List<Object> entityValues = new ArrayList<>();
        for (Field field : entityClassMetaData.getAllFields()) {
            try {
                entityValues.add(rs.getObject(field.getName()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return entityClassMetaData.getConstructor().newInstance(entityValues.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getValues(T entity) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(f -> {
            try {
                f.setAccessible(true);
                return f.get(entity);
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }).toList();
    }
}