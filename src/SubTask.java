public class SubTask extends Task {

    private EpicTask epicTask;

    public SubTask(String title, String description, EpicTask epicTask) {
        super(title, description);
        this.epicTask = epicTask;
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }
}
