package ru.otus.crm.dbmigrations;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

@Slf4j
public class MigrationsExecutorFlyway {

    private final Flyway flyway;

    public MigrationsExecutorFlyway(String dbUrl, String dbUserName, String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/db/migration")
                .load();
    }

    public void executeMigrations() {
        log.info("db migration started...");
        log.info("Found migrations: {}", flyway.info().all().length);
        flyway.migrate();
        log.info("db migration finished.");
    }
}
