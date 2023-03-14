package main.java.managers;

import main.java.intefaces.HistoryManager;
import main.java.service.ManagerSaveException;
import main.java.service.Status;
import main.java.service.TaskType;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String sep = File.separator;
    private static final String saveTasksFilePath = String.join(sep, "src", "main", "java", "saves", "taskSaves" + ".csv");
    private static File file = new File(saveTasksFilePath);

    // +++++++++++++++++++++++++++++++++++++++ TEST BLOCK +++++++++++++++++++++++++++++++++++++++
    public static void main(String[] args) throws IOException {

// - создать менеджер1 через конструктор, передав в него файл,
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

// - создать несколько задач, добавить из в менеджер, вызвать - что бы заполнилась история,

        Task task1 = new Task(UUID.randomUUID(), TaskType.TASK, "Переезд", Status.NEW, "Собрать коробки");
        Task task2 = new Task(UUID.randomUUID(), TaskType.TASK, "Переезд", Status.NEW, "Упаковать кошку");
        Epic epic1 = new Epic(UUID.randomUUID(), TaskType.EPIC, "Переезд", Status.NEW, "Переезд", new ArrayList<>());
        Subtask subtask1 = new Subtask(TaskType.SUBTASK, "тест1", Status.NEW, "Собрать коробки", epic1.getId());
        Subtask subtask2 = new Subtask(TaskType.SUBTASK, "тест2", Status.NEW, "Упаковать кошку", epic1.getId());

//        fileBackedTasksManager.addTask(task1);
//        fileBackedTasksManager.addTask(task2);
//        fileBackedTasksManager.addEpic(epic1);
//        fileBackedTasksManager.addSubtask(subtask1);
//        fileBackedTasksManager.addSubtask(subtask2);
//
//        fileBackedTasksManager.getTaskById(task1.getId());
//        fileBackedTasksManager.getTaskById(task2.getId());
//        fileBackedTasksManager.getEpicById(epic1.getId());
//        fileBackedTasksManager.getSubtaskById(subtask1.getId());
//        fileBackedTasksManager.getSubtaskById(subtask2.getId());
//
//        System.out.println("- вывести в консоль все задачи и историю менеджера 1");
//        fileBackedTasksManager.historyManager.getCustomLinkedList().stream().forEach(System.out::println);
//
//        fileBackedTasksManager.historyManager.getCustomLinkedList().stream().forEach(task -> System.out.println(task.getId()));
//
//        fileBackedTasksManager.save(fileBackedTasksManager.historyManager);
//        System.out.println();

// - при помощи метода loadFromFile, используя файл из 1 менеджера, создать менеджер2
// Как понимаю чтобы корректно работало надо закомментировать код выше и запустить
        FileBackedTasksManager fileBackedTasksManager2 = loadFromFile(fileBackedTasksManager.file);

        System.out.println("- вывести в консоль все задачаи и историю менеджера2");
        fileBackedTasksManager2.fromString().stream().forEach(System.out::println);
        System.out.println("История просмотров:");
        fileBackedTasksManager2.readHistoryFromFile().stream().forEach(System.out::println);
    }
    // +++++++++++++++++++++++++++++++++++++++ TEST BLOCK +++++++++++++++++++++++++++++++++++++++

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private static FileBackedTasksManager loadFromFile(File file) {
        var manager = new FileBackedTasksManager(file);
        return manager;
    }

    // метод возвращает последнюю строку с просмотренными задачами из файла
    private static List<String> historyFromString(String file) throws IOException {
        BufferedReader br;
        List<String> listString = new ArrayList<>();
        br = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                line = br.readLine();
                break;
            }
        }
        if (line != null && !line.isEmpty()) {
            listString = Arrays.asList(line.split(","));
        } else {
            return listString;
        }
        return listString;
    }

    public void save(HistoryManager manager) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            String viewedTasksIdToLine = "";
            int count = 0;

            out.write("id,type,name,status,description,epic\n");
            for (Task task : historyManager.getCustomLinkedList()) {
                out.write(task.toCsvFormat() + "\n");
            }

            String lastLine = historyToString(manager);

            out.write("\n"); // пустая строка после задач
            out.write(lastLine);
            out.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private static String historyToString(HistoryManager manager) {
        String line = manager.getCustomLinkedList().stream()
                .map(task -> task.getId().toString()).collect(Collectors.joining(","));
        return line;
    }

    // ТЗ-6. Напишите метод создания задачи из строки
    private List<Task> fromString() {
        List<Task> tasks = new ArrayList<>(); // Добавил задачи сразу в лист
        Task task;
        Epic epic;
        Subtask subtask;

        UUID id;
        TaskType taskType;
        String name;
        Status status;
        String description;
        List<UUID> list = null;
        UUID epicId = null;

        boolean check = true;
        while (check) {

            Map<UUID, UUID> subtasksOfEpicField = new HashMap<>();
            List<UUID> subtasksIdsByEpic = new ArrayList<>();
            List<Epic> epicList = new ArrayList<>();

            for (List<String> line : readFromCsvTasks()) {
                if (!(line.get(0).isEmpty())) {

                    id = UUID.fromString(String.valueOf(line.get(0)));
                    taskType = TaskType.valueOf(line.get(1));
                    name = line.get(2);
                    status = Status.valueOf(line.get(3));
                    description = line.get(4);
                    if (line.size() == 6) {
                        epicId = UUID.fromString(line.get(5));
                    }

                    if (line.get(1).equals(TaskType.TASK.toString())) {
                        task = new Task(id, taskType, name, status, description);
                        tasks.add(task);
                    } else if (line.get(1).equals(TaskType.EPIC.toString())) {
                        epic = new Epic(id, taskType, name, status, description);
                        epicList.add(epic);
                    }

                    if (line.get(1).equals(TaskType.SUBTASK.toString())) {
                        subtask = new Subtask(id, taskType, name, status, description, epicId);
                        tasks.add(subtask);
                        subtasksOfEpicField.put(id, epicId);
                    }
                }
            }
            for (Epic ep : epicList) {
                subtasksIdsByEpic = subtasksOfEpicField.entrySet()
                        .stream()
                        .filter(e -> Objects.equals(e.getValue(), ep.getId()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                ep.setSubtasksList(subtasksIdsByEpic);
                tasks.add(ep);
            }
            check = false;
        }
        return tasks;
    }

    private List<List<String>> readFromCsvTasks() {
        List<List<String>> addedTasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> innerList;
            while ((line = br.readLine()) != null) {
                innerList = new ArrayList<>(Arrays.asList(line.split(",")));
                addedTasks.add(innerList);
            }
            addedTasks.remove(0);
            addedTasks.remove(addedTasks.size() - 1);
            addedTasks.remove(addedTasks.size() - 1);
            return addedTasks;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private List<Task> readHistoryFromFile() {
        List<Task> tasks = new ArrayList<>();
        tasks = readCsvHistoryFromFile().stream()
                .flatMap(s -> fromString().stream()
                        .filter(task -> task.getId().toString().equals(s)))
                .collect(Collectors.toList());
        return tasks;
    }

    private List<String> readCsvHistoryFromFile() {
        List<UUID> listOfAddedTasks = null;
        List<String> listOfStrings;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String lastLine = null;
            String line = br.readLine();
            while (line != null) {
                lastLine = line;
                line = br.readLine();
            }
            if (lastLine == null) {
                throw new ManagerSaveException("File is empty.");
            }
            listOfStrings = new ArrayList<>(Arrays.asList(lastLine.split(",")));
            line = br.readLine();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return listOfStrings;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
    }

}