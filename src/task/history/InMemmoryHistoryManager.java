package task.history;

import task.Task;

public class InMemmoryHistoryManager implements HistoryManager {
    private Task[] history;

    public InMemmoryHistoryManager() {
        history = new Task[10];
    }

    @Override
    public Task[] getHistory() {
        return history;
    }

    @Override
    public void add(Task task) {
        var temp = history[0];
        history[0] = task;
        for (int i = 1; i < history.length; i++) {
            var tmp = history[i];
            history[i] = temp;
            temp = tmp;
        }
    }
}
