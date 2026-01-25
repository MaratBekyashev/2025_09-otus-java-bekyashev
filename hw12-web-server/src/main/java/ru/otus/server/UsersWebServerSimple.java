package ru.otus.server;

import org.eclipse.jetty.ee10.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.DBServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.*;

public class UsersWebServerSimple implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceClient clientService;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public UsersWebServerSimple(int port, DBServiceClient clientService, TemplateProcessor templateProcessor) {
        this.clientService = clientService;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();
        servletContextHandler.setErrorHandler(new ErrorPageErrorHandler() {
            {
                addErrorPage(401, "/error401");
                addErrorPage(403, "/error403");
            }
        });

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/admin", "/clients", "/client/*"));

        server.setHandler(sequence);
    }

    @SuppressWarnings({"squid:S1172"})
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletContextHandler.addServlet(new ServletHolder(new Error401Servlet(templateProcessor)), "/error401");
        servletContextHandler.addServlet(new ServletHolder(new Error403Servlet(templateProcessor)), "/error403");
        servletContextHandler.addServlet(
                new ServletHolder(new AdminServlet(templateProcessor, clientService)), "/admin");
        servletContextHandler.addServlet(
                new ServletHolder(new ClientServlet(templateProcessor, clientService)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new ClientApiServlet(clientService)), "/client/*");

        return servletContextHandler;
    }
}
