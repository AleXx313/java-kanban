package memory;

import manager.InMemoryTaskManager;
import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static File data;

    public FileBackedTaskManager(File file) {
        super();
        data = file;
    }

    public static String getPath() {
        return data.getPath();
    }

    //Переопределенные модифицирующие методы.
    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpicTasks() {
        super.clearEpicTasks();
        save();
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;

    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask epicTask = super.getEpicTaskById(id);
        save();
        return epicTask;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpicTask(int id) {
        super.removeEpicTask(id);
        save();
    }

    @Override
    public void removeSubTask(int id) {
        super.removeSubTask(id);
        save();
    }

    // СОХРАНЕНИЕ
    //Метод по преобразованию всех тасок в список строк.
    private List<String> convertTasksToStrings() {
        List<Task> tasks = getAllTypesTaskList();
        List<String> tasksToString = new ArrayList<>();
        for (Task task : tasks) {
            tasksToString.add(task.toString());
        }
        return tasksToString;
    }
    //Утилитарный метод по получению листа Тасок всех типов.
    private List<Task> getAllTypesTaskList(){
        List<Task> tasks = getTaskList();
        tasks.addAll(getEpicTaskList());
        tasks.addAll(getSubTaskList());
        return tasks;
    }

    //Метод по преобразованию истории вызовов в строку целых чисел (id).
    private static String historyToString() {
        List<Task> tasks = historyManager.getHistory();
        if (!tasks.isEmpty()){
            StringBuilder builder = new StringBuilder();
            for (Task task : tasks) {
                builder.append(task.getId() + ",");
            }
            return builder.substring(0, builder.length() - 1);
        }
        return "";
    }

    //Метод по записи данных в файл.
    private void save() {
        List<String> tasks = convertTasksToStrings();
        try (FileWriter writer = new FileWriter(data, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n"); //Именование колонок.
            for (String task : tasks) {
                writer.write(task + "\n");  //Запись тасок.
            }
            writer.write("\n"); //Пропуск строки.
            writer.write(historyToString()); //Запись истории.

        } catch (IOException e) {
            throw new ManagerSaveException("При записи данных в файл произошла ошибка");
        }
    }

    // ЗАГРУЗКА
    //Финальный метод для загрузки данных из файла
    public static void loadFile() {
        List<String> lines = readFileContents(getPath());
        int idCounter = 1;
        if (!lines.isEmpty()){
            for (int i = 1; i < lines.size() - 2; i++) {
                String line = lines.get(i);
                String[] taskStringArray = line.split(",");
                switch (taskStringArray[1]) {
                    case "Task":
                        taskLoad(taskStringArray);
                        break;
                    case "EpicTask":
                        epicTaskLoad(taskStringArray);
                        break;
                    case "SubTask":
                        subTaskLoad(taskStringArray);
                        break;
                    default:
                        throw new IllegalStateException("Непредвиденная ошибка. Дата файл поврежден!");
                }
                idCounter++;
            }
            setIdCounter(idCounter);
            loadHistory(lines.get(lines.size() - 1));
        }
    }

    // Загрузка списка строк из файла.
    private static List<String> readFileContents(String path) {
        try {
            return Files.readAllLines(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл");
            return Collections.emptyList();
        }
    }

    //Метод по восстановлению истории
    private static void loadHistory(String line) {
        List<Integer> history = historyFromString(line);

        for (Integer id : history) {
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epicTasks.containsKey(id)) {
                historyManager.add(epicTasks.get(id));
            } else if (subTasks.containsKey(id)) {
                historyManager.add(subTasks.get(id));
            }
        }
    }

    //Метод по получению спика целочисленных значений из строки
    private static List<Integer> historyFromString(String line) {
        List<Integer> history = new ArrayList<>();
        String[] lines = line.split(",");
        for (int i = 0; i < lines.length; i++) {
            history.add(Integer.parseInt(lines[i]));
        }
        return history;
    }

    //Методы по загрузке Задач
    private static void taskLoad(String[] taskStringArray) {
        Task task = new Task(taskStringArray[2], taskStringArray[4]);
        task.setId(Integer.parseInt(taskStringArray[0]));
        task.setStatus(Status.valueOf(taskStringArray[3]));
        tasks.put(Integer.parseInt(taskStringArray[0]), task);
    }

    private static void epicTaskLoad(String[] taskStringArray) {
        EpicTask epicTask = new EpicTask(taskStringArray[2], taskStringArray[4]);
        epicTask.setId(Integer.parseInt(taskStringArray[0]));
        epicTask.setStatus(Status.valueOf(taskStringArray[3]));
        epicTasks.put(Integer.parseInt(taskStringArray[0]), epicTask);
    }

    private static void subTaskLoad(String[] taskStringArray) {
        SubTask subTask = new SubTask(taskStringArray[2], taskStringArray[4]
                , epicTasks.get(Integer.parseInt(taskStringArray[5])));
        subTask.setStatus(Status.valueOf(taskStringArray[3]));
        subTask.setId(Integer.parseInt(taskStringArray[0]));
        subTasks.put(Integer.parseInt(taskStringArray[0]), subTask);
        subTask.getEpicTask().getSubTasks().put(subTask.getId(), subTask);
    }
}
