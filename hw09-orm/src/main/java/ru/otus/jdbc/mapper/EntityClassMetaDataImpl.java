package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import ru.otus.exceptions.NoConstructorException;
import ru.otus.exceptions.NoIdException;
import ru.otus.jdbc.annotations.MyId;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    private final Constructor<T> constructor;

    private Field idField;

    private final List<Field> allFields;

    private final List<Field> fieldsWithoutId;

    private final String entityName;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        try {
            this.constructor = this.clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new NoConstructorException(ex);
        }

        Field[] fields = this.clazz.getDeclaredFields();
        for (var field : fields) {
            if (field.isAnnotationPresent(MyId.class)) {
                this.idField = field;
                break;
            }
        }
        if (this.idField == null) {
            throw new NoIdException("No ID field for class: " + this.clazz.getName());
        }

        this.allFields = List.of(clazz.getDeclaredFields());
        this.fieldsWithoutId =
                this.allFields.stream().filter(i -> !i.equals(this.idField)).toList();

        this.entityName = this.clazz.getSimpleName();
    }

    @Override
    public String getName() {
        return this.entityName;
    }

    @Override
    public Constructor<T> getConstructor() {
        return this.constructor;
    }

    @Override
    public Field getIdField() {
        return this.idField;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldsWithoutId;
    }
}
