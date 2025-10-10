import manager.FileBackedTaskManager;
import model.Epic;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    @BeforeEach
    void setUp() throws IOException {
        taskManger = (FileBackedTaskManager) Managers.getFileBacked(Files.createTempFile("tasks", ".csv"));
    }


    @Test
    void shouldSerializeAndDeserializeManager() {
        var epic = new Epic(0, "Parent Epic", "Epic for subtask");
        taskManger.addEpic(epic);

        var subTask = new SubTask(0, "Subtask", "Subtask Description", epic.getId(), LocalDateTime.now(), Duration.ofHours(1));
        taskManger.addSubTask(subTask);

        var newManager = FileBackedTaskManager.loadFromFile(taskManger.getPath(), Managers.getDefaultHistory());

        Assertions.assertEquals(newManager.getEpic(epic.getId()), taskManger.getEpic(epic.getId()));
        Assertions.assertEquals(newManager.getSubTask(subTask.getId()), taskManger.getSubTask(subTask.getId()));
    }
}
