package ru.otus;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.dao.DriverManagerDataSource;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterSecurity;
import ru.otus.services.*;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница отображения клиентов
    http://localhost:8080/clients

    // Страница отображения заданного клиента
    http://localhost:8080/client/{id}

    // Создание клиентов
    http://localhost:8080/admin
*/
@Slf4j
public class homeWork {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {

        var configuration = new Configuration().configure();
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        var dataSource = new DriverManagerDataSource(dbUrl, dbUserName, dbPassword);
        flywayMigrations(dataSource);

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        DBServiceClient clientService = new DbServiceClientImpl(transactionManager, clientTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UserAuthService authService = new UserAuthServiceImpl(new InMemoryUserDao());

        UsersWebServer usersWebServer =
                new UsersWebServerWithFilterSecurity(WEB_SERVER_PORT, authService, clientService, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
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
