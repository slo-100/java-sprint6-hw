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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) throws IOException {
         final  String sep = File.separator;
          String file = String.join(sep, "src","main","java","saves","savesTasks" + ".csv");

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Path.of(file));
        Task task1 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Собрать коробки");
        Task task2 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Упаковать кошку");
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getTaskById(2);
        fileBackedTasksManager.save();
        System.out.println(fileBackedTasksManager.getHistoryManager().getViewedTasks());
//        fileBackedTasksManager.fromString(file);
//        fileBackedTasksManager.historyFromString();
        FileBackedTasksManager.historyToString(fileBackedTasksManager.getHistoryManager());

        /*

Напишите метод сохранения задачи в строку String toString(Task task) или переопределите базовый.
- Напишите метод создания задачи из строки Task fromString(String value).
- Напишите статические методы static String historyToString(HistoryManager manager)
static List<Integer> historyFromString(String value) для сохранения и восстановления менеджера истории из CSV.

* метод fromString возвращает лист задач
         */
    }

    private final static String sep = File.separator;
    private static String file = String.join(sep, "src","main","java","saves","savesTasks" + ".csv");
    static BufferedReader reader;

    static {
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public FileBackedTasksManager(Path file) {
        this.file = file.toString();
    }

    // (ТЗ-6) Создайте метод save без параметров — он будет сохранять текущее состояние менеджера в указанный файл
    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            String historyTasks = "";
            int count = 0;

            bw.write("id,type,name,status,description,epic\n");
            for (Task task : getAddedTasks()) {
                bw.write(task.toCsvFormat() + "\n");
            }

            for (int i = 0; i < getHistoryManager().getViewedTasks().size(); i++) { // ТЗ - 6.1
                if (count < getHistoryManager().getViewedTasks().size() - 1) {
                    historyTasks = historyTasks.concat((getHistoryManager().getViewedTasks().get(i).getId()) + ",");
                } else {
                    historyTasks = historyTasks.concat((getHistoryManager().getViewedTasks().get(i).getId()) + "");
                }
                count++;
            }

            bw.newLine(); // пустая строка после задач
            bw.write(historyTasks);
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

        Integer id;
        TaskType taskType;
        String name;
        Status status;
        String description;

        for (List<String> line : readerTasksFromCsv(str)) {

            id = Integer.parseInt(line.get(0));
            taskType = TaskType.valueOf(line.get(1));
            name = line.get(2);
            status = Status.valueOf(line.get(3));
            description = line.get(4);

            if (!(line.get(0).isEmpty())) {
                if (line.get(1).equals(TaskType.TASK.toString())) {
                    task = new Task(id , taskType, name, status, description);
                    tasks.add(task);
                } else if (line.get(1).equals(TaskType.EPIC.toString())) {
                    epic = new Epic(id , taskType, name, status, description);
                    tasks.add(epic);
                } else if (line.get(1).equals(TaskType.SUBTASK.toString())){
                    subtask = new Subtask(id , taskType, name, status, description);
                    tasks.add(subtask);
                }
            }

        }
        System.out.println("*************************"); // удалить
        System.out.println(tasks); // удалить
        return tasks;
    }


    public List<List<String>> readerTasksFromCsv(String file) {
        ArrayList listOfAddedTasks = new ArrayList<String>();
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
        String line = "";
        for (Task viewedTask : manager.getViewedTasks()) {
            line = line.concat(String.valueOf(viewedTask.getId())) + ",";
        }
        return line;
    }

    /*
    (ТЗ-6)Также отдельный метод  getLastLine(BufferedReader reader) для удобства читаемости, то параметр (String
    value) этого метода лишний, (видел у кого-то в параметре массив из строк), то есть все делают по-разному.
     */
    public List<Integer> historyFromString() throws IOException {
        List<Integer> list = new ArrayList<>();
        String line;
        line = historyFromString(file).toString();
        List<String> numbers = Arrays.asList(line.split(","));
        for (String number : numbers) {
            list.add(Integer.valueOf(number));
        }
        System.out.println("2************* historyFromString *************");
        System.out.println(list);
        return list;
    }

    // (ТЗ-6) Помимо метода сохранения создайте статический метод static FileBackedTasksManager loadFromFile(File
    //  file), который будет восстанавливать данные менеджера из файла при запуске программы.
    // Не забудьте убедиться, что новый менеджер задач работает так же, как предыдущий.
    public static FileBackedTasksManager loadFromFile(Path file) {
        var manager = new FileBackedTasksManager(file);
        return manager;
    }

    // метод возвращает последнюю строку с просмотренными задачами из файла
    public static List<Integer> historyFromString(String file) throws IOException {
        BufferedReader reader;
        List<Integer> listInteger;
        List<String> listString;
        reader = new BufferedReader(new FileReader(file));
        String line = "";
        String nextLine;
        while (true) {
            if (!((nextLine = reader.readLine()) != null)) {
                line = nextLine;
                break;
            }
        }
        listString = Arrays.asList(line.split(","));
        listInteger = listString.stream().map(Integer::parseInt).collect(Collectors.toList());
        return listInteger;
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