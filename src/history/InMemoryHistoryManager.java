package history;

import task.Task;
import utils.collection.MyLinkedHashMap;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final MyLinkedHashMap<Integer, Task> history;

    public InMemoryHistoryManager() {
        history = new MyLinkedHashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.reverseValues());
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        history.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }
}
