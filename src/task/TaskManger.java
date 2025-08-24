package task;

import java.util.List;

public interface TaskManger {
    public void clear();

    public List<Task> getAllTasks();

    public List<Task> getTasks();

    public List<SubTask> getSubTasks();

    public List<Epic> getEpics();

    public Task getTask(int id);

    public Task getSubTask(int id);

    public Task getEpic(int id);

    public void addTask(Task task);

    public void addEpic(Epic task);

    public void addSubTask(SubTask task);

    public void updateTask(Task task);

    public void updateSubTask(SubTask task);

    public void updateEpic(Epic task);

    public void deleteTask(Integer id);

    public void deleteSubTask(Integer id);

    public void deleteEpic(Integer id);

    public List<SubTask> getSubTasksByEpicId(int epicId);

    public List<Task> getHistory();
}
