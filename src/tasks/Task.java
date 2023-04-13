package tasks;

import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    @Expose
    protected final String title;
    @Expose
    protected final String description;
    @Expose
    protected int id = 0;
    @Expose
    protected Status status;
    @Expose
    protected LocalDateTime startTime;
    @Expose
    protected Duration duration;

    public Task(String title, String description, LocalDateTime startTime, int minutes) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(minutes);
        status = Status.NEW;
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

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
        return String.format("%d,Task,%s,%s,%s,%s,%d", id, title, status, description, startTime.format(formatter),
                duration.getSeconds());
    }
}
