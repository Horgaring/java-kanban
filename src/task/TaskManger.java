package task;

import java.util.ArrayList;

public interface TaskManger {
    public void clear();

    public ArrayList<Task> getAllTasks();

    public ArrayList<Task> getTasks();

    public ArrayList<SubTask> getSubTasks();

    public ArrayList<Epic> getEpics();

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

    public ArrayList<SubTask> getSubTasksByEpicId(int epicId);
}
