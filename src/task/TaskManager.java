package task;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private int idCounter;
    private ArrayList<Integer> deletedIds;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.idCounter = 0;
        this.deletedIds = new ArrayList<>();
    }

    public void clear() {
        tasks.clear();
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }
    
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public int addTask(Task task) {
        if (deletedIds.size() > 0) {
            tasks.put(deletedIds.get(0), task);
            task.setId(deletedIds.get(0));
            deletedIds.remove(0);
        } else {
            tasks.put(idCounter, task);
            task.setId(idCounter);
            idCounter++;
        }
        return task.getId();
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        if (tasks.get(id).getClass() == Epic.class) {
            for (SubTask subTask : ((Epic)tasks.get(id)).getSubTasks()) {
                deleteTask(subTask.getId());
                deletedIds.add(subTask.getId());
            }
        }
        tasks.remove(id);
        deletedIds.add(id);
    }

    public ArrayList<SubTask> getSubTasks(int id) {
        return ((Epic)tasks.get(id)).getSubTasks();
    }

    public void ChangeStatus(int id, TaskStatus status) {
        if (tasks.get(id).getClass() == Epic.class) {
            throw new IllegalArgumentException("Epic cannot be changed directly");
        } else {
            tasks.get(id).setStatus(status);
            if (tasks.get(id).getClass() == SubTask.class) {
                var epic = (Epic)tasks.get(((SubTask)tasks.get(id)).getParentTaskId());
                epic.UpdateStatus();
            }
        }
    }
}
