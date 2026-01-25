package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"java:S1989"})
@RequiredArgsConstructor
public class ClientServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "clientList";

    private final transient TemplateProcessor templateProcessor;

    private final transient DBServiceClient clientService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        var client = clientService.findAll();
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER, client);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}
