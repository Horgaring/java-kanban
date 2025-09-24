package task.manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    void clear();

    List<Task> getAllTasks();

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    Task getTask(int id);

    Task getSubTask(int id);

    Task getEpic(int id);

    void addTask(Task task);

    void addEpic(Epic task);

    void addSubTask(SubTask task);

    void updateTask(Task task);

    void updateSubTask(SubTask task);

    void updateEpic(Epic task);

    void deleteTask(Integer id);

    void deleteSubTask(Integer id);

    void deleteEpic(Integer id);

    List<SubTask> getSubTasksByEpicId(int epicId);

    List<Task> getHistory();
}
