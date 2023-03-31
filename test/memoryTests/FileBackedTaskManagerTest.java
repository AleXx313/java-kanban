package memoryTests;

import managerTests.TaskManagerTest;
import memory.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void setUp() {
        File file = new File("test/data.csv");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        taskManager = new FileBackedTaskManager(file);
    }

    @AfterEach
    public void deleteTestFileAfterTest() {
        try {
            Files.delete(Paths.get("test/data.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Проверить загрузку таски, эпика и сабтаски (в файле все есть)
    @Test
    public void shouldLoadEveryTaskTypeFromFile() {
        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(2);
        taskManager.getSubTaskById(3);

        taskManager.loadFile();

        assertEquals(List.of(taskManager.getTaskById(1), taskManager.getEpicTaskById(2),
                taskManager.getSubTaskById(3)), taskManager.getHistory());

        assertEquals("Test addNewTask", taskManager.getTaskById(1).getTitle());
        assertEquals("Test addNewEpicTask", taskManager.getEpicTaskById(2).getTitle());
        assertEquals("Test addNewSubTask", taskManager.getSubTaskById(3).getTitle());

        assertEquals(Status.NEW, taskManager.getTaskById(1).getStatus());
        assertEquals(Status.NEW, taskManager.getEpicTaskById(2).getStatus());
        assertEquals(Status.NEW, taskManager.getSubTaskById(3).getStatus());

        assertEquals(LocalDateTime.of(2022,03,29,6,0,0),
                taskManager.getTaskById(1).getStartTime());
        assertEquals(LocalDateTime.of(2022,03,29,8,0,0),
                taskManager.getEpicTaskById(2).getStartTime());
        assertEquals(LocalDateTime.of(2022,03,29,8,0,0),
                taskManager.getSubTaskById(3).getStartTime());

        assertEquals(Duration.ofMinutes(60), taskManager.getTaskById(1).getDuration());
        assertEquals(Duration.ofMinutes(60), taskManager.getEpicTaskById(2).getDuration());
        assertEquals(Duration.ofMinutes(60), taskManager.getSubTaskById(3).getDuration());


        assertNull(taskManager.getTaskById(9));
        assertNull(taskManager.getTaskById(0));

        taskManager.removeSubTask(3);
        taskManager.removeEpicTask(2);
        taskManager.removeTask(1);
    }

    //Проверить загрузку таски, эпика и сабтаски (в файле ничего нет)
    @Test
    public void shouldReturnNullTasksAndEmptyHistoryIfFIleIsEmpty() {
        taskManager.loadFile();
        assertNull(taskManager.getTaskById(1));
        assertNull(taskManager.getEpicTaskById(2));
        assertNull(taskManager.getSubTaskById(3));
        assertEquals(Collections.EMPTY_LIST, taskManager.getHistory());
    }

    @Test
    public void shouldReturnEpicTaskAndEmptyHistory() {
        taskManager.createEpicTask(epicTask);
        taskManager.loadFile();

        assertEquals(Collections.EMPTY_LIST, taskManager.getHistory());
        assertEquals(Status.NEW, taskManager.getEpicTaskById(1).getStatus());
        assertEquals("Test addNewEpicTask", taskManager.getEpicTaskById(1).getTitle());
        assertTrue(taskManager.getEpicTaskById(1).getSubTasks().isEmpty());
    }
}