package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int parentTaskId;

    public SubTask(int id, String name, String description, int parentTaskId, LocalDateTime startTime, Duration duration) {
        super(id, name, description, startTime, duration);
        this.parentTaskId = parentTaskId;
    }

    public int getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(int parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", parentTaskId=" + parentTaskId +
                '}';
    }
}
