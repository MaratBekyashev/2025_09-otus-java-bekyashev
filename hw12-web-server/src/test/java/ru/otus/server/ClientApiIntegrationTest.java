package ru.otus.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DBServiceClient;
import ru.otus.services.DbServiceClientImpl;
import ru.otus.servlet.ClientApiServlet;

class ClientApiIntegrationTest {

    private Server server;
    private SessionFactory sessionFactory;
    private DBServiceClient clientService;

    private int port = 8090; // отдельный порт для тестов

    @BeforeEach
    void setUp() throws Exception {

        // --- Hibernate (in-memory H2) ---
        Configuration configuration = new Configuration().configure("hibernate-test.cfg.xml");

        sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        clientService = new DbServiceClientImpl(transactionManager, clientTemplate);

        // --- Jetty ---
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(new ServletHolder(new ClientApiServlet(clientService)), "/client/*");

        server.setHandler(context);
        server.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        server.stop();
        sessionFactory.close();
    }

    @Test
    @DisplayName("Проверяем создание клиента в БД и интеграцию веб-сервера с БД")
    void shouldReturnClientById() throws Exception {

        Client client = new Client("HttpTestClient");
        Address address = new Address();
        address.setStreet("Test street");
        client.setAddress(address);
        client.setPhones(List.of(new Phone("1234567")));

        Client saved = clientService.saveClient(client);

        String urlString = "http://localhost:" + port + "/client/" + saved.getId();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);

        int responseCode = connection.getResponseCode();

        assertThat(responseCode).isEqualTo(200);

        String responseBody;
        try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()))) {
            responseBody = reader.readLine();
        }

        System.out.println("Response: " + responseBody);

        // проверяем, что в строке есть нужные данные
        assertThat(responseBody).contains("Client");
        assertThat(responseBody).contains("id=" + saved.getId());
        assertThat(responseBody).contains("name=HttpTestClient");
        assertThat(responseBody).contains("phoneNumber=1234567");
    }
}
