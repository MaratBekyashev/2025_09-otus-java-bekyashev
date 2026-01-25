package ru.otus.servlet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import ru.otus.model.Client;
import ru.otus.services.DBServiceClient;

@SuppressWarnings({"java:S1989"})
@RequiredArgsConstructor
public class ClientApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient DBServiceClient clientService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Long id = extractIdFromRequest(request);
        Client client = clientService.getClient(id).orElseThrow();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(client.toString());
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }
}
