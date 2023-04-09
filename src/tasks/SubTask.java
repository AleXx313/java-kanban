package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private EpicTask epicTask;

    public SubTask(String title, String description, LocalDateTime startTime, int minutes, EpicTask epicTask) {
        super(title, description, startTime, minutes);
        this.epicTask = epicTask;
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
        return String.format("%d,Task,%s,%s,%s,%s,%d,%d", id, title, status, description, startTime.format(formatter),
                duration.getSeconds(), epicTask.getId());
    }
}
