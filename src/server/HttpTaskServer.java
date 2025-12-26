package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import server.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    static final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;
    private Gson gson;

    public HttpTaskServer(TaskManager manager, GsonBuilder builder) throws IOException {
        taskManager = manager;
        gson = builder.create();

        this.server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TaskHandler(taskManager, gson));
        server.createContext("/subtasks", new SubTaskHandler(taskManager, gson));
        server.createContext("/epics", new EpicHandler(taskManager, gson));
        server.createContext("/history", new HistoryHandler(taskManager, gson));
        server.createContext("/prioritized", new PrioritizedTaskHandler(taskManager, gson));
    }

    public void start() throws IOException {
        server.start();
    }

    public void stop() throws IOException {
        server.stop(0);
    }

    public Gson getGson() {
        return gson;
    }
}
