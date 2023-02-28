//package main.java.managers;
//
//import main.java.intefaces.HistoryManager;
//import main.java.intefaces.TaskManager;
//import main.java.service.ManagerSaveException;
//import main.java.service.Status;
//import main.java.service.TaskType;
//import main.java.tasks.Epic;
//import main.java.tasks.Subtask;
//import main.java.tasks.Task;
//
//import java.io.*;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
//    public static void main(String[] args) {
//        System.out.println(FileBackedTasksManager.historyFromString());
//        System.out.println(FileBackedTasksManager.historyToString());
//    }
//    private final static String sep = File.separator;
//    private static String file = "src" + sep + "main" + sep + "java" + sep + "saves" + sep + "savesTasks" + ".csv";
//    public final String fileTasks2 = "src" + sep + "main" + sep + "java" + sep + "saves" + sep + "savesTasks" + ".csv"; // удалить
//    static BufferedReader reader;
//
//    static {
//        try {
//            reader = new BufferedReader(new FileReader(file));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public FileBackedTasksManager(Path file) {
//        this.file = file.toString();
//    }
//
//    // Создайте метод save без параметров — он будет сохранять текущее состояние менеджера в указанный файл
//    public void save() {
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
//            String addedTask = "";
//            String historyTasks = "";
//            int count = 0;
//
//            bw.write("id,type,name,status,description,epic\n");
//            for (Task task : getAddedTasks()) {
//                bw.write(task.toCsvFormat() + "\n");
//            }
//
//            for (Integer viewedTask : InMemoryTaskManager.getViewedTasks()) {
//                if (count < InMemoryTaskManager.getViewedTasks().size() - 1) {
//                    historyTasks = historyTasks.concat(viewedTask.toString() + ",");
//                } else {
//                    historyTasks = historyTasks.concat(viewedTask.toString() + "");
//                }
//                count++;
//            }
//            bw.newLine(); // пустая строка после задач
//            bw.write(historyTasks);
//        } catch (ManagerSaveException e) {
//            throw new ManagerSaveException("Произошла какая-то ошибка!"); //доделать
//        } catch (IOException e) {
//            throw new ManagerSaveException(e.getMessage());
//        }
//    }
//
//    // Напишите метод создания задачи из строки
//    public List<Task> fromString(String str) {
//        List<Task> tasks = new ArrayList<>(); // Добавил задачи сразу в лист
//        Task task;
//        Epic epic;
//        Subtask subtask;
//        for (List<String> line : csvReader(str)) {
//            if (!(line.get(0).isEmpty())) {
//                System.out.println("fromString(String line) line: " + line);
//                System.out.println("fromString(String line) line.size(): " + line.size());
//                if (line.get(1).equals(TaskType.TASK.toString())) {
//                    task = new Task(Integer.parseInt(line.get(0)), TaskType.valueOf(line.get(1)), line.get(2), Status.valueOf(line.get(3)), line.get(4));
//                    tasks.add(task);
//                } else if (line.get(1).equals(TaskType.EPIC.toString())) {
//                    epic = new Epic(Integer.parseInt(line.get(0)), TaskType.valueOf(line.get(1)), line.get(2),
//                            Status.valueOf(line.get(3)), line.get(4));
//                    tasks.add(epic);
//                } else if (line.get(1).equals(TaskType.SUBTASK.toString())){
//                    subtask = new Subtask(Integer.parseInt(line.get(0)), TaskType.valueOf(line.get(1)), line.get(2),
//                            Status.valueOf(line.get(3)), line.get(4));
//                    tasks.add(subtask);
//                }
//            }
//
//        }
//        System.out.println("*************************");
//        System.out.println(tasks);
//        return tasks;
//    }
//
//
//    public List<List<String>> csvReader(String file) {
//        ArrayList listOfAddedTasks = new ArrayList<String>();
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            List<String> innerList;
//            while ((line = br.readLine()) != null) {
//                innerList = new ArrayList<>(Arrays.asList(line.split(",")));
//                listOfAddedTasks.add(innerList);
//            }
//            listOfAddedTasks.remove(0);
//
//            System.out.println("csvReader listOfAddedTasks:" + listOfAddedTasks); // удалить
//            System.out.println("csvReader listOfAddedTasks.size():" + listOfAddedTasks.size()); // удалить
//
//            return listOfAddedTasks;
//        } catch (IOException e) {
//            throw new ManagerSaveException();
//        } catch (Exception e) {
//            throw new ManagerSaveException();
//        }
//    }
//
//    // (ТЗ-6) Напишите статические методы static String historyToString(HistoryManager manager)
//    // и static List<Integer> historyFromString(String value)
//    // для сохранения и восстановления менеджера истории из CSV.
//    public static String historyToString() {
//        // не совсем понятно для чего нужен менеджер (хистори?), этот же метод всеравно нельзя добавить интерфейс,
//        // другими словами доступа к методу через менеджер не будет
//        String line;
//        try {
//            line = getLastLine(reader); // не могу понять почему этот же ридор возвращает нули?? если запускать
//            // программу с этого файла
//            return line;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /*
//    //
//    Также отдельный метод  getLastLine(BufferedReader reader) для удобства читаемости, то параметр (String
//    value) этого метода лишний, (видел у кого-то в параметре массив из строк), то есть все делают по-разному.
//     */
//    public static List<Integer> historyFromString() {
//        List<Integer> list = new ArrayList<>();
//        String line;
//        try {
//            line = getLastLine(reader);
//            List<String> numbers = Arrays.asList(line.split(","));
//            for (String number : numbers) {
//                list.add(Integer.valueOf(number));
//            }
////            строка ниже почемуто не работает также через Integer.valueOf также иде ругалась пришлось разбить в
////            несколько строк выше, потерял много времени на поиск причины и так и не понял почему
////            list = new ArrayList<>(Integer.parseInt(Arrays.asList(getLastLine(reader).split(",")).toString()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return list;
//    }
//
//    // (ТЗ-6) Помимо метода сохранения создайте статический метод static FileBackedTasksManager loadFromFile(File
//    //  file), который будет восстанавливать данные менеджера из файла при запуске программы.
//    // Не забудьте убедиться, что новый менеджер задач работает так же, как предыдущий.
//    public static FileBackedTasksManager loadFromFile(Path file) {
//        var manager = new FileBackedTasksManager(file);
//        return manager;
//    }
//
//    // метод возвращает последнюю строку с просмотренными задачами из файла
//    private static String getLastLine(BufferedReader reader) throws IOException {
//        String line = "";
//        String nextLine;
//        while ((nextLine = reader.readLine()) != null) {
//            line = nextLine;
//        }
//        return line;
//    }
//
//    public static String getFile() {
//        return file;
//    }
//
//    @Override
//    public void addTask(Task task) {
//        super.addTask(task);
//        save();
//    }
//
//    @Override
//    public void addEpic(Epic epic) {
//        super.addEpic(epic);
//        save();
//    }
//
//    @Override
//    public void addSubtask(Subtask subtask) {
//        super.addSubtask(subtask);
//        save();
//    }
//
//}