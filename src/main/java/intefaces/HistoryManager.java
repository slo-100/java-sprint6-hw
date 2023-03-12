package main.java.intefaces;

import main.java.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(String id);

    List<Task> getCustomLinkedList();

    List<Task> getViewedTasks();

}
