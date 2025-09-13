import task.InMemoryTaskManager;
import task.TaskManger;
import history.HistoryManager;
import history.InMemoryHistoryManager;

public class Managers {
    public static TaskManger getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
