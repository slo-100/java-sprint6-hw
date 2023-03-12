package main.java.managers;

import main.java.intefaces.HistoryManager;
import main.java.service.Node;
import main.java.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>(); // класс с нодами
    private List<Task> viewedTasks = new ArrayList<>(); // ТЗ-6

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
        if (customLinkedList.tasksMap.containsKey(task.getId())) {
            customLinkedList.removeNode(customLinkedList.tasksMap.get(task.getId()));
        }
        customLinkedList.tasksMap.put(task.getId(), customLinkedList.tail);
        viewedTasks.add(task); // ТЗ - 6.1
    }

    @Override
    public ArrayList<Task> getCustomLinkedList() {
        return customLinkedList.getTasksByNodes();
    }

    @Override
    public void remove(String id) {
        if (customLinkedList.tasksMap.containsKey(id)) {
            customLinkedList.removeNode(customLinkedList.tasksMap.get(id));
            customLinkedList.tasksMap.remove(id);
        }
    }

    @Override
    public List<Task> getViewedTasks() { // ТЗ-6
            return viewedTasks;
    }

}

class CustomLinkedList<Task> {
    Map<String, Node<Task>> tasksMap = new HashMap<>();
    private Node<Task> head;
    protected Node<Task> tail;
    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    public ArrayList<Task> getTasksByNodes() {
        final ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.get());
            head = head.next;
            current = head;
        }
        return tasks;
    }

    public void removeNode(Node node) {
        final Node<Task> next = node.next;
        final Node<Task> prev = node.prev;

        // Если в списке всего один элемент и мы его удаляем, то хвост и голова должны стать null
        if (next == null && prev == null) {
            head = null;
            tail = null;
        } else {

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
        }
    }


}