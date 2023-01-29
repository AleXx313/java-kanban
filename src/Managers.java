public class Managers {

    private static final TaskManager manager = new InMemoryTaskManager();
    private static final HistoryManager historyManager = new InMemoryHistoryManager();

    public TaskManager getDefault(){
        return manager;
    }

    public static HistoryManager getDefaultHistory(){
        return historyManager;
    }

}
