package handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
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

public class HttpTaskManagerHistoryTest {

    // создаём экземпляр InMemoryTaskManager
    private TaskManager manager = Managers.getDefault();

    // передаём его в качестве аргумента в конструктор HttpTaskServer
    private HttpTaskServer taskServer;
    private Gson gson;

    public HttpTaskManagerHistoryTest() throws IOException {
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
    public void shouldReturnCorrectHistory() throws IOException, InterruptedException {
        var epic = new Epic(0, "Test 2", "");
        var epic2 = new Epic(0, "Test ash", "");
        Task task = new Task(0, "Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task(0, "Test 3", "Testing task 2",
                LocalDateTime.now().plusHours(1), Duration.ofMinutes(5));

        manager.addTask(task);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addEpic(epic2);

        manager.getTask(task.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic.getId());
        manager.getEpic(epic2.getId());
        manager.getTask(task2.getId());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        assertEquals(manager.getHistory().size(), gson.<List<Task>>fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType()).size());
    }
}