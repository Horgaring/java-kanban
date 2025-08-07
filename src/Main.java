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

        var task1Id = taskManager.addTask(task1);
        var task2Id = taskManager.addTask(task2);
        var epicId = taskManager.addEpic(epic);
        var epic2Id = taskManager.addEpic(epic2);
        subTask1.setParentTaskId(epicId);
        subTask2.setParentTaskId(epicId);
        subTask3.setParentTaskId(epic2Id);
        var subTask1Id = taskManager.addSubTask(subTask1);
        var subTask2Id = taskManager.addSubTask(subTask2);
        var subTask3Id = taskManager.addSubTask(subTask3);

        System.out.println("--------------------------------");
        System.out.println(taskManager.getTasks());
        System.out.println("--------------------------------");

        task1.setId(task1Id);
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);

        subTask1.setId(subTask1Id);
        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setId(subTask2Id);
        subTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);

        subTask3.setId(subTask3Id);
        subTask3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask3);

        System.out.println(taskManager.getTasks());
        System.out.println("--------------------------------");

        taskManager.deleteTask(task1Id);
        taskManager.deleteEpic(epic2Id);

        System.out.println(taskManager.getTasks());
        System.out.println("--------------------------------");

    }

  
   
}
