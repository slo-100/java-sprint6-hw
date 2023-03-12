package main.java;

import main.java.managers.FileBackedTasksManager;
import main.java.service.*;
import main.java.tasks.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private final static String sep = File.separator;
    private static String savesTasksFile = String.join(sep, "src", "main", "java", "saves", "taskSaves" + ".csv");
    private static File file = new File(savesTasksFile);
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(savesTasksFile);

        Task task1 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Собрать коробки");
        Task task2 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Упаковать кошку");
        Task task3 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Сказать слова прощания");


        Epic epic1 = new Epic(TaskType.EPIC, "Переезд", Status.NEW, "Переезд", new ArrayList<>());
        Epic epic2 = new Epic(TaskType.EPIC, "Переезд2", Status.NEW, "Переезд2", new ArrayList<>());



        boolean menu = true;
        while (menu) {
            printMenu();

            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1: // Получение всех задач
                    printMenuCase1();
                    int userInputCase1 = scanner.nextInt();
                    switch (userInputCase1) {
                        case 1:
                            fileBackedTasksManager.addTask(task1);
                            fileBackedTasksManager.addTask(task2);
                            fileBackedTasksManager.addTask(task3);
                            break;
                        case 2:
                            fileBackedTasksManager.addEpic(epic1);
                            fileBackedTasksManager.addEpic(epic2);
                            break;
                        case 3:
                            Subtask subtask1 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест1", Status.NEW,
                                    "Собрать коробки");
                            Subtask subtask2 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест2", Status.NEW,
                                    "Упаковать кошку");
                            Subtask subtask3 = new Subtask(epic2.getId(), TaskType.SUBTASK, "тест3", Status.NEW,
                                    "Сказать слова прощания");
                            fileBackedTasksManager.addSubtask(subtask1);
                            fileBackedTasksManager.addSubtask(subtask2);
                            fileBackedTasksManager.addSubtask(subtask3);
                            break;
                    }
                    break;

                case 2: // Получение всех задач
                    printMenuCase2();
                    int userInputCase2 = scanner.nextInt();
                    switch (userInputCase2) {
                        case 1:
                            System.out.println(fileBackedTasksManager.getTasks());
                            break;
                        case 2:
                            System.out.println(fileBackedTasksManager.getEpics());
                            break;
                        case 3:
                            System.out.println(fileBackedTasksManager.getSubtasks());
                            break;
                    }
                    break;

                case 3: // Удаление всех задач
                    printMenuCase3();
                    int userInputCase3 = scanner.nextInt();
                    switch (userInputCase3) {
                        case 1:
                            fileBackedTasksManager.taskClean();
                            break;
                        case 2:
                            fileBackedTasksManager.epicClean();
                            break;
                        case 3:
                            fileBackedTasksManager.subtaskClean();
                            break;
                    }
                    break;

                case 4: // получение по id
                    printMenuCase4();
                    int userInputCase4 = scanner.nextInt();
                    System.out.println("Введите номер идентификатора");
                    String taskId = scanner.next();
                    switch (userInputCase4) {
                        case 1:
                            System.out.println(fileBackedTasksManager.getTaskById(taskId));
                            break;
                        case 2:
                            System.out.println(fileBackedTasksManager.getEpicById(taskId));
                            break;
                        case 3:
                            System.out.println(fileBackedTasksManager.getSubtaskById(taskId));
                            break;
                    }
                    break;

                case 5: // обновление по id
                    printMenuCase5();
                    int userInputCase5 = scanner.nextInt();
                    System.out.println("Введите номер идентификатора той задачи которую хотите обновить");
                    String taskIdCase5 = scanner.next();
                    switch (userInputCase5) {
                        case 1:
                            task3.setDescription("Сказать слова прощания test Case5");
                            fileBackedTasksManager.updateTask(task3);
                            break;
                        case 2:

                            break;
                        case 3:
                            // Помимо id задачи который хотим заменить нужен также id эпика для обновление списка
                            // поэтому подразумевается что епик уже занесен в мапу
                            Subtask subtaskTest2 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест1", Status.NEW,
                                    "Собрать коробки", epic1.getId()); // исправить
                            fileBackedTasksManager.updateSubtask(subtaskTest2);
                            break;
                    }
                    break;

                case 6: // Удаление по идентификатору.
                    printMenuCase6();
                    int userInputCase6 = scanner.nextInt();
                    System.out.println("Введите идентификатор для удаления");
                    String idRemove = scanner.nextLine();
                    switch (userInputCase6) {
                        case 1:
                            fileBackedTasksManager.removeTaskById(idRemove);
                            break;
                        case 2:
                            fileBackedTasksManager.removeEpicById(idRemove);
                            break;
                        case 3:
                            fileBackedTasksManager.removeSubtaskById(idRemove);
                            break;
                    }
                    break;

                case 7: // Изменить статус
                    printMenuCase7();
                    System.out.println("Введите id задачи, чей статус хотите поменять");
                    String statusId = scanner.nextLine();
                    System.out.println("Назначьте статус, где:\n1 - Задача новая\n" + "2 - Задача выполнена\n3 - Задача в действии");
                    int check = scanner.nextInt();
                    Status status7 = null;
                    switch (check) {
                        case 1:
                            status7 = Status.NEW;
                            break;
                        case 2:
                            status7 = Status.DONE;
                            break;
                        case 3:
                            status7 = Status.IN_PROGRESS;
                            break;
                    }
                    fileBackedTasksManager.changeStatusSubtask(statusId, status7);
                    break;

                case 8: // Получение списка всех подзадач определённого эпика.
                    System.out.println("Получение списка всех подзадач определённого эпика\n" + "Введите id эпика, чтобы получить его подзадачи");
                    String epicId = scanner.nextLine();
                    System.out.println(fileBackedTasksManager.getSubtaskList(epicId));
                    break;

                // ТЗ-4
                case 9: // Информация по просмотрам.
                    System.out.println("Какие задачи были просмотрены:");
                    System.out.println(fileBackedTasksManager.getHistoryList());
                    break;

                case 10: // тесты
                    fileBackedTasksManager.save();
                    System.out.println("test1 getViewedTasks(): " + fileBackedTasksManager.historyManager.getCustomLinkedList());
                    System.out.println("test2 getViewedTasks(): " + fileBackedTasksManager.historyManager.getCustomLinkedList());
                    System.out.println("test3 getViewedTasks(): " + fileBackedTasksManager.historyManager.getCustomLinkedList());
                    fileBackedTasksManager.historyManager.getCustomLinkedList().stream()
                            .forEach(task -> {System.out.println(task.getId());});

                    System.out.println("test2 getViewedTasks(): " + fileBackedTasksManager.historyManager.getCustomLinkedList());

                    fileBackedTasksManager.historyManager.getCustomLinkedList().stream()
                            .forEach(task -> {System.out.println(task.getId());});
                    break;
/*
"когда ты создаешь экземпляр класса FileBackedTasksManager fileBackedTasksManager , у тебя автоматиечки создаются все поля родительского класса InMemoryTaskManager, в том числе и новый historyManager, который будет принадлижать только fileBackedTasksManager, и каждый раз когда ты будешь работать с задачами через  fileBackedTasksManager, вся история будет сохраняться в historyManager, а из этого поля ты уже сможешь вытащить список задач viewedTasks(а потом и айдишники, которые ты запишешь в файл)"
 */
                    //
                case 0: // Выход
                    menu = false;
                    break;
            }

        }
    }

    public static void printMenu() {
        System.out.println("1 - Добавить новую задачу");
        System.out.println("2 - Получить список всех задач");
        System.out.println("3 - Удалить все задачи");
        System.out.println("4 - Получить по идентификатору");
        System.out.println("5 - Обновить по идентификатору");
        System.out.println("6 - Удалить по идентификатору");
        System.out.println("7 - Изменить статус");
        System.out.println("8 - Получение списка всех подзадач определённого эпика");
        System.out.println("9 - Информация по просмотрам");
        System.out.println("10 - Сохранить задачи");

        System.out.println("0 - Выход");
    }

    public static void printMenuCase1() {
        System.out.println("1 - Добавить новую задачу");
        System.out.println("2 - Добавить новый эпик");
        System.out.println("3 - Добавить новую подзадачу");
    }

    public static void printMenuCase2() {
        System.out.println("1 - Получить все задачи");
        System.out.println("2 - Получить все эпики");
        System.out.println("3 - Получить все подзадачи");
    }

    public static void printMenuCase3() {
        System.out.println("1 - Удалить все задачи");
        System.out.println("2 - Удалить все эпикови");
        System.out.println("3 - Удалить все подзадачи");
    }

    public static void printMenuCase4() {
        System.out.println("1 - Получить задачу по идентификатору");
        System.out.println("2 - Получить эпик по идентификатору");
        System.out.println("3 - Получить подзадачу по идентификатору");
    }


    public static void printMenuCase5() {
        System.out.println("1 - Обновление задачи по идентификатору");
        System.out.println("2 - Обновление епика по идентификатору");
        System.out.println("3 - Обновление подзадачи по идентификатору");
    }

    public static void printMenuCase6() {
        System.out.println("1 - Удалить задачу по идентификатору");
        System.out.println("2 - Удалить епик по идентификатору");
        System.out.println("3 - Удалить подзадачу по идентификатору");
    }

    public static void printMenuCase7() {
        System.out.println("1 - Изменить статус задачи");
        System.out.println("2 - Изменить статус епика");
        System.out.println("3 - Изменить статус подзадачи");
    }

}

