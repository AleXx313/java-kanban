package Util;

import History.HistoryManager;
import History.InMemoryHistoryManager;
import Manager.InMemoryTaskManager;
import Manager.TaskManager;

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
