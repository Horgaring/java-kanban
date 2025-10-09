package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasks;

    public Epic(int id, String name, String description) {
        super(id, name, description, LocalDateTime.now(), Duration.ZERO);
        this.subTasks = new ArrayList<>();
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask.getId());
    }

    public void removeSubTask(Integer subTask) {
        subTasks.remove(subTask);
    }


    public void setStartTimeAndDuration(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
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