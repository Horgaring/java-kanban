import java.util.ArrayList;
import java.util.Arrays;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskManager;
import task.TaskStatus;

public class Main {

    public static void main(String[] args) {
        var taskManager = new TaskManager();
        var task1 = new Task(1, "Task 1", "Description 1");
        var task2 = new Task(2, "Task 2", "Description 2");

        var subTask1 = new SubTask(3, "SubTask 1", "Description 3", 1);
        var subTask2 = new SubTask(4, "SubTask 2", "Description 4", 1);
        var epic = new Epic(5, "Epic 1", "Description 5");

        var subTask3 = new SubTask(6, "SubTask 3", "Description 6", 5);
        var epic2 = new Epic(7, "Epic 2", "Description 7");

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        task1 = taskManager.getTasks().getFirst();
        task2 = taskManager.getTasks().getLast();
        epic = taskManager.getEpics().getFirst();
        epic2 = taskManager.getEpics().getLast();
        subTask1.setParentTaskId(epic.getId());
        subTask2.setParentTaskId(epic.getId());
        subTask3.setParentTaskId(epic2.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        subTask1 = taskManager.getSubTasks().getFirst();
        subTask2 = taskManager.getSubTasks().get(1);
        subTask3 = taskManager.getSubTasks().get(2);

        System.out.println("--------------------------------");
        System.out.println(taskManager.getAllTasks());
        System.out.println("--------------------------------");

        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);

        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);

        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println("--------------------------------");

        taskManager.deleteTask(task1.getId());
        taskManager.deleteEpic(epic.getId());

        System.out.println(taskManager.getAllTasks());
        System.out.println("--------------------------------");

    }

  
   
}
