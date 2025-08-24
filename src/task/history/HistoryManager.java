package task.history;
import task.Task;
import java.util.List;

public interface HistoryManager {
    public List<Task> getHistory();

    public void add(Task task);
}
