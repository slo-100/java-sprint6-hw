package main.java.managers;

import main.java.intefaces.HistoryManager;
import main.java.service.ManagerSaveException;
import main.java.service.Status;
import main.java.service.TaskType;
import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {

    // +++++++++++++++++++++++++++++++++++++++ TEST BLOCK +++++++++++++++++++++++++++++++++++++++
    public static void main(String[] args) throws IOException {
        final String sep = File.separator;
        String savesTasksFile = String.join(sep, "src", "main", "java", "saves", "taskSaves" + ".csv");
//        File file = new File(savesTasksFile);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(savesTasksFile);
        FileBackedTasksManager fileBackedTasksManagerTEST;

        Task task1 = new Task(UUID.randomUUID().toString(), TaskType.TASK, "Переезд", Status.NEW, "Собрать коробки");
        Task task2 = new Task(UUID.randomUUID().toString(), TaskType.TASK, "Переезд", Status.NEW, "Упаковать кошку");

        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);

        boolean menu = true;
        while (menu) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1 - Получить по идентификатору");
            System.out.println("2 - Тест loadFromFile");
            System.out.println("3 - Тест save()");
            System.out.println("4 - Тест historyFromString(file)");
            System.out.println("5 - Тест historyToString");
            System.out.println("6 - Тест fromString");
            System.out.println("0 - Выход");
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1: // Получение всех задач
                    System.out.println("getViewedTasks()" + fileBackedTasksManager.getHistoryManager().getViewedTasks() + " size: " + fileBackedTasksManager.getHistoryManager().getViewedTasks().size());
                    System.out.println("Введите номер идентификатора");

                    String taskId = scanner.next();
                    System.out.println(fileBackedTasksManager.getTaskById(taskId));

//                System.out.println(fileBackedTasksManager.getEpicById(taskId));
//                System.out.println(fileBackedTasksManager.getSubtaskById(taskId));
                    break;
                case 2:
                    fileBackedTasksManagerTEST = loadFromFile(savesTasksFile);
                    fileBackedTasksManagerTEST.fromString(savesTasksFile).stream().forEach(System.out::println);
                    break;
                case 3:
                    fileBackedTasksManager.save();
                    break;
                case 4:
                    System.out.println("\nметод возвращает последнюю строку с просмотренными задачами из файла public static List<String> historyFromString(String file):");
                    fileBackedTasksManager.historyFromString(savesTasksFile);
                    break;
                case 5:
                    System.out.println("\n(ТЗ-6) Напишите статические методы static String historyToString(HistoryManager manager)\n и static List<Integer> historyFromString(String value) для сохранения и восстановления менеджера истории из CSV.\n public static String historyToString(HistoryManager manager):");
                    historyToString(fileBackedTasksManager.getHistoryManager());
                    break;
                case 6:
                    System.out.println("List<Task> fromString(String str)");
                    fileBackedTasksManagerTEST = loadFromFile(savesTasksFile);
                    System.out.println(fileBackedTasksManagerTEST.fromString(savesTasksFile));
                    break;
                case 0:
                    menu = false;
                    break;
            }
        }
    }
    // +++++++++++++++++++++++++++++++++++++++ TEST BLOCK +++++++++++++++++++++++++++++++++++++++

    private final static String sep = File.separator;
    private static String savesTasksFile = String.join(sep, "src", "main", "java", "saves", "taskSaves" + ".csv");
    public FileBackedTasksManager(String file) {
        this.savesTasksFile = file;
    }

    // (ТЗ-6) Создайте метод save без параметров — он будет сохранять текущее состояние менеджера в указанный файл
    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(savesTasksFile))) {
            String viewedTasksIdToLine = "";
            int count = 0;

            bw.write("id,type,name,status,description,epic\n");
            for (Task task : getAddedTasks()) {
                bw.write(task.toCsvFormat() + "\n");
            }
            System.out.println("save() метод getHistoryManager().getViewedTasks() " +
                    historyManager.getViewedTasks().size());

            for (int i = 0; i < getHistoryManager().getViewedTasks().size(); i++) { // ТЗ - 6.1
                if (count < getHistoryManager().getViewedTasks().size() - 1) {
                    viewedTasksIdToLine = viewedTasksIdToLine.concat((getHistoryManager().getViewedTasks().get(i).getId()) + ",");
                } else {
                    viewedTasksIdToLine = viewedTasksIdToLine.concat((getHistoryManager().getViewedTasks().get(i).getId()) + "");
                }
                count++;
            }

            bw.newLine(); // пустая строка после задач
            bw.write(viewedTasksIdToLine);
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    // ТЗ-6. Напишите метод создания задачи из строки
    public List<Task> fromString(String str) {
        List<Task> tasks = new ArrayList<>(); // Добавил задачи сразу в лист
        Task task;
        Epic epic;
        Subtask subtask;

        String id;
        TaskType taskType;
        String name;
        Status status;
        String description;

        for (List<String> line : readerTasksFromCsv(str)) {
            if (!(line.get(0).isEmpty())) {

                id = String.valueOf(line.get(0));
                taskType = TaskType.valueOf(line.get(1));
                name = line.get(2);
                status = Status.valueOf(line.get(3));
                description = line.get(4);

                if (line.get(1).equals(TaskType.TASK.toString())) {
                    task = new Task(id, taskType, name, status, description);
                    tasks.add(task);
                } else if (line.get(1).equals(TaskType.EPIC.toString())) {
                    epic = new Epic(id, taskType, name, status, description);
                    tasks.add(epic);
                } else if (line.get(1).equals(TaskType.SUBTASK.toString())) {
                    subtask = new Subtask(id, taskType, name, status, description);
                    tasks.add(subtask);
                }
            }
        }
        return tasks;
    }

    public List<List<String>> readerTasksFromCsv(String file) {
        List listOfAddedTasks = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> innerList;
            while ((line = br.readLine()) != null) {
                innerList = new ArrayList<>(Arrays.asList(line.split(",")));
                listOfAddedTasks.add(innerList);
            }
            listOfAddedTasks.remove(0);
            listOfAddedTasks.remove(listOfAddedTasks.size() - 1);
            listOfAddedTasks.remove(listOfAddedTasks.size() - 1);
            return listOfAddedTasks;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        // "но лучше, оставить все как есть, только выбрасывать ManagerSaveException() с разными соответствующими месседжами"
        // Создать второй класс эксепшн?
    }

    // (ТЗ-6) Напишите статические методы static String historyToString(HistoryManager manager)
    // и static List<Integer> historyFromString(String value)
    // для сохранения и восстановления менеджера истории из CSV.

    public static String historyToString(HistoryManager manager) {
        String line = manager.getViewedTasks().stream()
                .map(task -> task.getId()).collect(Collectors.joining(","));
        return line;
    }

    // (ТЗ-6) Помимо метода сохранения создайте статический метод static FileBackedTasksManager loadFromFile(File
    //  file), который будет восстанавливать данные менеджера из файла при запуске программы.
    // Не забудьте убедиться, что новый менеджер задач работает так же, как предыдущий.
    public static FileBackedTasksManager loadFromFile(String file) {
        var manager = new FileBackedTasksManager(file);
        return manager;
    }

    // метод возвращает последнюю строку с просмотренными задачами из файла
    public static List<String> historyFromString(String file) throws IOException {
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