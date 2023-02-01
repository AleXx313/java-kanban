import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int MAX_HISTORY_LENGTH = 10;
    private final List<Task> viewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (viewHistory.size() >= MAX_HISTORY_LENGTH) {
            viewHistory.remove(0);
        }
        viewHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return viewHistory;
    }
}
