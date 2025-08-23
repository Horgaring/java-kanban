import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;

public class TaskTest {
    @Test
    void shouldReturnTrueWhenTasksHaveSameId() {
        var task1 = new Task(1, "Task1", "Task2");
        var task2 = new Task(1, "Task1", "Task6");

        Assertions.assertEquals(task1, task2);
    }
}
