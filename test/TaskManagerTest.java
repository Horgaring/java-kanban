import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;
import task.history.HistoryManager;

public class TaskManagerTest {
    private static TaskManger taskManger;

    @BeforeEach
    void setup() {
        taskManger = Managers.getDefault();
    }

    @Test
    void shouldEqualInProgressWhenOneOfTaskInProgress() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId());
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(new SubTask(0, "", "", epic.getId()));

        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManger.updateSubTask(subTask1);

        Assertions.assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldEqualInProgressWhenOneOfTaskDone() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId());
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(new SubTask(0, "", "", epic.getId()));

        subTask1.setStatus(TaskStatus.DONE);
        taskManger.updateSubTask(subTask1);

        Assertions.assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldEqualDoneWhenAllSubTasksAreDone() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId());
        var subTask2 = new SubTask(0, "", "", epic.getId());
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(subTask2);

        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        taskManger.updateSubTask(subTask1);
        taskManger.updateSubTask(subTask1);

        Assertions.assertEquals(epic.getStatus(), TaskStatus.DONE);
    }

    @Test
    void shouldRemoveAllSubtasksWhenEpicIsRemoved() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId());
        var subTask2 = new SubTask(0, "", "", epic.getId());
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(subTask2);

        taskManger.deleteEpic(epic.getId());

        Assertions.assertEquals(taskManger.getSubTasks().size(), 0);
    }

    @Test
    void shouldAddAndFindTaskById() {
        var task = new Task(0, "Test Task", "Description");
        taskManger.addTask(task);

        var savedTask = taskManger.getTask(task.getId());

        Assertions.assertNotNull(savedTask, "Задача должна быть найдена по id");
        Assertions.assertEquals("Test Task", savedTask.getName());
        Assertions.assertEquals(TaskStatus.NEW, savedTask.getStatus());
    }

    @Test
    void shouldAddAndFindEpicById() {
        var epic = new Epic(0, "Test Epic", "Epic Description");
        taskManger.addEpic(epic);

        Epic savedEpic = (Epic) taskManger.getEpic(epic.getId());

        Assertions.assertNotNull(savedEpic, "Эпик должен быть найден по id");
        Assertions.assertEquals("Test Epic", savedEpic.getName());
        Assertions.assertTrue(savedEpic.getSubTasks().isEmpty(), "У нового эпика не должно быть подзадач");
    }

    @Test
    void shouldAddAndFindSubTaskById() {
        var epic = new Epic(0, "Parent Epic", "Epic for subtask");
        taskManger.addEpic(epic);

        var subTask = new SubTask(0, "Subtask", "Subtask Description", epic.getId());
        taskManger.addSubTask(subTask);

        SubTask savedSubTask = (SubTask)taskManger.getSubTask(subTask.getId());

        Assertions.assertNotNull(savedSubTask, "Подзадача должна быть найдена по id");
        Assertions.assertEquals("Subtask", savedSubTask.getName());
        Assertions.assertEquals(epic.getId(), savedSubTask.getParentTaskId(), "ID эпика должен совпадать");
    }
}
