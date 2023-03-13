package main.java.intefaces;

import main.java.service.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public interface TaskManager {
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void taskClean();

    void epicClean();

    void subtaskClean();

    Task getTaskById(UUID taskId);

    Epic getEpicById(UUID taskId);

    Subtask getSubtaskById(UUID taskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskById(UUID id);

    void removeEpicById(UUID id);

    void removeSubtaskById(UUID id);

    void changeStatusTask(UUID id, Status status);

    void changeStatusSubtask(UUID id, Status status);

    List<UUID> getSubtaskList(UUID epicId);

    void updateEpicStatus(UUID epicId);

    List<Task> getHistoryList();

    HistoryManager getHistoryManager();

}