import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTest {
    @Test
    void shouldReturnTrueWhenTasksHaveSameId() {
        var task1 = new Task(1, "Task1", "Task2", LocalDateTime.now(), Duration.ofHours(1));
        var task2 = new Task(1, "Task1", "Task6", LocalDateTime.now(), Duration.ofHours(2));

        Assertions.assertEquals(task1, task2);
    }
}
