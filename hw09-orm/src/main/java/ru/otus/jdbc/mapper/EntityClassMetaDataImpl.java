package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import ru.otus.exceptions.NoConstructorException;
import ru.otus.jdbc.annotations.MyId;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    private Constructor<T> constructor;

    private Field idField;

    private List<Field> allFields;

    private List<Field> fieldsWithoutId;

    private String entityName;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = getConstructor();
        this.idField = getIdField();
        this.allFields = getAllFields();
        this.fieldsWithoutId = getFieldsWithoutId();
        this.entityName = getName();
    }

    @Override
    public String getName() {
        if (this.entityName != null) {
            return this.entityName;
        }

        this.entityName = this.clazz.getSimpleName();
        return this.entityName;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (this.constructor != null) {
            return this.constructor;
        }
        try {
            this.constructor = this.clazz.getDeclaredConstructor();
            return this.constructor;
        } catch (NoSuchMethodException ex) {
            throw new NoConstructorException(ex);
        }
    }

    @Override
    public Field getIdField() {
        if (this.idField != null) {
            return this.idField;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (var field : fields) {
            if (field.isAnnotationPresent(MyId.class)) {
                this.idField = field;
            }
        }
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (this.allFields != null) {
            return this.allFields;
        }
        this.allFields = List.of(clazz.getDeclaredFields());
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (this.fieldsWithoutId != null) {
            return fieldsWithoutId;
        }
        this.fieldsWithoutId =
                getAllFields().stream().filter(i -> !i.equals(getIdField())).toList();
        return this.fieldsWithoutId;
    }
}
