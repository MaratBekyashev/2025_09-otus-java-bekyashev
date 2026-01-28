package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.otus.services.TemplateProcessor;

@RequiredArgsConstructor
public class Error401Servlet extends HttpServlet {

    private final TemplateProcessor templateProcessor;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage("error401.html", Map.of()));
    }
}
