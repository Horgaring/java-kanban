package handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
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

public class HttpTaskManagerEpicsTest {

    // создаём экземпляр InMemoryTaskManager
    private TaskManager manager = Managers.getDefault();

    // передаём его в качестве аргумента в конструктор HttpTaskServer
    private HttpTaskServer taskServer;
    private Gson gson;

    public HttpTaskManagerEpicsTest() throws IOException {
        taskServer = new HttpTaskServer(manager);
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
    public void shouldReturn201AndAddEpicToTheManager() throws IOException, InterruptedException {
        var epic = new Epic(0, "Test 2", "");
        // конвертируем её в JSON
        String taskJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
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
        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void shouldReturn200AndRemoveEpic() throws IOException, InterruptedException {
        var epic = new Epic(0, "", "");
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getEpics().size(), "Некорректное количество задач");
    }

    @Test
    public void shouldReturnEpics() throws IOException, InterruptedException {
        var epic = new Epic(0, "", "");
        var epic2 = new Epic(0, "2", "");

        manager.addEpic(epic);
        manager.addEpic(epic2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(manager.getEpics(), gson.fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType()));
    }

    @Test
    public void shouldReturnEpicById() throws IOException, InterruptedException {
        var epic = new Epic(0, "", "");
        manager.addEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(epic, gson.fromJson(response.body(), Epic.class));
    }

    @Test
    public void shouldReturnSubTasksByEpicId() throws IOException, InterruptedException {
        var epic = new Epic(0, "", "");
        manager.addEpic(epic);

        SubTask task = new SubTask(0, "Test 2", "Testing task 2", epic.getId(),
                LocalDateTime.now(), Duration.ofMinutes(5));

        manager.addSubTask(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertEquals(manager.getSubTasks(), gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType()));
    }
}