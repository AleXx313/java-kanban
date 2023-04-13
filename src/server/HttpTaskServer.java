package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.TaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import util.Managers;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private static final int PORT = 8080;

    private TaskManager taskManager;
    private Gson gson;
    private HttpServer server;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getFileBackedTaskManager(new File("src/data.csv"));
        gson = Managers.getDefaultGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/task", this::handleTasks);
        server.createContext("/tasks/subtask", this::handleSubtasks);
        server.createContext("/tasks/epic", this::handleEpics);
        server.createContext("/tasks", this::handleInfo);
    }
    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this();
        this.taskManager = taskManager;
    }

    private void handleTasks(HttpExchange h){
        try{
            //GET, GET+id, POST, DELETE+id, DELETE
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();

            switch (method){
                case "GET":{
                    if (Pattern.matches("^/tasks/task$", path)) {
                        String response = gson.toJson(taskManager.getTaskList());
                        sendText(h, response);
                    } else if (Pattern.matches("^/tasks/task/\\d+$", path)){
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parseId(pathId);
                        if (id != -1){
                            String response = gson.toJson(taskManager.getTaskById(id));
                            sendText(h,response);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "POST":{
                    if (Pattern.matches("^/tasks/task$", path)){
                        String taskJson = readText(h);
                        Task task = gson.fromJson(taskJson, Task.class);
                        if (task.getId() != 0){
                            taskManager.updateTask(task);
                            System.out.println("Задача с id " + task.getId() + " обновлена.");
                            h.sendResponseHeaders(201, 0);
                        } else {
                            taskManager.createTask(task);
                            System.out.println("Задача создана, ей присвоен id " + task.getId() + ".");
                            h.sendResponseHeaders(201, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "DELETE":{
                    if (Pattern.matches("^/tasks/task$", path)) {
                        taskManager.clearTasks();
                        h.sendResponseHeaders(200, 0);

                    } else if (Pattern.matches("^/tasks/task/\\d+$", path)){
                        String pathId = path.replaceFirst("/tasks/task/", "");
                        int id = parseId(pathId);
                        if (id != -1){
                            taskManager.removeTask(id);
                            System.out.println("Удалена задача с id - " + id);
                            h.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Передан неверный метод запроса. Ожидается GET, POST или DELETE.");
                    h.sendResponseHeaders(405, 0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            h.close();
        }
    }
    private void handleSubtasks (HttpExchange h) {
        try{
            //GET, GET+id, GET+epic+id, POST, DELETE+id, DELETE
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();

            switch (method){
                case "GET":{
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        String response = gson.toJson(taskManager.getSubTaskList());
                        sendText(h, response);
                    } else if (Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parseId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(taskManager.getSubTaskById(id));
                            sendText(h, response);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks/subtask/epic/\\d+$", path)){
                        String pathId = path.replaceFirst("/tasks/subtask/epic/", "");
                        int id = parseId(pathId);
                        if (id != -1){
                            String response = gson.toJson(taskManager.getSubTaskListByEpic(id));
                            sendText(h, response);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "POST":{
                    if (Pattern.matches("^/tasks/subtask$", path)){
                        String taskJson = readText(h);
                        SubTask subTask = gson.fromJson(taskJson, SubTask.class);
                        if (subTask.getId() != 0){
                            taskManager.updateSubTask(subTask);
                            System.out.println("Задача с id " + subTask.getId() + " обновлена.");
                            h.sendResponseHeaders(201, 0);
                        } else {
                            taskManager.createSubTask(subTask);
                            System.out.println("Задача создана, ей присвоен id " + subTask.getId() + ".");
                            h.sendResponseHeaders(201, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "DELETE":{
                    if (Pattern.matches("^/tasks/subtask$", path)) {
                        taskManager.clearSubTasks();
                        h.sendResponseHeaders(200, 0);

                    } else if (Pattern.matches("^/tasks/subtask/\\d+$", path)){
                        String pathId = path.replaceFirst("/tasks/subtask/", "");
                        int id = parseId(pathId);
                        if (id != -1){
                            taskManager.removeSubTask(id);
                            System.out.println("Удалена подзадача с id - " + id);
                            h.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Передан неверный метод запроса. Ожидается GET, POST или DELETE.");
                    h.sendResponseHeaders(405, 0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            h.close();
        }
    }
    private void handleEpics (HttpExchange h) {
        try{
            //GET, GET+id, POST, DELETE+id, DELETE
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();

            switch (method){
                case "GET":{
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        String response = gson.toJson(taskManager.getEpicTaskList());
                        sendText(h, response);
                    } else if (Pattern.matches("^/tasks/epic/\\d+$", path)){
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parseId(pathId);
                        if (id != -1){
                            String response = gson.toJson(taskManager.getEpicTaskById(id));
                            sendText(h,response);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "POST":{
                    if (Pattern.matches("^/tasks/epic$", path)){
                        String taskJson = readText(h);
                        EpicTask epicTask = gson.fromJson(taskJson, EpicTask.class);
                        if (epicTask.getId() != 0){
                            taskManager.updateEpicTask(epicTask);
                            System.out.println("Задача с id " + epicTask.getId() + " обновлена.");
                            h.sendResponseHeaders(201, 0);
                        } else {
                            taskManager.createEpicTask(epicTask);
                            System.out.println("Задача создана, ей присвоен id " + epicTask.getId() + ".");
                            h.sendResponseHeaders(201, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "DELETE":{
                    if (Pattern.matches("^/tasks/epic$", path)) {
                        taskManager.clearEpicTasks();
                        h.sendResponseHeaders(200, 0);

                    } else if (Pattern.matches("^/tasks/epic/\\d+$", path)){
                        String pathId = path.replaceFirst("/tasks/epic/", "");
                        int id = parseId(pathId);
                        if (id != -1){
                            taskManager.removeEpicTask(id);
                            System.out.println("Удалена эпик задача с id - " + id);
                            h.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Получен некорректный id - " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Передан неверный метод запроса. Ожидается GET, POST или DELETE.");
                    h.sendResponseHeaders(405, 0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            h.close();
        }
    }
    private void handleInfo(HttpExchange h) {
        try{
            //GET, GET+history
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();
            if (method.equals("GET")){
                if (Pattern.matches("^/tasks$", path)){
                    String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                } else if (Pattern.matches("^/tasks/history$", path)){
                    String response = gson.toJson(taskManager.getHistory());
                    sendText(h, response);
                } else {
                    h.sendResponseHeaders(405, 0);
                }
                return;
            }
            System.out.println("Передан неверный метод запроса. Ожидается GET.");
            h.sendResponseHeaders(405, 0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            h.close();
        }
    }

    private int parseId(String path){
        try{
            return Integer.parseInt((path));
        } catch (NumberFormatException e){
            e.printStackTrace();
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop() {
        System.out.println("Остановлен сервер на порту " + PORT);
        server.stop(0);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

}
