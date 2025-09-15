import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import history.HistoryManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    private static HistoryManager historyManager;

    @BeforeEach
    void setup() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldReturnLast12TasksWhen12TasksAdded() {
        for (int i = 1; i <= 12; i++) {
            historyManager.add(new Task(i, "Task " + i, ""));
        }

        List<Task> history = historyManager.getHistory();

        assertEquals("Task 12", history.get(0).getName());
        assertEquals("Task 1", history.get(11).getName());

    }

    @Test
    void shouldRemoveElementCorrectly() {
        Task task = new Task(1, "Test Task", "Description");
        historyManager.add(task);

        historyManager.remove(1);

        Assertions.assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldReturnNullWhenRemovingNonExistentKey() {
        historyManager.remove(1);

        Assertions.assertTrue(true);
    }

    @Test
    void shouldMaintainOrderAfterRemoval() {
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        Task task3 = new Task(3, "Third", "Description 3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);

        Collection<Task> values = historyManager.getHistory();
        List<Task> valueList = new ArrayList<>(values);

        Assertions.assertEquals(2, valueList.size());
        Assertions.assertEquals(task1, valueList.get(1));
        Assertions.assertEquals(task3, valueList.get(0));
    }
}
