package task;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(int id, String name, String description, ArrayList<SubTask> subTasks) {
        super(id, name, description);
        this.subTasks = subTasks;
        UpdateStatus();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (SubTask subTask : subTasks) {
            subTask.setParentTaskId(id);
        }
    }

    public void UpdateStatus() {
        var status = TaskStatus.NEW;
        for (int i = 0; i< subTasks.size(); i++) {
            if (subTasks.get(i).getStatus() == TaskStatus.IN_PROGRESS) {
                status = TaskStatus.IN_PROGRESS;
                break;
            }
            if (subTasks.get(i).getStatus() != status) {
                if (i == 0) {
                    status = subTasks.get(i).getStatus();
                } else {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
            }
        }
        this.setStatus(status);
    }

    @Override
    public String toString() {
        ArrayList<Integer> subTasksIds = new ArrayList<>();
        for (SubTask subTask : getSubTasks()) {
            subTasksIds.add(subTask.getId());
        }
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasks=" + subTasksIds +
                '}';
    }
}