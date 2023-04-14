package serverTests;

import managerTests.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskManager;
import server.KVServer;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer server;

    @Override
    @BeforeEach
    public void setUp() {
        try {
            server = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.start();
        taskManager = new HttpTaskManager("http://localhost:8078");
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    public void testLoadFromServer(){
        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);

        List<Task> expectedHistory = taskManager.getHistory();
        List<Task> expectedPrioritized = taskManager.getPrioritizedTasks();
        List<Task> expectedTasks = taskManager.getTaskList();
        List<EpicTask> expectedEpics = taskManager.getEpicTaskList();
        List<SubTask> expectedSubs = taskManager.getSubTaskList();

        HttpTaskManager loadedManager = new HttpTaskManager("http://localhost:8078");

        List<Task> actualHistory = loadedManager.getHistory();
        List<Task> actualPrioritized = loadedManager.getPrioritizedTasks();
        List<Task> actualTasks = loadedManager.getTaskList();
        List<EpicTask> actualEpics = loadedManager.getEpicTaskList();
        List<SubTask> actualSubs = loadedManager.getSubTaskList();

        assertIterableEquals(expectedHistory, actualHistory);
        assertIterableEquals(expectedPrioritized, actualPrioritized);
        assertIterableEquals(expectedTasks, actualTasks);
        assertIterableEquals(expectedEpics, actualEpics);
        assertIterableEquals(expectedSubs, actualSubs);

    }
}
