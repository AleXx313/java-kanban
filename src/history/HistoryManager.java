package history;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    //Добавление просмотренной задачи в историю задач
    void add(Task task);

    //Удаление просмотренной задачи из истории задач по ID задачи
    void remove(int id);

    //Получение истории задач
    List<Task> getHistory();
}


