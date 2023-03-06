package util;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import memory.FileBackedTaskManager;

import java.io.File;

public class Managers {

    private static HistoryManager historyManager;
    private static TaskManager manager;

    private static FileBackedTaskManager fileBackedTaskManager;
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(File file){
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.loadFile();
        return manager;
    }
}
