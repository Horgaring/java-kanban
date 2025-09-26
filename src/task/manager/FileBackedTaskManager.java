package task.manager;

import history.HistoryManager;
import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path path;

    public FileBackedTaskManager(HistoryManager historyManager, Path file) {
        super(historyManager);
        this.path = file;
    }

    public static FileBackedTaskManager loadFromFile(Path file, HistoryManager historyManager) {
        var manager = new FileBackedTaskManager(historyManager, file);
        try (var reader = Files.newBufferedReader(file, Charset.defaultCharset())) {
            while (reader.ready()) {
                var line = reader.readLine();
                var split = line.split(",");
                if (split[0].equals("id")) {
                    continue;
                }

                switch (split[1]) {
                    case "TASK" -> {
                        var task = new Task(Integer.parseInt(split[0]), split[2], split[4]);
                        task.setStatus(TaskStatus.valueOf(split[3]));
                        manager.addTask(task);
                    }
                    case "SUBTASK" -> {
                        var task = new SubTask(Integer.parseInt(split[0]), split[2], split[4], Integer.parseInt(split[5]));
                        task.setStatus(TaskStatus.valueOf(split[3]));
                        manager.addSubTask(task);
                    }
                    case "EPIC" -> {
                        var task = new Epic(Integer.parseInt(split[0]), split[2], split[4]);
                        task.setStatus(TaskStatus.valueOf(split[3]));
                        manager.addEpic(task);
                    }
                }
            }
            return manager;

        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public Path getPath() {
        return path;
    }

    private void save() {
        try (var writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE)) {
            writer.write("id,type,name,status,description,epic\n");

            for (var task : super.getTasks()) {
                writer.write(task.getId() + "," +
                        "TASK" + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + "," + "\n");
            }

            for (var task : super.getEpics()) {
                writer.write(task.getId() + "," +
                        "EPIC" + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + "," + "\n");
            }

            for (var task : super.getSubTasks()) {
                writer.write(task.getId() + "," +
                        "SUBTASK" + "," +
                        task.getName() + "," +
                        task.getStatus() + "," +
                        task.getDescription() + "," +
                        task.getParentTaskId() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }


    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic task) {
        super.addEpic(task);
        save();
    }

    @Override
    public void deleteSubTask(Integer subtask) {
        super.deleteSubTask(subtask);
        save();
    }

    @Override
    public void deleteTask(Integer task) {
        super.deleteTask(task);
        save();
    }

    @Override
    public void deleteEpic(Integer task) {
        super.deleteEpic(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic task) {
        super.updateEpic(task);
        save();
    }


}
