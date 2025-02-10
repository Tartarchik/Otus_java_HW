package ru.otus.homework.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClass;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM %s".formatted(entityClass.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM %s WHERE %s = ?".formatted(entityClass.getName(), entityClass.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClass.getFieldsWithoutId();
        String fieldsNameStr = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String values = fieldsWithoutId.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        return "insert into %s (%s) values (%s)".formatted(entityClass.getName(), fieldsNameStr, values);
    }

    @Override
    public String getUpdateSql() {
        String fieldsName = entityClass.getFieldsWithoutId().stream().map(field ->
                field.getName() + " = ?").collect(Collectors.joining(", "));

        return "UPDATE %s SET %s WHERE %s = ?".formatted(entityClass.getName(), fieldsName, entityClass.getIdField().getName());
    }
}
