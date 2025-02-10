package ru.otus.homework.jdbc.mapper;

import ru.otus.homework.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> entityClass;
    private final List<Field> fields;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.fields = Arrays.asList(entityClass.getDeclaredFields());
    }


    @Override
    public String getName() {
        return entityClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        Class<?>[] parameterTypes = new Class<?>[fields.size()];
        int index = 0;
        for (Field field : fields) {
            parameterTypes[index++] = field.getType();
        }
        try {
            return entityClass.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        Field idField = null;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                idField = field;
            }
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fields.stream()
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
