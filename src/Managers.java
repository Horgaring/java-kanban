import history.HistoryManager;
import history.InMemoryHistoryManager;
import task.manager.FileBackedTaskManager;
import task.manager.InMemoryTaskManager;
import task.manager.TaskManager;

import java.nio.file.Path;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getFileBacked(Path path) {
        return new FileBackedTaskManager(getDefaultHistory(), path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
