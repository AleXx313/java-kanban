package tasks;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {


    private EpicTask epicTask;
    @Expose
    private int epicTaskId;


    public SubTask(String title, String description, LocalDateTime startTime, int minutes, EpicTask epicTask) {
        super(title, description, startTime, minutes);
        this.epicTask = epicTask;
        epicTaskId = epicTask.getId();
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

    public void setEpicTaskId(int epicTaskId) {
        this.epicTaskId = epicTaskId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
        return String.format("%d,Task,%s,%s,%s,%s,%d,%d", id, title, status, description, startTime.format(formatter),
                duration.getSeconds(), epicTask.getId());
    }
}
