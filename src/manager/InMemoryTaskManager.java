package manager;

import history.HistoryManager;
import tasks.*;
import util.Managers;


import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    protected final transient HistoryManager historyManager;

    protected final TreeSet<Task> timeTree = new TreeSet<>(new StartTimeComparator());

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    protected void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    //Добавляем задачи.
    @Override
    public void createTask(Task task) {
        if (isOverlapping(task)) {
            System.out.println("Заданный временной отрезок уже используется! Задача не создана!");
            return;
        }

        task.setId(idCounter);
        idCounter++;
        tasks.put(task.getId(), task);
        timeTree.add(task);
    }

    @Override
    public void createEpicTask(EpicTask epicTask) {

        epicTask.setId(idCounter);
        idCounter++;
        epicTasks.put(epicTask.getId(), epicTask);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (isOverlapping(subTask)) {
            System.out.println("Заданный временной отрезок уже используется! Подзадача не создана!");
            return;
        }
        subTask.setId(idCounter);
        idCounter++;
        subTasks.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
        epicTask.getSubTasks().put(subTask.getId(), subTask);
        epicTask.setStatusBySubtasks();
        epicTask.setTimeBySubtasks();
        timeTree.add(subTask);
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
    public ArrayList<SubTask> getSubTaskListByEpic(int id) {
        if (epicTasks.containsKey(id)){
            EpicTask epicTask = epicTasks.get(id);

            ArrayList<SubTask> allTasks = new ArrayList<>();
            for (Integer key : epicTask.getSubTasks().keySet()) {
                allTasks.add(subTasks.get(key));
            }
            return allTasks;
        }

        return new ArrayList<SubTask>();
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

    @Override
    public void clearSubTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    // Получаем по идентификатору
    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (epicTasks.containsKey(id)) {
            historyManager.add(epicTasks.get(id));
            return epicTasks.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
            return subTasks.get(id);
        }
        return null;
    }

    //Обновление задач
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        EpicTask epicTask = epicTasks.get(subTask.getEpicTaskId());
        if (epicTask.getStatus().equals(Status.NEW)) {
            epicTask.setStatus(Status.IN_PROGRESS);
        }
        subTasks.put(subTask.getId(), subTask);
        epicTask.getSubTasks().put(subTask.getId(), subTask);
        if (epicTask.isDoneCheck()) {
            epicTask.setStatus(Status.DONE);
        }
        epicTask.setTimeBySubtasks();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        epicTasks.put(epicTask.getId(), epicTask);
        epicTask.setTimeBySubtasks();
    }

    //Удаляем задачи
    @Override
    public void removeTask(int id) {
        if (getTaskById(id) != null) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicTask(int id) {
        if (getEpicTaskById(id) != null) {

            List<Integer> subtasksId = new ArrayList<>(epicTasks.get(id).getSubTasks().keySet());
            for (Integer subId : subtasksId){
                removeSubTask(subId);
                historyManager.remove(subId);
            }
            epicTasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubTask(int id) {
        if (getSubTaskById(id) != null) {
            EpicTask epicTask = epicTasks.get(subTasks.get(id).getEpicTaskId());
            epicTask.getSubTasks().remove(id);
            subTasks.remove(id);
            historyManager.remove(id);
            epicTask.getSubTasks().remove(id);
            epicTask.setStatusBySubtasks();
            epicTask.setTimeBySubtasks();
        }
    }

    //Получаем историю просмотров, вызывая одноименный метод экземпляра класса "Менеджер историй".
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(timeTree);
    }

    public boolean isOverlapping(Task task) {
        boolean overlap = false;
        List<Task> tasks = getPrioritizedTasks();
        for (Task taskToCompare : tasks) {
            if (task.getStartTime().isAfter(taskToCompare.getStartTime())
                    && task.getStartTime().isBefore(taskToCompare.getEndTime())
                    || task.getEndTime().isAfter(taskToCompare.getStartTime())
                    && task.getEndTime().isBefore(taskToCompare.getEndTime())) {
                overlap = true;
            }
        }
        return overlap;
    }

    public void clearHistory() {
        for (int i = 0; i <= historyManager.getHistory().size(); i++) {
            historyManager.remove(i + 1);
        }
    }
    public EpicTask utilGetEpicBydId(int id){
        if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        }
        return null;
    }
}
