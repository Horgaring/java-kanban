package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.TimeIntervalConflictException;
import manager.TaskManager;
import model.Epic;
import model.SubTask;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                var path = exchange.getRequestURI().getPath().split("/");
                if (path.length == 2) {
                    getEpic(exchange);
                } else if (path.length == 3) {
                    getEpicById(exchange);
                } else {
                    getEpicSubtasks(exchange);
                }

                break;
            case "POST":
                createEpic(exchange);
                break;
            case "DELETE":
                deleteEpic(exchange);
                break;
        }
        exchange.close();
    }

    private void getEpicSubtasks(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int epicId = Integer.parseInt(id);
            if (taskManager.getEpic(epicId).isEmpty()) {
               super.sendNotFound(exchange);
                return;
            }

            super.sendText(exchange, gson.toJson(
                    taskManager.getSubTasksByEpicId(epicId),
                    new TypeToken<List<SubTask>>(){}.getType()
            ));
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void deleteEpic(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int epicId = Integer.parseInt(id);
            taskManager.deleteEpic(epicId);

            exchange.sendResponseHeaders(200, 0);
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        try {
            Epic epic =  gson.fromJson(
                    new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset()), Epic.class);
            taskManager.addEpic(epic);
            exchange.sendResponseHeaders(201, 0);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
        } catch (TimeIntervalConflictException e) {
            super.sendHasInteractions(exchange);
        }
    }

    private void getEpicById(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int epicId = Integer.parseInt(id);
            if (taskManager.getEpic(epicId).isEmpty()) {
                super.sendNotFound(exchange);
                return;
            }

            super.sendText(exchange, gson.toJson(taskManager.getEpic(epicId).get(), Epic.class));
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void getEpic(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getEpics(), new TypeToken<List<Epic>>(){}.getType()));
    }
}
