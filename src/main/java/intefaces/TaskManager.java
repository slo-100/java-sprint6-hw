package main.java.intefaces;

import main.java.service.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.LinkedList;
import java.util.List;

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

    Task getTaskById(String taskId);

    Epic getEpicById(String taskId);

    Subtask getSubtaskById(String taskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskById(String id);

    void removeEpicById(String id);

    void removeSubtaskById(String id);

    void changeStatusTask(String id, Status status);

    void changeStatusSubtask(String id, Status status);

    List<String> getSubtaskList(String epicId);

    void updateEpicStatus(String epicId);

    LinkedList<Task> getHistoryList();

    HistoryManager getHistoryManager();

}
