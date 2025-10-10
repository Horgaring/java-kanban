package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    void clear();

    List<Task> getAllTasks();

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    Optional<Task> getTask(int id);

    Optional<SubTask> getSubTask(int id);

    Optional<Epic> getEpic(int id);

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
