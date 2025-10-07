import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;

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
