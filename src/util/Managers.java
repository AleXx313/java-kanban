package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import history.HistoryManager;
import history.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import memory.FileBackedTaskManager;
import server.HttpTaskManager;
import tasks.Status;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    private static HistoryManager historyManager;
    private static TaskManager manager;

    private static FileBackedTaskManager fileBackedTaskManager;

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078");

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultInMemory() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.loadFile();
        return manager;
    }

    public static Gson getDefaultGson() {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Status.class, new StatusAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        return gson;
    }

}
