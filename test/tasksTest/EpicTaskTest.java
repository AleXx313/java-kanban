package tasksTest;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {

    private InMemoryTaskManager manager;
    private EpicTask epic;
    private SubTask sub1;
    private SubTask sub2;
    @BeforeEach
    public void setUp(){
        manager = new InMemoryTaskManager();
        epic = new EpicTask("Test Epic", "Test Epic Description",
                LocalDateTime.of(2022,03,29,8,0,0), 60);
        epic.setId(1);
        sub1 = new SubTask("Test Sub One", "Test Sub One Description",
                LocalDateTime.of(2022,03,29,8,0,0), 15, epic);
        sub2 = new SubTask("Test Sub Two", "Test Sub Two Description",
                LocalDateTime.of(2022,03,29,8,45,0), 60, epic);
    }

    //Если подзадач нет, то лист с подзадачами пустой, а статус Эпика - NEW;
    @Test
    public void statusShouldBeNewIfSubTasksListIsEmpty(){
        manager.createEpicTask(epic);
        assertTrue(epic.getSubTasks().isEmpty(), "Лист подзадач не пустой, хотя подзадач не создавалось!");
        assertEquals(epic.getStatus(), Status.NEW, "Статус эпика не NEW, хотя подзадач нет!");
    }

    //Если у всех подзадач статус NEW, то и у Эпика статус NEW
    @Test
    public void statusShouldBeNewIfSubtasksStatusIsOnlyNew(){
        manager.createEpicTask(epic);
        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        assertEquals(epic.getStatus(), Status.NEW, "Статус эпика не NEW, хотя статус подзадач не менялся" +
                "и должен быть NEW!");
    }

    //Если у всех подзадач статус DONE, то и у Эпика статус DONE
    @Test
    public void statusShouldBeDoneIfSubtasksStatusIsOnlyDone() {
        manager.createEpicTask(epic);
        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        sub1.setStatus(Status.DONE);
        manager.updateSubTask(sub1);
        sub2.setStatus(Status.DONE);
        manager.updateSubTask(sub2);
        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика не DONE, хотя статус всех подзадач DONE");
    }

    //Если у одной подзадачи статус NEW, а у другой DONE, статус эпика IN_PROGRESS
    @Test
    public void statusShouldBeIn_ProgressIfSubtasksStatusIsDoneAndNew() {
        manager.createEpicTask(epic);
        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        sub2.setStatus(Status.DONE);
        manager.updateSubTask(sub2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус эпика не IN_PROGRESS, хотя статус" +
                "одной из подзадач NEW, а другой из подзадач DONE");
    }

    //Если у одной подзадачи статус NEW, а у другой DONE, статус эпика IN_PROGRESS
    @Test
    public void statusShouldBeIn_ProgressIfSubtasksStatusIsOnlyIn_Progress() {
        manager.createEpicTask(epic);
        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        sub1.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(sub1);
        sub2.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(sub2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус эпика не IN_PROGRESS, хотя статус" +
                "подзадач только IN_PROGRESS!");
    }
    @Test
    public void shouldChangeEpicTimeFieldsDependsOfSubsTime(){
        manager.createEpicTask(epic);
        assertEquals(LocalDateTime.MIN.plusYears(10000), epic.getStartTime(), "Время старта не минимальное, хотя сабтасок нет");
        manager.createSubTask(sub1);
        manager.createSubTask(sub2);
        assertEquals(LocalDateTime.of(2022,03,29,8,0,0),
                epic.getStartTime());
        assertEquals(LocalDateTime.of(2022,03,29,9,45,0),
                epic.getEndTime());
        assertEquals(Duration.ofMinutes(75), epic.getDuration());
    }
}