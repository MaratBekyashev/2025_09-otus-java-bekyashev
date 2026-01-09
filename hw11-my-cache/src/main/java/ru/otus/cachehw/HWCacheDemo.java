package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.HomeWork;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        var dataSource = new DriverManagerDataSource(HomeWork.URL, HomeWork.USER, HomeWork.PASSWORD);

        HomeWork.flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);

        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entityClassMetaDataClient, entitySQLMetaDataClient);

        var clientService = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        Client savedClient = clientService.saveClient(new Client("My Test Cache Client"));
        // ********************* доказать, что кэш работает быстрее БД ************************
        Long clientId = savedClient.getId();
        logger.info("Saved client is {}", savedClient);
        long start = System.currentTimeMillis();
        clientService.getClient(clientId).get();
        logger.info("GetSavedClient. First call: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        clientService.getClient(clientId);
        logger.info("GetSavedClient. Second call (cache): " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        clientService.getClient(clientId);
        logger.info("GetSavedClient. Third call (cache): " + (System.currentTimeMillis() - start));
        // clientId = null;
        // ПОказать, что кэш очишается GC
        System.gc();
        logger.info("Garbage collector worked...");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Client client = clientService.getClient(clientId).get();
        logger.info("GetSavedClient after GC. Client is {}", client);
    }
}
