package task;
import task.history.HistoryManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManger {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    private int idCounter;
    public HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.idCounter = 0;
        this.historyManager = historyManager;
    }

    public void clear() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    public ArrayList<Task> getAllTasks() {
        var res = new ArrayList<Task>(tasks.values());
        res.addAll(subTasks.values());
        res.addAll(epics.values());
        return res;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }
    
    public Task getTask(int id) {
        var task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    public Task getSubTask(int id) {
        var task = subTasks.get(id);
        historyManager.add(task);
        return task;
    }

    public Task getEpic(int id) {
        var task = epics.get(id);
        historyManager.add(task);
        return task;
    }

    public void addTask(Task task) {
        tasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
    }

    public void addEpic(Epic task) {
        epics.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
    }

    public void addSubTask(SubTask task) {
        subTasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
        var epic = epics.get(task.getParentTaskId());
        epic.addSubTask(task.getId());
        updateEpicStatus(epic);
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
        var oldSubTask = subTasks.get(task.getId());

        if (!epics.containsKey(task.getParentTaskId())) {
            System.out.println("Epic not found");
            return;
        }
        if (oldSubTask.getParentTaskId() != task.getParentTaskId()) {
            var oldEpic = epics.get(oldSubTask.getParentTaskId());
            oldEpic.removeSubTask(task.getId());
        }
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
        var epic = epics.get(epicId);
        epic.removeSubTask(epicId);
        updateEpicStatus(epics.get(epicId));
    }

    public void deleteEpic(Integer id) {
        var subTaskIds = epics.get(id).getSubTasks();
        for (int subTaskId : subTaskIds) {
            subTasks.remove(subTaskId);
        }
        epics.remove(id);
    }

    public ArrayList<SubTask> getSubTasksByEpicId(int epicId) {
        var epic = epics.get(epicId);
        var res = new ArrayList<SubTask>();

        for (var task : epic.getSubTasks()) {
            res.add(subTasks.get(task));
        }

        return res;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
