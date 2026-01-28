package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;

@RequiredArgsConstructor
public class AdminServlet extends HttpServlet {

    private final transient TemplateProcessor templateProcessor;
    private final transient DBServiceClient clientService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println(templateProcessor.getPage("create-client.html", Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("clientName");
        String street = request.getParameter("street");
        String[] phonesArr = request.getParameterValues("phones");

        Address address = null;
        if ((street != null && !street.isBlank())) {
            address = new Address();
            address.setStreet(street);
        }

        // 3. Формируем список телефонов
        List<Phone> phones = Arrays.asList(phonesArr).stream()
                .filter(e -> e != null && !e.isBlank())
                .map(e -> new Phone(e.trim()))
                .collect(Collectors.toList());

        var client = new Client(name);
        client.setAddress(address);
        client.setPhones(phones);
        clientService.saveClient(client);

        response.sendRedirect("/clients");
    }
}
