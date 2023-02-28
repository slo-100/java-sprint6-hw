package main.java.tasks;

import main.java.service.Status;
import main.java.service.TaskType;

import java.util.Objects;

public class Task {
    private int id;
    private TaskType taskType;
    private String name;
    private Status status;
    private String description;



    public Task(TaskType taskType, String name, Status status, String description) {
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, TaskType taskType, String name, Status status , String description) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "main.java.tasks.Task{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", status='" + status + '\'' + '}';
    }

    public String toCsvFormat() { // Напишите метод сохранения задачи в строку (ТЗ-6)
        String result;
        result = id + "," + taskType + "," + name + "," + status + "," + description;
        return result;
    }
}