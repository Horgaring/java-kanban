package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.subTasks = new ArrayList<>();
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(int subTask) {
        subTasks.add(subTask);
    }

    public void removeSubTask(Integer subTask) {
        subTasks.remove(subTask);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasks=" + getSubTasks() +
                '}';
    }
}