package serverTests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import util.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    private TaskManager manager;
    private final Gson gson = Managers.getDefaultGson();

    private Task task;
    private EpicTask epic;
    private SubTask subTask;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefaultInMemory();
        server = new HttpTaskServer(manager);

        task = new Task("Task1", "Task1 Desc"
                , LocalDateTime.of(2022, 1,1,0,0,0), 15);
        epic = new EpicTask("EpicTask1", "EpicTask1 Desc"
                , LocalDateTime.of(2022, 1,1,0,0,0), 15);
        subTask = new SubTask("SubTask1", "SubTask1 Desc"
                , LocalDateTime.of(2022, 1,1,4,0,0), 15, epic);

        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testTaskEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        String taskToSave = gson.toJson(task);
        final HttpRequest.BodyPublisher postTaskBody = HttpRequest.BodyPublishers.ofString(taskToSave);
        HttpRequest requestToPostTask = HttpRequest.newBuilder().uri(uri).POST(postTaskBody).build();
        HttpResponse<String> responseToPostTask = client.send(requestToPostTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseToPostTask.statusCode());
        assertEquals(1, manager.getTaskList().get(0).getId());
        assertEquals("Task1", manager.getTaskList().get(0).getTitle());

        task = manager.getTaskList().get(0);
        task.setStatus(Status.IN_PROGRESS);
        String taskToUpdate = gson.toJson(task);
        final HttpRequest.BodyPublisher taskToUpdateBody = HttpRequest.BodyPublishers.ofString(taskToUpdate);
        HttpRequest requestToUpdateTask = HttpRequest.newBuilder().uri(uri).POST(taskToUpdateBody).build();
        HttpResponse<String> responseToUpdateTask = client.send(requestToUpdateTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseToUpdateTask.statusCode());
        assertEquals(Status.IN_PROGRESS, manager.getTaskList().get(0).getStatus());

        HttpRequest getRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        Type taskType = new TypeToken< ArrayList<Task> >() {}.getType();
        List<Task> actual = gson.fromJson(getResponse.body(), taskType);
        assertNotNull(actual);
        assertEquals(1, actual.size());

        URI uri2 = URI.create("http://localhost:8080/tasks/task/1");
        HttpRequest getByIdRequest = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> getByIdResponse = client.send(getByIdRequest, HttpResponse.BodyHandlers.ofString());
        Task taskFromJson = gson.fromJson(getByIdResponse.body(), Task.class);
        assertNotNull(taskFromJson);
        assertEquals(1, taskFromJson.getId());
        assertEquals("Task1", taskFromJson.getTitle());

        HttpRequest deleteByIdRequest = HttpRequest.newBuilder().uri(uri2).DELETE().build();
        HttpResponse<String> deleteByIdResponse = client.send(deleteByIdRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteByIdResponse.statusCode());
        assertTrue(manager.getTaskList().isEmpty());
    }
    @Test
    void testSubTaskEndpoint() throws IOException, InterruptedException {
        manager.createEpicTask(epic);
        subTask = new SubTask("SubTask1", "SubTask1 Desc"
                , LocalDateTime.of(2022, 1,1,4,0,0), 15, epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        String epicToSave = gson.toJson(subTask);
        final HttpRequest.BodyPublisher epicToSaveBody = HttpRequest.BodyPublishers.ofString(epicToSave);
        HttpRequest requestToPostEpic = HttpRequest.newBuilder().uri(uri).POST(epicToSaveBody).build();
        HttpResponse<String> responseToPostEpic = client.send(requestToPostEpic, HttpResponse.BodyHandlers.ofString());
        subTask = manager.getSubTaskById(2);
        assertEquals(201, responseToPostEpic.statusCode());
        assertEquals(2, manager.getSubTaskList().get(0).getId());
        assertEquals("SubTask1", manager.getSubTaskList().get(0).getTitle());
        assertEquals(manager.getEpicTaskById(1).getSubTasks().get(2), subTask);

        subTask.setStatus(Status.IN_PROGRESS);
        String epicToUpdate = gson.toJson(subTask);
        final HttpRequest.BodyPublisher updateEpicBody = HttpRequest.BodyPublishers.ofString(epicToUpdate);
        HttpRequest requestToUpdateEpic = HttpRequest.newBuilder().uri(uri).POST(updateEpicBody).build();
        client.send(requestToUpdateEpic, HttpResponse.BodyHandlers.ofString());
        epic = manager.getEpicTaskById(1);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        assertEquals(Status.IN_PROGRESS, subTask.getStatus());

        HttpRequest getRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        Type subTaskType = new TypeToken< ArrayList<SubTask> >() {}.getType();
        List<SubTask> actual = gson.fromJson(getResponse.body(), subTaskType);
        assertNotNull(actual);
        assertEquals(1, actual.size());

        URI uri2 = URI.create("http://localhost:8080/tasks/subtask/2");
        HttpRequest getByIdRequest = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> getByIdResponse = client.send(getByIdRequest, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskFromJson = gson.fromJson(getByIdResponse.body(), SubTask.class);
        assertNotNull(subTaskFromJson);
        assertEquals(2, subTaskFromJson.getId());
        assertEquals("SubTask1", subTaskFromJson.getTitle());
        assertEquals(epic.getSubTasks().size(), 1);

        HttpRequest deleteByIdRequest = HttpRequest.newBuilder().uri(uri2).DELETE().build();
        HttpResponse<String> deleteByIdResponse = client.send(deleteByIdRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteByIdResponse.statusCode());
        assertTrue(manager.getSubTaskList().isEmpty());
        assertTrue(epic.getSubTasks().isEmpty());
    }

    @Test
    void testEpicTaskEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        String subTaskToSave = gson.toJson(epic);
        final HttpRequest.BodyPublisher postSubTaskBody = HttpRequest.BodyPublishers.ofString(subTaskToSave);
        HttpRequest requestToPostSubTask = HttpRequest.newBuilder().uri(uri).POST(postSubTaskBody).build();
        HttpResponse<String> responseToPostSubTask = client.send(requestToPostSubTask, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseToPostSubTask.statusCode());
        assertEquals(1, manager.getEpicTaskList().get(0).getId());
        assertEquals("EpicTask1", manager.getEpicTaskList().get(0).getTitle());
        epic = manager.getEpicTaskById(1);

        subTask.setEpicTaskId(1);
        SubTask subTask2 = new SubTask("SubTask1", "SubTask1 Desc"
                , LocalDateTime.of(2022, 1,1,6,0,0), 15, epic);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);

        String subTaskToUpdate = gson.toJson(epic);
        final HttpRequest.BodyPublisher updateSubTaskBody = HttpRequest.BodyPublishers.ofString(subTaskToUpdate);
        HttpRequest requestToUpdateSubTask = HttpRequest.newBuilder().uri(uri).POST(updateSubTaskBody).build();
        client.send(requestToUpdateSubTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(epic.getSubTasks().size(), 2);

        HttpRequest getRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        Type epicTaskType = new TypeToken< ArrayList<EpicTask> >() {}.getType();
        List<EpicTask> actual = gson.fromJson(getResponse.body(), epicTaskType);
        assertNotNull(actual);
        assertEquals(1, actual.size());

        URI uri2 = URI.create("http://localhost:8080/tasks/epic/1");
        HttpRequest getByIdRequest = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> getByIdResponse = client.send(getByIdRequest, HttpResponse.BodyHandlers.ofString());
        EpicTask epicTaskFromJson = gson.fromJson(getByIdResponse.body(), EpicTask.class);
        assertNotNull(epicTaskFromJson);
        assertEquals(1, epicTaskFromJson.getId());
        assertEquals("EpicTask1", epicTaskFromJson.getTitle());

        HttpRequest deleteByIdRequest = HttpRequest.newBuilder().uri(uri2).DELETE().build();
        HttpResponse<String> deleteByIdResponse = client.send(deleteByIdRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteByIdResponse.statusCode());
        assertTrue(manager.getSubTaskList().isEmpty());
        assertTrue(manager.getEpicTaskList().isEmpty());
        assertNull(manager.getSubTaskById(1));
    }

    @Test
    void testInfoEndpoint() throws IOException, InterruptedException {
        manager.createTask(task);
        manager.createEpicTask(epic);
        subTask.setEpicTaskId(epic.getId());
        SubTask subTask2 = new SubTask("SubTask1", "SubTask1 Desc"
                , LocalDateTime.of(2022, 1,1,6,0,0), 15, epic);
        manager.createSubTask(subTask);
        manager.createSubTask(subTask2);
        manager.getTaskById(1);
        manager.getEpicTaskById(2);
        manager.getSubTaskById(3);
        manager.getSubTaskById(4);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest getRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        Type listType = new TypeToken<List<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(getResponse.body(), listType);
        assertFalse(actual.isEmpty());
        assertEquals(3, actual.size());

        uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest getHistoryRequest = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> getHistoryResponse = client.send(getHistoryRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getHistoryResponse.statusCode());
        List<Task> actual1 = gson.fromJson(getHistoryResponse.body(), listType);
        assertFalse(actual1.isEmpty());
        assertEquals(4, actual1.size());
    }
}