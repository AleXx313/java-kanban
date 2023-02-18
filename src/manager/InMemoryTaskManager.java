package Manager;

import History.HistoryManager;
import Tasks.*;
import Util.Managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int idCounter = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //Добавляем задачи.
    @Override
    public void createTask(Task task) {
        task.setStatus(Status.NEW);
        task.setId(idCounter);
        idCounter++;
        tasks.put(task.getId(), task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {
        epicTask.setStatus(Status.NEW);
        epicTask.setId(idCounter);
        idCounter++;
        epicTasks.put(epicTask.getId(), epicTask);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        subTask.setStatus(Status.NEW);
        subTask.setId(idCounter);
        idCounter++;
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpicTask().getSubTasks().put(subTask.getId(), subTask);
        subTask.getEpicTask().setStatusBySubtasks();
    }

    // Получаем списки задач.
    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            allTasks.add(tasks.get(key));
        }
        return allTasks;
    }

    @Override
    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> allTasks = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            allTasks.add(epicTasks.get(key));
        }
        return allTasks;
    }

    @Override
    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> allTasks = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            allTasks.add(subTasks.get(key));
        }
        return allTasks;
    }

    @Override
    public ArrayList<SubTask> getSubTaskListByEpic(EpicTask epicTask) {
        ArrayList<SubTask> allTasks = new ArrayList<>();
        for (Integer key : epicTask.getSubTasks().keySet()) {
            allTasks.add(subTasks.get(key));
        }
        return allTasks;
    }

    //Удаляем задачи
    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    //Думал, что эпик может существовать без сабтаски.
    @Override
    public void clearSubTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    // Получаем по идентификатору
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        historyManager.add(epicTasks.get(id));
        return epicTasks.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    //Обновление задач
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask.getEpicTask().getStatus().equals(Status.NEW)) {
            subTask.getEpicTask().setStatus(Status.IN_PROGRESS);
        }
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpicTask().getSubTasks().put(subTask.getId(), subTask);
        if (subTask.getEpicTask().isDoneCheck()) {
            subTask.getEpicTask().setStatus(Status.DONE);
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
    }

    //Удаляем задачи
    @Override
    public void removeTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicTask(int id) {

        for (Integer subTaskId : epicTasks.get(id).getSubTasks().keySet()) {
            subTasks.remove(subTaskId);
            historyManager.remove(subTaskId);
        }
        epicTasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void removeSubTask(int id) {
        EpicTask epicTask = subTasks.get(id).getEpicTask();
        subTasks.remove(id);
        historyManager.remove(id);
        epicTask.getSubTasks().remove(id);
        epicTask.setStatusBySubtasks();
    }

    //Получаем историю просмотров, вызывая одноименный метод экземпляра класса "Менеджер историй".
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
