package historyTests;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    protected HistoryManager historyManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;

    @BeforeEach
    public void createTask() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Test addNewTask", "Test addNewTask description");
        task.setId(1);
        epicTask = new EpicTask("Test addNewEpicTask", "Test addNewEpicTask description");
        epicTask.setId(2);
        subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description", epicTask);
        subTask.setId(3);
    }
    @AfterEach
    public void clearHistory(){
        historyManager.remove(1);
        historyManager.remove(2);
        historyManager.remove(3);
    }
    //Проверка
    @Test
    void shouldAddTasksOnlyDuplicateNotAllowed() {
        assertNotNull(historyManager.getHistory(), "Лист историй null!");
        assertEquals(Collections.EMPTY_LIST, historyManager.getHistory(), "Лист историй не пустой!");

        historyManager.add(task);
        assertEquals(List.of(task), historyManager.getHistory(),"1 задача не добавилась в лист историй!");

        historyManager.add(epicTask);
        historyManager.add(subTask);
        assertEquals(List.of(task, epicTask, subTask), historyManager.getHistory(), "Задачи не добавились в " +
                "лист историй или добавились не в правильном порядке!");

        historyManager.add(task);
        assertEquals(List.of(epicTask, subTask, task), historyManager.getHistory(), "Задача задублировалась " +
                "или не стала последней вызванной!");
        historyManager.remove(1);
        assertEquals(List.of(epicTask, subTask), historyManager.getHistory(), "Не выполнено удаление с конца!");
        historyManager.add(task);
        historyManager.remove(2);
        assertEquals(List.of(subTask, task), historyManager.getHistory(), "Не выполнено удаление с начала!");
        historyManager.add(epicTask);
        historyManager.remove(1);
        assertEquals(List.of(subTask, epicTask), historyManager.getHistory(), "Не выполнено удаление" +
                "из середины!");
    }
}