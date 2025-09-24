import history.HistoryManager;
import history.InMemoryHistoryManager;
import task.manager.FileBackedTaskManager;
import task.manager.InMemoryTaskManager;
import task.manager.TaskManager;

import java.io.IOException;
import java.nio.file.Files;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getFileBackend() throws IOException {
        return new FileBackedTaskManager(getDefaultHistory(), Files.createTempFile("tasks", ".csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
