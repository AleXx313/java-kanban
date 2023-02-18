package util;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Managers {

    private static final TaskManager manager = new InMemoryTaskManager();
    private static final HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault(){
        return manager;
    }

    public static HistoryManager getDefaultHistory(){
        return historyManager;
    }

}
