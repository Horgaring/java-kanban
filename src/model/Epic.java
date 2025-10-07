package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks;
    private LocalDateTime endTime;

    public Epic(int id, String name, String description) {
        super(id, name, description, LocalDateTime.now(), Duration.ZERO);
        this.subTasks = new ArrayList<>();
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask.getId());
        updateTimes(subTask);
    }

    public void removeSubTask(Integer subTask) {
        subTasks.remove(subTask);
    }

    private void updateTimes(SubTask newSubTask) {
        if (startTime.isAfter(newSubTask.getStartTime())) {
            startTime = newSubTask.getStartTime();
        }
        if (endTime == null) {
            endTime = newSubTask.getEndTime();
        } else if (endTime.isBefore(newSubTask.getEndTime())) {
            endTime = newSubTask.getEndTime();
        }
        duration = Duration.between(startTime, endTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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