package task;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private int idCounter;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.idCounter = 0;
    }

    public void clear() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    public ArrayList<Task> getTasks() {
        var res = new ArrayList<Task>(tasks.values());
        res.addAll(subTasks.values());
        res.addAll(epics.values());
        return res;
    }
    
    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Task getSubTask(int id) {
        return subTasks.get(id);
    }

    public Task getEpic(int id) {
        return epics.get(id);
    }

    public int addTask(Task task) {
        tasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
        return task.getId();
    }

    public int addEpic(Epic task) {
        epics.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
        return task.getId();
    }

    public int addSubTask(SubTask task) {
        subTasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
        var epic = epics.get(task.getParentTaskId());
        epic.addSubTask(task.getId());
        updateEpicStatus(epic);
        return task.getId();
    }

    private void updateEpicStatus(Epic epic){
        boolean allDone = true;
        boolean allNew = true;
        var subTaskIds = epic.getSubTasks();

        for (int id : subTaskIds) {
            var subtask = subTasks.get(id);
            if (subtask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask task) {
        subTasks.put(task.getId(), task);
        updateEpicStatus(epics.get(task.getParentTaskId()));
    }

    public void updateEpic(Epic task) {
        epics.put(task.getId(), task);
    }

    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    public void deleteSubTask(Integer id) {
        var epicId = subTasks.get(id).getParentTaskId();
        subTasks.remove(id);
        epics.get(epicId).removeSubTask(id);
    }

    public void deleteEpic(Integer id) {
        var subTaskIds = epics.get(id).getSubTasks();
        for (int subTaskId : subTaskIds) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }
}
