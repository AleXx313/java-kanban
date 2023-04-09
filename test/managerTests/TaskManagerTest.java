package managerTests;

import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.EpicTask;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;
    public abstract void setUp();

    @BeforeEach
    public void createTask() {
        task = new Task("Test addNewTask", "Test addNewTask description",
                LocalDateTime.of(2022,03,29,6,0,0), 60);
        task.setId(1);
        epicTask = new EpicTask("Test addNewEpicTask", "Test addNewEpicTask description",
                LocalDateTime.of(2022,03,29,0,0,0), 60);
        epicTask.setId(2);
        subTask = new SubTask("Test addNewSubTask", "Test addNewSubTask description",
                LocalDateTime.of(2022,03,29,8,0,0), 60, epicTask);
        subTask.setId(3);
    }

    //Тесты по созданию задач одновременно проверяют и сохранение задач в контейнерах, а также работы методов по
    //получению списков задач каждого типа. Кроме того проверяется работа методов по получению задачи по id.
    @Test
    void testCreateNewTaskMethod() {
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        String title = "Test addNewTask";
        assertEquals(title, task.getTitle());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testCreateNewEpicTaskMethod() {
        taskManager.createEpicTask(epicTask);
        final int epicTaskId = epicTask.getId();
        final EpicTask savedEpicTask = taskManager.getEpicTaskById(epicTaskId);

        String title = "Test addNewEpicTask";
        assertEquals(title, epicTask.getTitle());
        assertNotNull(savedEpicTask, "Эпик не найден.");
        assertEquals(epicTask, savedEpicTask, "Эпики не совпадают.");

        final List<EpicTask> epicTasks = taskManager.getEpicTaskList();
        assertNotNull(epicTasks, "Эпики не возвращаются.");
        assertEquals(1, epicTasks.size(), "Неверное количество эпиков.");
        assertEquals(epicTask, epicTasks.get(0), "Эпики не совпадают.");
    }

    @Test
    void testCreateNewSubTaskMethod() {
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        final int subTaskId = subTask.getId();
        final SubTask savedSubTask = taskManager.getSubTaskById(subTaskId);

        String title = "Test addNewSubTask";
        assertEquals(title, subTask.getTitle());
        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
        assertNotNull(subTask.getEpicTask(), "У подзадачи отсутствует Эпик");
        assertEquals(subTask.getEpicTask(), epicTask, "У подзадачи неверный Эпик");

        final List<SubTask> subTasks = taskManager.getSubTaskList();
        assertNotNull(subTasks, "Подзадачи на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");

        final List<SubTask> subTasksByEpic = taskManager.getSubTaskListByEpic(1);
        assertNotNull(subTasks, "Подзадачи по эпику не возвращаются.");
        assertEquals(1, subTasksByEpic.size(), "Неверное количество подзадач по эпику.");
        assertEquals(subTask, subTasksByEpic.get(0), "Подзадачи по эпику не совпадают.");
    }

    //Получаем по идентификатору если он 0
    @Test
    void testToGetTaskByIdWithZeroId() {
        Task task = taskManager.getTaskById(0);
        assertNull(task, "Задача не null, хотя задачи с таким id не существует!");
    }

    @Test
    void testToGetEpicTaskByIdWithZeroId() {
        EpicTask task = taskManager.getEpicTaskById(0);
        assertNull(task, "Задача не null, хотя подзадачи с таким id не существует!");
    }

    @Test
    void testToGetSubTaskByIdWithZeroId() {
        SubTask task = taskManager.getSubTaskById(0);
        assertNull(task, "Задача не null, хотя эпика с таким id не существует!");
    }

    //Получаем по идентификатору если он 9999
    @Test
    void testToGetTaskByIdWithOutOfBoundId() {
        Task task = taskManager.getTaskById(9999);
        assertNull(task, "Задача не null, хотя задачи с таким id не существует!");
    }

    @Test
    void testToGetEpicTaskByIdWithOutOfBoundId() {
        EpicTask task = taskManager.getEpicTaskById(9999);
        assertNull(task, "Задача не null, хотя подзадачи с таким id не существует!");
    }

    @Test
    void testToGetSubTaskByIdWithOutOfBoundId() {
        SubTask task = taskManager.getSubTaskById(9999);
        assertNull(task, "Задача не null, хотя эпика с таким id не существует!");
    }

    //Получаем по идентификатору если он 1
    @Test
    void testToGetTaskByIdWithExistId() {
        taskManager.createTask(task);
        final int taskId = task.getId();
        Task taskExpected = taskManager.getTaskById(taskId);
        assertEquals(taskExpected, task, "Задача не null, хотя задачи с таким id не существует!");
    }

    @Test
    void testToGetEpicTaskByIdWithExistId() {
        taskManager.createEpicTask(epicTask);
        final int epicTaskId = epicTask.getId();
        EpicTask taskExpected = taskManager.getEpicTaskById(epicTaskId);
        assertEquals(taskExpected, epicTask, "Задача не null, хотя подзадачи с таким id не существует!");
    }

    @Test
    void testToGetSubTaskByIdWithExistId() {
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        final int subTaskId = subTask.getId();
        SubTask taskExpected = taskManager.getSubTaskById(subTaskId);
        assertEquals(taskExpected, subTask, "Задача не null, хотя эпика с таким id не существует!");
    }

    //Получение листа до создания таски 3
    @Test
    void shouldReturnEmptyListsWhenThereWasNoTaskCreated() {
        assertEquals(Collections.EMPTY_LIST, taskManager.getTaskList(), "Лист с задачами не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getEpicTaskList(), "Лист с эпиками не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskList(), "Лист с подзадачами не пустой!");
    }

    //Получение листа сабок до создания
    @Test
    void shouldReturnEmptyListsWhenThereWasNoSubTaskCreated() {
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskListByEpic(2),
                "Лист с подзадачами не пустой!");
    }

    //Проверка методов по удалению данных в контейнерах
    @Test
    void shouldReturnEmptyListsWhenListWithTasksAndEpicsWasCleared() {
        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.clearTasks();
        taskManager.clearEpicTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getTaskList(), "Лист с задачами не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getEpicTaskList(), "Лист с эпиками не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskList(), "Лист с подзадачами не пустой!");
    }

    @Test
    void shouldReturnEmptyListsWhenListWithSubTasksWasCleared() {
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.clearSubTasks();
        assertEquals(Collections.EMPTY_LIST, taskManager.getEpicTaskList(), "Лист с эпиками не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskList(), "Лист с подзадачами не пустой!");
    }

    //Проверка обновления статуса
    @Test
    void shouldUpdateStatus() {
        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        assertEquals(Status.NEW, task.getStatus(), "Статус не NEW, хотя задача только создана!");
        assertEquals(Status.NEW, epicTask.getStatus(), "Статус не NEW, хотя эпик задача только создана!");
        assertEquals(Status.NEW, subTask.getStatus(), "Статус не NEW, хотя подзадача только создана!");

        Status inProgress = Status.IN_PROGRESS;
        task.setStatus(inProgress);
        subTask.setStatus(inProgress);
        taskManager.updateTask(task);
        taskManager.updateSubTask(subTask);
        assertEquals(Status.IN_PROGRESS, task.getStatus(), "Статус не IN_PROGRESS, хотя статус обновлен");
        assertEquals(Status.IN_PROGRESS, epicTask.getStatus(), "Статус не IN_PROGRESS," +
                "хотя статус у подзадачи эпика IN_PROGRESS!");
        assertEquals(Status.IN_PROGRESS, subTask.getStatus(), "Статус не IN_PROGRESS, хотя статус обновлен!");

        Status done = Status.DONE;
        task.setStatus(done);
        subTask.setStatus(done);
        taskManager.updateTask(task);
        taskManager.updateSubTask(subTask);
        assertEquals(Status.DONE, task.getStatus(), "Статус не DONE, хотя статус обновлен");
        assertEquals(Status.DONE, epicTask.getStatus(), "Статус не DONE," +
                "хотя статус у подзадачи эпика DONE!");
        assertEquals(Status.DONE, subTask.getStatus(), "Статус не DONE, хотя статус обновлен!");

    }
    //Проверка удаления задач
    @Test
    public void shouldReturnEmptyTaskListsWhenTasksRemoved(){
        taskManager.createTask(task);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.removeTask(task.getId());
        taskManager.removeEpicTask(epicTask.getId());

        assertEquals(Collections.EMPTY_LIST, taskManager.getTaskList(), "Лист с задачами не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getEpicTaskList(), "Лист с эпиками не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskList(), "Лист с подзадачами не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskListByEpic(2),
                "Лист с подзадачами не пустой!");

        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.removeSubTask(subTask.getId());
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskList(), "Лист с подзадачами не пустой!");
        assertEquals(Collections.EMPTY_LIST, taskManager.getSubTaskListByEpic(2),
                "Лист с подзадачами не пустой!");
    }
    //Проверка получения списка истории
    @Test
    public void shouldReturnEmptyHistoryListWhenNoTaskWasTouched(){
        assertEquals(Collections.EMPTY_LIST, taskManager.getHistory(), "История не пустая!");
    }
    @Test
    public void shouldReturnHistoryListWithSizeOneWhenOneTaskWasTouched(){
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        assertEquals(1, taskManager.getHistory().size(), "Количество записей в истории не равно 1!");
    }

    @Test
    public void shouldRemoveTaskByIdIfTaskWithIdExist(){
        taskManager.createTask(task);
        taskManager.removeTask(task.getId());
        assertNull(taskManager.getTaskById(task.getId()));
    }

    @Test
    public void shouldRemoveEpicAndSubByEpicIdIfEpicWithIdExist(){
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.removeEpicTask(epicTask.getId());
        assertNull(taskManager.getEpicTaskById(epicTask.getId()));
        assertNull(taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void shouldRemoveSubByIdIfSubWithIdExist(){
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.removeSubTask(subTask.getId());
        assertNotNull(taskManager.getEpicTaskById(epicTask.getId()));
        assertNull(taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void getPrioritizedShouldReturnListWithAscendingOrder(){
        Task task2 = new Task("TestTask2", "TestTask2",
                LocalDateTime.of(2022, 03,29,5,0,0), 59);
        Task task3 = new Task("TestTask3", "TestTask3",
                LocalDateTime.of(2022, 03,29,4,0,0), 59);
        Task task4 = new Task("TestTask4", "TestTask4",
                LocalDateTime.of(2022, 03,29,3,0,0), 59);
        SubTask subTask2 = new SubTask("TestSubTask2", "TestSubTask2",
                LocalDateTime.of(2022,03,29,12,0,0), 59, epicTask);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        assertEquals(List.of(task4, task3, task2, task, subTask, subTask2), taskManager.getPrioritizedTasks());
    }

    @Test
    public void shouldNotCreateNewTaskIfItOverlapExistingTasks(){
        Task task2 = new Task("TestTask2", "TestTask2",
                LocalDateTime.of(2022, 03,29,5,45,0), 60);
        Task task3 = new Task("TestTask3", "TestTask3",
                LocalDateTime.of(2022, 03,29,5,01,0), 60);
        Task task4 = new Task("TestTask4", "TestTask4",
                LocalDateTime.of(2022, 03,29,6,59,0), 60);
        SubTask subTask2 = new SubTask("TestSubTask2", "TestSubTask2",
                LocalDateTime.of(2022,03,29,8,30,0), 60, epicTask);
        taskManager.createTask(task);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        assertEquals(List.of(task, subTask), taskManager.getPrioritizedTasks());
    }
}
