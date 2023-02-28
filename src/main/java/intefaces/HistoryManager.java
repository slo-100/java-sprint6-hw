package main.java.intefaces;

import main.java.service.Node;
import main.java.tasks.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getCustomLinkedList();

    List<Task> getViewedTasks();

}
