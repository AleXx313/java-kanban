package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    protected final String title;
    protected final String description;
    protected int id = 0;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String title, String description, LocalDateTime startTime, int minutes) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(minutes);
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime(){
        return this.startTime;
    }
    public Duration getDuration(){
        return this.duration;
    }

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
        return String.format("%d,Task,%s,%s,%s,%s,%d", id, title, status, description, startTime.format(formatter),
                duration.getSeconds());
    }
}
