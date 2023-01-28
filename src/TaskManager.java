import java.util.ArrayList;


public interface TaskManager {

    void createTask(Task task);
    void createEpicTask(EpicTask epicTask);
    void createSubTask(SubTask subTask);

    // Получаем списки задач.
    ArrayList<Task> getTaskList();
    ArrayList<EpicTask> getEpicTaskList();
    ArrayList<SubTask> getSubTaskList();
    ArrayList<SubTask> getSubTaskListByEpic(EpicTask epicTask);

    //Удаляем задачи
    void clearTasks();
    void clearEpicTasks();
    void clearSubTasks();

    // Получаем по идентификатору
    Task getTaskById(int id);
    EpicTask getEpicTaskById(int id);
    SubTask getSubTaskById(int id);

    //Обновление задач
    void updateTask(Task task);
    void updateSubTask(SubTask subTask);
    void updateEpicTask(EpicTask epicTask);

    //Удаляем задачи
    void removeTask(int id);
    void removeEpicTask(int id);
    void removeSubTask(int id);

}