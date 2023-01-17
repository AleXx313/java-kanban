import java.util.HashMap;

public class EpicTask extends Task {

    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public EpicTask(String title, String description) {
        super(title, description);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public boolean isDoneCheck() {
        boolean flag = true;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) {
                flag = false;
                break;
            }
        }
        return flag;
    }
}
