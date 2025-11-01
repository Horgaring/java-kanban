package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import exception.TimeIntervalConflictException;
import manager.TaskManager;
import model.SubTask;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class SubTaskHandler extends BaseHttpHandler {


    public SubTaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (exchange.getRequestURI().getPath().split("/").length == 2) {
                    getSubTasks(exchange);
                } else  {
                    getSubTaskById(exchange);
                }
                break;
            case "POST":
                createSubTask(exchange);
                break;
            case "DELETE":
                deleteSubTask(exchange);
                break;
        }
        exchange.close();
    }

    private void deleteSubTask(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int taskId = Integer.parseInt(id);
            taskManager.deleteSubTask(taskId);

            exchange.sendResponseHeaders(200, 0);

        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void createSubTask(HttpExchange exchange) throws IOException {
        try {
            SubTask task =  gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                    Charset.defaultCharset()), SubTask.class);

            if (task.getId() == 0) {
                taskManager.addSubTask(task);
            } else  {
                taskManager.updateSubTask(task);
            }

            exchange.sendResponseHeaders(201, 0);
        } catch (JsonSyntaxException e) {
            exchange.sendResponseHeaders(400, 0);
        } catch (TimeIntervalConflictException e) {
            exchange.sendResponseHeaders(406, 0);
        }
    }

    private void getSubTaskById(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];

            int taskId = Integer.parseInt(id);
            if (taskManager.getSubTask(taskId).isEmpty()) {
                sendNotFound(exchange);
                return;
            }
            sendText(exchange,gson.toJson(taskManager.getSubTask(taskId).get(), SubTask.class));
        } catch (NumberFormatException e) {
            exchange.sendResponseHeaders(400, 0);
        }
    }

    private void getSubTasks(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getSubTasks(), new TypeToken<List<SubTask>>(){}.getType()));
    }
}
