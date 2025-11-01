package handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = Managers.getDefault();


    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer;
    Gson gson;

    public HttpTaskManagerTasksTest() throws IOException {
        taskServer = new HttpTaskServer(manager, Managers.getDefaultGsonBuilder());
        this.gson = taskServer.getGson();
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() throws IOException {
        taskServer.stop();
    }

    @Test
    public void shouldReturn201AndAddTaskToTheManager() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")

                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturn406A() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));

        manager.addTask(task2);
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")

                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());
    }

    @Test
    public void shouldReturn200AndRemoveTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));

        manager.addTask(task);
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size(), "Некорректное количество задач");
    }

    @Test
    public void shouldReturnTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task(0, "Test 3", "Testing task 2",
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(5));

        manager.addTask(task);
        manager.addTask(task2);
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(manager.getTasks(), gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType()));
    }

    @Test
    public void shouldReturnTaskById() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task(0, "Test 3", "Testing task 2",
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(5));

        manager.addTask(task);
        manager.addTask(task2);
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(task, gson.fromJson(response.body(), Task.class));
    }
}