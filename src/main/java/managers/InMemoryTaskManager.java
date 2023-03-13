package main.java.managers;

import main.java.intefaces.HistoryManager;
import main.java.intefaces.TaskManager;
import main.java.service.Status;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<UUID, Task> tasks = new HashMap<>();
    private HashMap<UUID, Epic> epics = new HashMap<>();
    private HashMap<UUID, Subtask> subtasks = new HashMap<>();

    public HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void addTask(Task task) {
        task.setId(UUID.randomUUID());
        tasks.put(task.getId(), task);
        System.out.println("Задача успешно добавлена");
    }

    @Override
    public void addEpic(Epic epic) {
//        epic.setId(UUID.randomUUID()); ДЛЯ ПРОВЕРКИ ТЗ-6
        epics.put(epic.getId(), epic);
        System.out.println("Епик успешно добавлен");
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(UUID.randomUUID());
        if (epics.containsKey(subtask.getEpicId())) { // true, если был добавлен эпик // переделать
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).getSubtasksList().add(subtask.getId()); // добавляет id подзадачи в
            // список эпика
            System.out.println("Подзадача успешно добавлена");
            updateEpicStatus(epics.get(subtask.getEpicId()).getId()); // обновляет статус эпика
        } else {
            System.out.println("Сначала нужно добавить Эпик");
        }
    }

    // case 2: Получение списка всех задач.-------------------------------------
    @Override
    public List<Task> getTasks() {
        List<Task> list = tasks.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> list = epics.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> list = subtasks.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        return list;
    }

    // case 3: Удаление всех задач.---------------------------------------
    @Override
    public void taskClean() {
        tasks.clear();
        System.out.println("Удаление задач выполнено");
    }

    @Override
    public void epicClean() {
        subtasks.clear();
        epics.clear();
        System.out.println("Удаление эпиков выполнено");
    }

    @Override
    public void subtaskClean() {
        int subtaskCleanCounter = 0;
        subtasks.clear();

        epics.values().stream().forEach( epic -> epic.cleanSubtaskIds());
        subtaskCleanCounter++;

        if (epics.size() == subtaskCleanCounter) {
            System.out.println("Удаление задач выполнено");
        }
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic.getId()); // "Нужно обновить статусы эпиков"
            System.out.println("Обновление статуса эпиков выполнено");
        }
    }

    // ТЗ-4
    // case 4:get методы-------------------------------------------------------------
    public Task getTaskById(UUID idInput) {
        Task task = null;
        if (tasks.get(idInput) != null) {
            historyManager.add(tasks.get(idInput));
            task = tasks.get(idInput);
        }
        return task;
    }

    public Epic getEpicById(UUID idInput) {
        Epic epic = null;
        if (epics.get(idInput) != null) {
            historyManager.add(epics.get(idInput));
            epic = epics.get(idInput);
        }
        return epic;
    }

    public Subtask getSubtaskById(UUID idInput) {
        Subtask subtask = null;
        if (subtasks.get(idInput) != null) {
            historyManager.add(subtasks.get(idInput));
            subtask = subtasks.get(idInput);
        }
        return subtask;
    }


    // case 5: Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
// "Это значит что в объекте Task заполнено поле id и мы можем его использовать для обновления объекта. поэтому во всех трёх методах должен на вход подаваться только объект задачи"
    @Override
    public void updateTask(Task task) {

        for (Task t : tasks.values()) {             // итерация мапы
            if (t.getId() == task.getId()) {            // "перед обновлением, надо проверить, что объект с заданным id есть в соответствующей мапе."
                tasks.put(t.getId(), task);             // добавляем в мапу новую задачу с тем же id
                System.out.println("Обновление задачи прошло успешно");
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        final UUID id = epic.getId();
        for (Epic e : epics.values()) {
            if (e.getId() == id) {
                epics.put(e.getId(), epic);
                System.out.println("Обновление прошло успешно");
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        final UUID id = subtask.getId();
        final UUID epicId = subtask.getEpicId();
        if (epics.containsKey(epicId) && subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
            System.out.println("Обновление прошло успешно");
            updateEpicStatus(epicId);
        }
    }

    // case 6: Удалить по идентификатору. ----------------------------------------
    @Override
    public void removeTaskById(UUID id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
            System.out.println("Задача удалена");
        }
    }

    @Override
    public void removeEpicById(UUID id) {
        List<UUID> list;
        if (epics.containsKey(id)) {
            list = epics.get(id).getSubtasksList();
            for (Subtask subtask : subtasks.values()) {
                for (UUID subtaskId : list) {
                    if (subtask.getId() == subtaskId) {
                        subtasks.remove(subtask.getEpicId()); // то удаляем
                    }
                }
            }
            epics.remove(id);
            historyManager.remove(id);
            System.out.println("Эпик удалён");
        }

    }

    @Override
    public void removeSubtaskById(UUID id) {
        if (epics.containsKey(subtasks.get(id).getEpicId())) {
            epics.get(subtasks.get(id).getEpicId()).removeSubtask(id);
            System.out.println("Удаление прошло успешно");
            updateEpicStatus(subtasks.get(id).getEpicId());
        }
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
            historyManager.remove(id);
            System.out.println("Подзадача удалена");
        }
    }

    // case 7: Изменить статус --------------------------------------------------
    @Override
    public void changeStatusTask(UUID id, Status status) {
        if (tasks.containsKey(id)) {
            tasks.get(id).setStatus(status);
            System.out.println("Статус изменён");
        } else {
            System.out.println("Задача с таким идентификатором не найдена");
        }
    }

    @Override
    public void changeStatusSubtask(UUID id, Status status) {
        if (subtasks.containsKey(id)) {
            subtasks.get(id).setStatus(status);
            System.out.println("Статус изменён");
            updateEpicStatus(subtasks.get(id).getEpicId());
        } else {
            System.out.println("Подзадача с таким идентификатором не найдена");
        }
    }

    // case 8: Получение списка всех подзадач определённого эпика. -----------------------------
    @Override
    public List<UUID> getSubtaskList(UUID epicId) {
        for (UUID id : epics.keySet()) {
            if (epics.get(id).getId() == epicId) {
                return epics.get(id).getSubtasksList();
            }
        }
        return null;
    }

    // метод обновления статуса епика
    @Override
    public void updateEpicStatus(UUID id) {
        if (epics.containsKey(id)) {
            boolean inProgress = true;
            // если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
            // *если у эпика нет подзадач
            if (epics.get(id).getSubtasksList().size() == 0) {
                epics.get(id).setStatus(Status.NEW);
                inProgress = false;
            }
            // *или все они имеют статус NEW
            if (epics.get(id).getSubtasksList().size() != 0) { // проверить на ноль
                int counterNew = 0;
                for (int i = 0; i < epics.get(id).getSubtasksList().size(); i++) { // итерация листа с id подзадач
                    if (subtasks.get(epics.get(id).getSubtasksList().get(i)).getStatus().equals(Status.NEW)) {
                        counterNew++;
                    }
                }
                if (epics.get(id).getSubtasksList().size() == counterNew) { // если 0? то значит новая
                    epics.get(id).setStatus(Status.NEW);
                    inProgress = false;
                }
            }

            // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
            if (epics.get(id).getSubtasksList().size() != 0) { // проверить на ноль
                int counterDone = 0;
                for (int i = 0; i < epics.get(id).getSubtasksList().size(); i++) { // перебор листа с id подзадач
                    if (subtasks.get(epics.get(id).getSubtasksList().get(i)).getStatus().equals(Status.DONE)) {
                        counterDone++;
                    }
                }
                if (epics.get(id).getSubtasksList().size() == counterDone) {
                    epics.get(id).setStatus(Status.DONE);
                    inProgress = false;
                }

                // во всех остальных случаях статус должен быть IN_PROGRESS
                if (inProgress) {
                    epics.get(id).setStatus(Status.IN_PROGRESS);
                }
                epics.put(epics.get(id).getId(), epics.get(id));
                System.out.println("Обновление списка эпика прошло успешно");
            }
        } else {
            System.out.println("Епик с таким id не найден");
        }
    }

    // case 9:
    public List<Task> getHistoryList() {
        return historyManager.getCustomLinkedList();
    }

}