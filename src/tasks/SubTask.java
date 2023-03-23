package tasks;

public class SubTask extends Task {

    private EpicTask epicTask;

    public SubTask(String title, String description, EpicTask epicTask) {
        super(title, description);
        this.epicTask = epicTask;
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    @Override
    public String toString() {
        return String.format("%d,SubTask,%s,%s,%s,%d", id, title, status, description, epicTask.id);
    }
}
