import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private static int idCounter = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    //Добавляем задачи.
    public void createTask(Task task) {
        task.setStatus(Status.NEW);
        task.setId(idCounter);
        idCounter++;
        tasks.put(task.getId(), task);
    }

    public void createEpicTask(EpicTask epicTask) {
        epicTask.setStatus(Status.NEW);
        epicTask.setId(idCounter);
        idCounter++;
        epicTasks.put(epicTask.getId(), epicTask);
    }

    public void createSubTask(SubTask subTask) {
        subTask.setStatus(Status.NEW);
        subTask.setId(idCounter);
        idCounter++;
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpicTask().getSubTasks().put(subTask.getId(), subTask);
    }

    // Получаем списки задач.
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer key : tasks.keySet()) {
            allTasks.add(tasks.get(key));
        }
        return allTasks;
    }

    public ArrayList<EpicTask> getEpicTaskList() {
        ArrayList<EpicTask> allTasks = new ArrayList<>();
        for (Integer key : epicTasks.keySet()) {
            allTasks.add(epicTasks.get(key));
        }
        return allTasks;
    }

    public ArrayList<SubTask> getSubTaskList() {
        ArrayList<SubTask> allTasks = new ArrayList<>();
        for (Integer key : subTasks.keySet()) {
            allTasks.add(subTasks.get(key));
        }
        return allTasks;
    }

    public ArrayList<SubTask> getSubTaskListByEpic(EpicTask epicTask){
        ArrayList<SubTask> allTasks = new ArrayList<>();
        for (Integer key : epicTask.getSubTasks().keySet()){
            allTasks.add(subTasks.get(key));
        }
        return allTasks;
    }

    //Удаляем задачи
    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    // Получаем по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public EpicTask getEpicTaskById(int id) {
        return epicTasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    //Обновление задач
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

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

    //Удаляем задачи
    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpicTask(int id) {

        for (Integer subTaskId : epicTasks.get(id).getSubTasks().keySet()) {
            subTasks.remove(subTaskId);
        }
        epicTasks.remove(id);

    }

    public void removeSubTask(int id) {
        subTasks.remove(id);
    }

}
