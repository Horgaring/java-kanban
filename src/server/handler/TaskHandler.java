package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.TimeIntervalConflictException;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (exchange.getRequestURI().getPath().split("/").length == 2) {
                    getTasks(exchange);
                } else  {
                    getTaskById(exchange);
                }
                break;
            case "POST":
                createTask(exchange);
                break;
            case "DELETE":
                deleteTask(exchange);
                break;
        }
        exchange.close();
    }

    private void deleteTask(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int taskId = Integer.parseInt(id);
            taskManager.deleteTask(taskId);

            exchange.sendResponseHeaders(200, 0);

        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void createTask(HttpExchange exchange) throws IOException {
        try {
            Task task =  gson.fromJson(new String(exchange.getRequestBody().readAllBytes(), Charset.defaultCharset()), Task.class);

            if (task.getId() == 0) {
                taskManager.addTask(task);
            } else  {
                taskManager.updateTask(task);
            }

            exchange.sendResponseHeaders(201, 0);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().write(e.getMessage().getBytes());
        } catch (TimeIntervalConflictException e) {
            exchange.sendResponseHeaders(406, 0);
        }
    }

    private void getTaskById(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int taskId = Integer.parseInt(id);
            if (taskManager.getTask(taskId).isEmpty()) {
                sendNotFound(exchange);
                return;
            }

            sendText(exchange, gson.toJson(taskManager.getTask(taskId).get(), Task.class));
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getTasks(), new TypeToken<List<Task>>(){}.getType()));
    }
}
