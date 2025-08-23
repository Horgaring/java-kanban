import task.InMemmoryTaskManager;
import task.TaskManger;
import task.history.HistoryManager;
import task.history.InMemmoryHistoryManager;

public class Managers {
    public static TaskManger getDefault() {
        return new InMemmoryTaskManager(getDefaultHistory());
   }

    public static HistoryManager getDefaultHistory() {
        return new InMemmoryHistoryManager();
    }
}
