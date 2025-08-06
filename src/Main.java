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
        var epic = new Epic(5, "Epic 1", "Description 5", new ArrayList<>(Arrays.asList(subTask1, subTask2)));

        var subTask3 = new SubTask(6, "SubTask 3", "Description 6", 5);
        var epic2 = new Epic(7, "Epic 2", "Description 7", new ArrayList<>(Arrays.asList(subTask3)));

        var task1Id = taskManager.addTask(task1);
        var task2Id = taskManager.addTask(task2);
        var subTask1Id = taskManager.addTask(subTask1);
        var subTask2Id = taskManager.addTask(subTask2);
        var epicId = taskManager.addTask(epic);
        var epic2Id = taskManager.addTask(epic2);
        var subTask3Id = taskManager.addTask(subTask3);

        System.out.println("--------------------------------");
        System.out.println(taskManager.getTasks());
        System.out.println("--------------------------------");

        taskManager.ChangeStatus(task1Id, TaskStatus.IN_PROGRESS);
        taskManager.ChangeStatus(subTask1Id, TaskStatus.DONE);
        taskManager.ChangeStatus(subTask3Id, TaskStatus.DONE);

        System.out.println(taskManager.getTasks());
        System.out.println("--------------------------------");

        taskManager.deleteTask(task1Id);
        taskManager.deleteTask(epic2Id);

        System.out.println(taskManager.getTasks());
        System.out.println("--------------------------------");

    }

  
   
}
