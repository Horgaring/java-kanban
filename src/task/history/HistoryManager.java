package task.history;

import task.Task;

public interface HistoryManager {
    public Task[] getHistory();

    public void add(Task task);
}
