import exception.TimeIntervalConflictException;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManger;

    @BeforeEach
    abstract void setUp() throws IOException;


    @Test
    void shouldEqualInProgressWhenOneOfTaskInProgress() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId(), LocalDateTime.now(), Duration.ofHours(1));
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(new SubTask(0, "", "", epic.getId(), LocalDateTime.now().plusHours(1), Duration.ofHours(1)));

        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManger.updateSubTask(subTask1);

        Assertions.assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldEqualInProgressWhenOneOfTaskDone() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId(), LocalDateTime.now(), Duration.ofHours(1));
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(new SubTask(0, "", "", epic.getId(), LocalDateTime.now().plusHours(1), Duration.ofHours(1)));

        subTask1.setStatus(TaskStatus.DONE);
        taskManger.updateSubTask(subTask1);

        Assertions.assertEquals(epic.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldEqualDoneWhenAllSubTasksAreDone() {
        var epic = new Epic(0, "", "");
        taskManger.addEpic(epic);
        var subTask1 = new SubTask(0, "", "", epic.getId(), LocalDateTime.now(), Duration.ofHours(1));
        var subTask2 = new SubTask(0, "", "", epic.getId(), LocalDateTime.now().plusHours(1), Duration.ofHours(1));
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
        var subTask1 = new SubTask(0, "", "", epic.getId(), LocalDateTime.now(), Duration.ofHours(1));
        var subTask2 = new SubTask(0, "", "", epic.getId(), LocalDateTime.now().plusHours(1), Duration.ofHours(2));
        taskManger.addSubTask(subTask1);
        taskManger.addSubTask(subTask2);

        taskManger.deleteEpic(epic.getId());

        Assertions.assertEquals(taskManger.getSubTasks().size(), 0);
    }

    @Test
    void shouldAddAndFindTaskById() {
        var task = new Task(0, "Test Task", "Description", LocalDateTime.now(), Duration.ofHours(1));
        taskManger.addTask(task);

        var savedTask = taskManger.getTask(task.getId());

        Assertions.assertNotNull(savedTask, "Задача должна быть найдена по id");
        var val = savedTask.get();
        Assertions.assertEquals("Test Task", val.getName());
        Assertions.assertEquals(TaskStatus.NEW, val.getStatus());
    }

    @Test
    void shouldAddAndFindEpicById() {
        var epic = new Epic(0, "Test Epic", "Epic Description");
        taskManger.addEpic(epic);

        Optional<Epic> savedEpic = taskManger.getEpic(epic.getId());

        Assertions.assertNotNull(savedEpic, "Эпик должен быть найден по id");
        var val = savedEpic.get();
        Assertions.assertEquals("Test Epic", val.getName());
        Assertions.assertTrue(val.getSubTasks().isEmpty(), "У нового эпика не должно быть подзадач");
    }

    @Test
    void shouldAddAndFindSubTaskById() {
        var epic = new Epic(0, "Parent Epic", "Epic for subtask");
        taskManger.addEpic(epic);

        var subTask = new SubTask(0, "Subtask", "Subtask Description", epic.getId(), LocalDateTime.now(), Duration.ofHours(1));
        taskManger.addSubTask(subTask);

        Optional<SubTask> savedSubTask = taskManger.getSubTask(subTask.getId());

        Assertions.assertTrue(savedSubTask.isPresent(), "Подзадача должна быть найдена по id");
        var val = savedSubTask.get();
        Assertions.assertEquals("Subtask", val.getName());
        Assertions.assertEquals(epic.getId(), val.getParentTaskId(), "ID эпика должен совпадать");
    }

    @Test
    void shouldReturnCorrectStartTimeAndEndTimeForEpic() {
        var epic = new Epic(0, "Parent Epic", "Epic for subtask");
        taskManger.addEpic(epic);

        var subTask = new SubTask(0, "Subtask", "Subtask Description", epic.getId(), LocalDateTime.of(2000, 5, 5, 6, 5), Duration.ofHours(1));
        var subTask2 = new SubTask(0, "Subtask", "Subtask Description", epic.getId(), LocalDateTime.of(2000, 5, 5, 7, 5), Duration.ofHours(2));
        var subTask3 = new SubTask(0, "Subtask", "Subtask Description", epic.getId(), LocalDateTime.of(2000, 5, 5, 1, 5), Duration.ofHours(3));
        taskManger.addSubTask(subTask);
        taskManger.addSubTask(subTask2);
        taskManger.addSubTask(subTask3);


        Assertions.assertEquals(LocalDateTime.of(2000, 5, 5, 1, 5), epic.getStartTime());
        Assertions.assertEquals(LocalDateTime.of(2000, 5, 5, 9, 5), epic.getEndTime());
        Assertions.assertEquals(8, epic.getDuration().toHours());
    }

    @Test
    void shouldThrowTimeIntervalConflictException() {
        Assertions.assertThrows(TimeIntervalConflictException.class, () -> {
            var Task = new Task(0, "Subtask", "Subtask Description", LocalDateTime.now(), Duration.ofHours(1));
            var Task2 = new Task(0, "Subtask", "Subtask Description", LocalDateTime.now(), Duration.ofHours(2));
            taskManger.addTask(Task);
            taskManger.addTask(Task2);
        }, "Пересечение двух задач");
    }
}
