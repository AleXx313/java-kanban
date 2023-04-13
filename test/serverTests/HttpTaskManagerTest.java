package serverTests;

import managerTests.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskManager;
import server.KVServer;

import java.io.IOException;

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
}
