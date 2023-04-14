package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import memory.FileBackedTaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import util.Managers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String path) {
        client = new KVTaskClient(path);
        gson = Managers.getDefaultGson();
        load();
    }

    @Override
    protected void save() {
        client.put("task", gson.toJson(getTaskList()));
        client.put("epic", gson.toJson(getEpicTaskList()));
        client.put("subtask", gson.toJson(getSubTaskList()));
        client.put("history", gson.toJson(historyByIntegerList()));
        client.put("id", gson.toJson(idCounter));
    }

    private List<Integer> historyByIntegerList() {
        List<Integer> historyById = new ArrayList<>();
        List<Task> history = getHistory();
        for (Task task : history) {
            historyById.add(task.getId());
        }
        return historyById;
    }

    private void load() {
        String loadedJson = client.load("task");
        Type listType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> loadedTasks = gson.fromJson(loadedJson, listType);
        loadTasks(loadedTasks);

        loadedJson = client.load("epic");
        listType = new TypeToken<List<EpicTask>>() {
        }.getType();
        List<EpicTask> loadedEpics = gson.fromJson(loadedJson, listType);
        loadEpics(loadedEpics);

        loadedJson = client.load("subtask");
        listType = new TypeToken<List<SubTask>>() {
        }.getType();
        List<SubTask> loadedSubs = gson.fromJson(loadedJson, listType);
        loadSubs(loadedSubs);

        loadedJson = client.load("history");
        listType = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> loadedHistory = gson.fromJson(loadedJson, listType);
        loadHistory(loadedHistory);

        loadedJson = client.load("id");
        Integer id = gson.fromJson(loadedJson, Integer.class);
        if (id == null){
            return;
        }
        setIdCounter(id);
    }

    private void loadTasks(List<Task> loadedTasks) {
        if (loadedTasks == null){
            return;
        }
        for (Task task : loadedTasks) {
            tasks.put(task.getId(), task);
            timeTree.add(task);
        }
    }

    private void loadEpics(List<EpicTask> loadedEpics) {
        if (loadedEpics == null){
            return;
        }
        for (EpicTask epicTask : loadedEpics) {
            epicTasks.put(epicTask.getId(), epicTask);
        }
    }

    private void loadSubs(List<SubTask> loadedSubs) {
        if (loadedSubs == null){
            return;
        }
        for (SubTask subTask : loadedSubs) {
            EpicTask epic = epicTasks.get(subTask.getEpicTaskId());
            subTask.setEpicTask(epic);
            epic.getSubTasks().put(subTask.getId(), subTask);
            subTasks.put(subTask.getId(), subTask);
            timeTree.add(subTask);
        }
    }

    public void loadHistory(List<Integer> loadedHistory) {
        if (loadedHistory == null){
            return;
        }
        fillHistory(loadedHistory);
    }
}
