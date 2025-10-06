package task.manager;

import history.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, SubTask> subTasks;
    private Map<Integer, Epic> epics;
    private int idCounter;
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.idCounter = 0;
        this.historyManager = historyManager;
    }

    @Override
    public void clear() {
        tasks.clear();
        subTasks.clear();
        epics.clear();
    }

    @Override
    public List<Task> getAllTasks() {
        var res = new ArrayList<>(tasks.values());
        res.addAll(subTasks.values());
        res.addAll(epics.values());
        return res;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(int id) {
        var task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Task getSubTask(int id) {
        var task = subTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Task getEpic(int id) {
        var task = epics.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void addTask(Task task) {
        tasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
    }

    @Override
    public void addEpic(Epic task) {
        epics.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
    }

    @Override
    public void addSubTask(SubTask task) {
        subTasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
        var epic = epics.get(task.getParentTaskId());
        epic.addSubTask(task.getId());
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
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

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
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

    @Override
    public void updateEpic(Epic task) {
        epics.put(task.getId(), task);
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        var epicId = subTasks.get(id).getParentTaskId();
        subTasks.remove(id);
        var epic = epics.get(epicId);
        epic.removeSubTask(epicId);
        updateEpicStatus(epics.get(epicId));
    }

    @Override
    public void deleteEpic(Integer id) {
        var subTaskIds = epics.get(id).getSubTasks();

        for (int subTaskId : subTaskIds) {
            subTasks.remove(subTaskId);
        }

        epics.remove(id);
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        var epic = epics.get(epicId);
        var res = new ArrayList<SubTask>();

        for (var task : epic.getSubTasks()) {
            res.add(subTasks.get(task));
        }

        return res;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
