package memoryTests;

import managerTests.TaskManagerTest;
import memory.FileBackedTaskManager;

import memory.ManagerSaveException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import tasks.Status;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new FileBackedTaskManager(new File("test/data.csv"));
    }

    @AfterAll
    public static void deleteTestFileAfterAllTests() {
        try {
            Files.delete(Paths.get("test/data.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void clearStaticContainers() {
        taskManager.removeSubTask(subTask.getId());
        taskManager.removeEpicTask(epicTask.getId());
        taskManager.removeTask(task.getId());
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