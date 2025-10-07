package manager;

import exception.TimeIntervalConflictException;
import manager.history.HistoryManager;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks;
    private Map<Integer, SubTask> subTasks;
    private Map<Integer, Epic> epics;
    private int idCounter;
    private HistoryManager historyManager;
    private Set<Task> sortedTasks;
    private Set<SubTask> sortedSubTasks;
    private Set<Epic> sortedEpics;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        sortedTasks = new TreeSet<>();
        sortedSubTasks = new TreeSet<>();
        sortedEpics = new TreeSet<>();
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

    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    public Set<SubTask> getPrioritizedSubTasks() {
        return sortedSubTasks;
    }

    public Set<Epic> getPrioritizedEpics() {
        return sortedEpics;
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
    public Optional<Task> getTask(int id) {
        if (!tasks.containsKey(id)) {
            return Optional.empty();
        }
        var task = tasks.get(id);
        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public Optional<SubTask> getSubTask(int id) {
        if (!subTasks.containsKey(id)) {
            return Optional.empty();
        }
        var task = subTasks.get(id);
        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public Optional<Epic> getEpic(int id) {
        if (!epics.containsKey(id)) {
            return Optional.empty();
        }
        var task = epics.get(id);
        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public void addTask(Task task) throws TimeIntervalConflictException {
        var hasIntersection = getPrioritizedTasks().stream()
                .filter(p -> hasIntersectionBetween(task, p)).findFirst();
        if (hasIntersection.isPresent()) {
            throw new TimeIntervalConflictException(
                    String.format("Конфликт: задача '%s' пересекается с '%s'",
                            task.getName(), hasIntersection.get().getName())
            );
        }
        tasks.put(idCounter, task);
        task.setId(idCounter);
        sortedTasks.add(task);
        idCounter++;
    }

    @Override
    public void addEpic(Epic task) {
        epics.put(idCounter, task);
        task.setId(idCounter);
        sortedEpics.add(task);
        idCounter++;
    }

    @Override
    public void addSubTask(SubTask task) throws TimeIntervalConflictException {
        var hasIntersection = getPrioritizedSubTasks().stream()
                .filter(p -> hasIntersectionBetween(task, p)).findFirst();
        if (hasIntersection.isPresent()) {
            throw new TimeIntervalConflictException(
                    String.format("Конфликт: задача '%s' пересекается с '%s'",
                            task.getName(), hasIntersection.get().getName())
            );
        }
        subTasks.put(idCounter, task);
        task.setId(idCounter);
        idCounter++;
        var epic = epics.get(task.getParentTaskId());
        epic.addSubTask(task);
        sortedSubTasks.add(task);
        updateEpicStatus(epic);
    }

    public boolean hasIntersectionBetween(Task t1, Task t2) {
        return t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getStartTime())
                || t1.getStartTime().isBefore(t2.getEndTime()) && t1.getEndTime().isAfter(t2.getEndTime());
    }

    private void updateEpicStatus(Epic epic) {
        List<SubTask> epicSubTasks = epic.getSubTasks().stream()
                .map(id -> subTasks.get(id))  // Assumes subTasks is Map<Integer, SubTask>
                .filter(Objects::nonNull)     // Optional: Skip null subtasks to avoid NPEs
                .toList();

        if (epicSubTasks.isEmpty()) {
            // Edge case: No subtasks—keep as NEW or handle per your business rules.
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allDone = epicSubTasks.stream().allMatch(t -> t.getStatus() == TaskStatus.DONE);
        boolean allNew = epicSubTasks.stream().allMatch(t -> t.getStatus() == TaskStatus.NEW);

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
        epics.get(id).getSubTasks().stream()
                .forEach(subTaskId -> subTasks.remove(subTaskId));

        epics.remove(id);
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int epicId) {
        return epics
                .get(epicId)
                .getSubTasks()
                .stream()
                .map(t -> subTasks.get(t)).collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
