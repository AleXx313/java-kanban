public class Managers <T extends TaskManager> {

    private T manager;
    private static HistoryManager historyManager = new InMemoryHistoryManager();

    public TaskManager getDefault(){
        return manager;
    }


    //Не уверен что правильно понял последний пункт ТЗ про рефаторинг History.
    public static HistoryManager getDefaultHistory(){
        return historyManager;
    }

}
