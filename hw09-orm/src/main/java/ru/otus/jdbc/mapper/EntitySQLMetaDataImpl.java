package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    @Override
    public String getSelectAllSql() {
        var result = "select " + String.join(", ", getAllFieldsNames()) + " from " + entityClassMetaData.getName();
        return result;
    }

    @Override
    public String getSelectByIdSql() {
        String result =
                getSelectAllSql() + " where " + entityClassMetaData.getIdField().getName() + " = ?";
        return result;
    }

    @Override
    public String getInsertSql() {
        List<String> fields = getNonIdFields();
        List<String> ss = fields.stream()
                .map(i -> {
                    return "?";
                })
                .toList();
        var result = "insert into " + entityClassMetaData.getName() + "(" + String.join(",", fields) + ")" + " values ("
                + String.join(",", ss) + ")";
        return result;
    }

    @Override
    public String getUpdateSql() {
        List<String> fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .toList();
        List<String> updateFields = fields.stream().map(e -> e + " = ?").toList();
        String result = "update " + entityClassMetaData.getName() + " set " + String.join(",", updateFields)
                + " where "
                + entityClassMetaData.getIdField().getName() + " = ?";
        return result;
    }

    private List<String> getAllFieldsNames() {
        List<String> result =
                entityClassMetaData.getAllFields().stream().map(Field::getName).toList();
        return result;
    }

    private List<String> getNonIdFields() {
        List<String> result = entityClassMetaData.getAllFields().stream()
                .filter(e -> !e.equals(entityClassMetaData.getIdField()))
                .map(Field::getName)
                .toList();
        return result;
    }
}
