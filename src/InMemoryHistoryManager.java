import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final static int MAX_HISTORY_LENGTH = 10;
    private final List<Task> viewHistory = new ArrayList<>();

    public void add(Task task) {
        if (viewHistory.size() < MAX_HISTORY_LENGTH){
            viewHistory.add(task);
        }else if(viewHistory.size() >= MAX_HISTORY_LENGTH){
            viewHistory.remove(0);
            viewHistory.add(task);
        }
    }
    public List<Task> getHistory() {
        return viewHistory;
    }
}
