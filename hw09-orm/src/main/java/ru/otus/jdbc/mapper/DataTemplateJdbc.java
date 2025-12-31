package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохраняет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
@RequiredArgsConstructor
@Slf4j
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;

    private final EntityClassMetaData<T> entityMetaData;

    private final EntitySQLMetaData entitySQLMetaData;

    private final Map<String, Function<ResultSet, ?>> sqlMappers;

    @Override
    public Optional<T> findById(Connection connection, long id) {
        Function<ResultSet, T> handler = (Function<ResultSet, T>) sqlMappers.get("findById");
        String query = entitySQLMetaData.getSelectByIdSql();
        var result = dbExecutor.executeSelect(connection, query, List.of(id), handler);
        return result;
    }

    @Override
    public List<T> findAll(Connection connection) {
        Function<ResultSet, List<T>> handler = (Function<ResultSet, List<T>>) sqlMappers.get("findAll");
        String query = entitySQLMetaData.getSelectAllSql();
        Optional<List<T>> dataList = dbExecutor.executeSelect(connection, query, Collections.emptyList(), handler);
        List<T> result = dataList.get();
        return result;
    }

    @Override
    public long insert(Connection connection, T object) {
        String insertQuery = entitySQLMetaData.getInsertSql();
        log.info("Generated SQL text for INSERT: {}", insertQuery);

        List<Object> params = new ArrayList<>();

        for (Field field : entityMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                params.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("Params for INSERT: {}", params);

        long result = dbExecutor.executeStatement(connection, insertQuery, params);
        return result;
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            List<Object> params = new ArrayList<>();

            // сначала все поля, кроме id
            for (Field field : entityMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(object));
            }

            // потом id
            Field idField = entityMetaData.getIdField();
            idField.setAccessible(true);
            params.add(idField.get(object));
            log.info("Generated SQL text for UPDATE: {}", entitySQLMetaData.getUpdateSql());
            log.info("Params for UPDATE = {}", params);
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
