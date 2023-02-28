package main.java;

import main.java.intefaces.HistoryManager;
import main.java.intefaces.TaskManager;
import main.java.managers.FileBackedTasksManager;
import main.java.managers.InMemoryTaskManager;
import main.java.service.Managers;
import main.java.service.*;
import main.java.tasks.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager inMemoryTaskManager = Managers.getDefault();

        final String sep = File.separator;
        final String savesTasks = "src" + sep + "main" + sep + "java" + sep + "saves" + sep + "savesTasks" +
                ".csv";
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(Path.of(savesTasks));

        Task task1 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Собрать коробки");
        Task task2 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Упаковать кошку");
        Task task3 = new Task(TaskType.TASK, "Переезд", Status.NEW, "Сказать слова прощания");

        Epic epic1 = new Epic(TaskType.EPIC, "Переезд", Status.NEW, "Переезд", new ArrayList<>());



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
                            inMemoryTaskManager.addEpic(epic1);
                            break;
                        case 3:
                            // подразумевается, что подзадача добавляется только после внесении эпика в мапу (из-за
                            // невомозожности взять epicId до этого момента, так как id теперь присваиваются в
                            // менеджере)
                            Subtask subtask1 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест1", Status.NEW,
                                    "Собрать коробки");
                            Subtask subtask2 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест2", Status.NEW,
                                    "Упаковать кошку");
                            Subtask subtask3 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест3", Status.NEW,
                                    "Сказать слова прощания");
                            inMemoryTaskManager.addSubtask(subtask1);
                            inMemoryTaskManager.addSubtask(subtask2);
                            inMemoryTaskManager.addSubtask(subtask3);
                            break;
                    }
                    break;

                case 2: // Получение всех задач
                    printMenuCase2();
                    int userInputCase2 = scanner.nextInt();
                    switch (userInputCase2) {
                        case 1:
                            System.out.println(inMemoryTaskManager.getTasks());
                            break;
                        case 2:
                            System.out.println(inMemoryTaskManager.getEpics());
                            break;
                        case 3:
                            System.out.println(inMemoryTaskManager.getSubtasks());
                            break;
                    }
                    break;

                case 3: // Удаление всех задач
                    printMenuCase3();
                    int userInputCase3 = scanner.nextInt();
                    switch (userInputCase3) {
                        case 1:
                            inMemoryTaskManager.taskClean();
                            break;
                        case 2:
                            inMemoryTaskManager.epicClean();
                            break;
                        case 3:
                            inMemoryTaskManager.subtaskClean();
                            break;
                    }
                    break;

                case 4: // получение по id
                    printMenuCase4();
                    int userInputCase4 = scanner.nextInt();
                    System.out.println("Введите номер идентификатора");
                    int taskId = scanner.nextInt();
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
                    int update = scanner.nextInt(); // update1 нужно внести в id задачи
                    switch (userInputCase5) {
                        case 1:

                            break;
                        case 2:

                            break;
                        case 3:
                            // Помимо id задачи который хотим заменить нужен также id эпика для обновление списка
                            // поэтому подразумевается что епик уже занесен в мапу
                            Subtask subtaskTest2 = new Subtask(epic1.getId(), TaskType.SUBTASK, "тест1", Status.NEW,
                                    "Собрать коробки", epic1.getId());
                            inMemoryTaskManager.updateSubtask(subtaskTest2);
                            break;
                    }
                    break;

                case 6: // Удаление по идентификатору.
                    printMenuCase6();
                    int userInputCase6 = scanner.nextInt();
                    System.out.println("Введите идентификатор для удаления");
                    int idRemove = scanner.nextInt();
                    switch (userInputCase6) {
                        case 1:
                            inMemoryTaskManager.removeTaskById(idRemove);
                            break;
                        case 2:
                            inMemoryTaskManager.removeEpicById(idRemove);
                            break;
                        case 3:
                            inMemoryTaskManager.removeSubtaskById(idRemove);
                            break;
                    }
                    break;

                case 7: // Изменить статус
                    printMenuCase7();
                    int statusChange = scanner.nextInt();
                    System.out.println("Введите id задачи, чей статус хотите поменять");
                    int statusId = scanner.nextInt();
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
                    inMemoryTaskManager.changeStatusSubtask(statusId, status7);
                    break;

                case 8: // Получение списка всех подзадач определённого эпика.
                    System.out.println("Получение списка всех подзадач определённого эпика\n" + "Введите id эпика, чтобы получить его подзадачи");
                    int epicId = scanner.nextInt();
                    System.out.println(inMemoryTaskManager.getSubtaskList(epicId));
                    break;

                // ТЗ-4
                case 9: // Информация по просмотрам.
                    System.out.println("Какие задачи были просмотрены:");
                    System.out.println(inMemoryTaskManager.getHistoryList());
                    break;

                case 10:
                    System.out.println("------------------- TEST1 -----------------");
                    System.out.println(inMemoryTaskManager.getHistoryManager().getViewedTasks());
/*
"когда ты создаешь экземпляр класса FileBackedTasksManager fileBackedTasksManager , у тебя автоматиечки создаются все поля родительского класса InMemoryTaskManager, в том числе и новый historyManager, который будет принадлижать только fileBackedTasksManager, и каждый раз когда ты будешь работать с задачами через  fileBackedTasksManager, вся история будет сохраняться в historyManager, а из этого поля ты уже сможешь вытащить список задач viewedTasks(а потом и айдишники, которые ты запишешь в файл)"
 */
                    //
                    System.out.println(fileBackedTasksManager.getHistoryManager().getViewedTasks());
                    break;
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
        System.out.println("10 - Тест");

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

