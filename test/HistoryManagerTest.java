import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Task;
import task.history.HistoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    private static HistoryManager historyManager;

    @BeforeAll
    static void setup() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldReturnLast10TasksWhen12TasksAdded() {
        for (int i = 1; i <= 12; i++) {
            historyManager.add(new Task(i, "Task " + i, ""));
        }

        Task[] history = historyManager.getHistory();

        assertEquals("Task 12", history[0].getName());
        assertEquals("Task 3", history[9].getName());

    }
}
