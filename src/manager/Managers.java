package manager;

import com.google.gson.GsonBuilder;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

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

    public static GsonBuilder getDefaultGsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
    }

}
