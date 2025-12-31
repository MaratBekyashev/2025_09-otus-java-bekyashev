package ru.otus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.crm.service.DbServiceManagerImpl;
import ru.otus.jdbc.mapper.*;

@SuppressWarnings({"java:S125", "java:S1481"})
@Slf4j
public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5432/otus";
    private static final String USER = "otus";
    private static final String PASSWORD = "otus";

    public static void main(String[] args) {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement pst = connection.prepareStatement("select 1");
                    ResultSet rs = pst.executeQuery()) {

                if (rs.next()) {
                    System.out.println("DB OK: " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        Map<String, Function<ResultSet, ?>> clientHandlersMap = Map.of(
                "findById",
                        rs -> {
                            try {
                                if (rs.next()) {
                                    return new Client(rs.getLong("id"), rs.getString("name"));
                                }
                                return null;
                            } catch (SQLException e) {
                                throw new DataTemplateException(e);
                            }
                        },
                "findAll",
                        rs -> {
                            try {
                                List<Client> result = new ArrayList<>();
                                while (rs.next()) {
                                    result.add(new Client(rs.getLong("id"), rs.getString("name")));
                                }
                                return result;
                            } catch (SQLException e) {
                                throw new DataTemplateException(e);
                            }
                        });

        var dataTemplateClient = new DataTemplateJdbc<Client>(
                dbExecutor, entityClassMetaDataClient, entitySQLMetaDataClient, clientHandlersMap);

        // Код дальше должен остаться
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        // Сделайте тоже самое с классом Manager (для него надо сделать свою таблицу)

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        Map<String, Function<ResultSet, ?>> managerHandlersMap = Map.of(
                "findById",
                        rs -> {
                            try {
                                if (rs.next()) {
                                    return new Manager(rs.getLong("no"), rs.getString("label"), rs.getString("param1"));
                                }
                                return null;
                            } catch (SQLException e) {
                                throw new DataTemplateException(e);
                            }
                        },
                "findAll",
                        rs -> {
                            try {
                                List<Manager> result = new ArrayList<>();
                                while (rs.next()) {
                                    result.add(new Manager(
                                            rs.getLong("no"), rs.getString("label"), rs.getString("param1")));
                                }
                                return result;
                            } catch (SQLException e) {
                                throw new DataTemplateException(e);
                            }
                        });

        var dataTemplateManager = new DataTemplateJdbc<Manager>(
                dbExecutor, entityClassMetaDataManager, entitySQLMetaDataManager, managerHandlersMap);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();

        log.info("Found migrations: {}", flyway.info().all().length);

        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
