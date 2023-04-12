package tasks;

import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EpicTask extends Task {

    @Expose
    private final HashMap<Integer, SubTask> subTasks;

    private LocalDateTime endTime;

    public EpicTask(String title, String description, LocalDateTime startTime, int duration) {
        super(title, description, startTime, duration);
        this.startTime = LocalDateTime.MIN.plusYears(10000);
        this.duration = Duration.ofMinutes(0);
        subTasks = new HashMap<>();

    }

    public void setEpicIdForSubTasks(){
        for (SubTask subTask : subTasks.values()){
            subTask.setEpicTaskId(this.id);
        }
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public void setTimeBySubtasks(){
        setDurationBySubTasks();
        setEndTimeBySubTasks();
        setStartTimeBySubTasks();
    }

    private void setDurationBySubTasks(){
        Duration duration = Duration.ofMinutes(0);
        for (SubTask subTask : subTasks.values()){
            duration = duration.plus(subTask.getDuration());
        }
        this.duration = duration;
    }

    private void setStartTimeBySubTasks(){
        LocalDateTime startTime = LocalDateTime.MAX;
        for (SubTask subTask : subTasks.values()){
            if(subTask.getStartTime().isBefore(startTime)){
                startTime = subTask.getStartTime();
            }
        }
        this.startTime = startTime;
    }

    private void setEndTimeBySubTasks(){
        LocalDateTime endTime = LocalDateTime.MIN;
        for (SubTask subTask : subTasks.values()){
            if(subTask.getEndTime().isAfter(endTime)){
                endTime = subTask.getEndTime();
            }
        }
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime(){
        return this.endTime;
    }
    public void setStatusBySubtasks(){
        if (isDoneCheck()){
            setStatus(Status.DONE);
            return;
        }
        boolean isInProgress = false;
        for (SubTask subTask : subTasks.values()){
            if (subTask.getStatus().equals(Status.IN_PROGRESS) || subTask.getStatus().equals(Status.DONE)){
                isInProgress = true;
                break;
            }
        }
        if (isInProgress){
            setStatus(Status.IN_PROGRESS);
        } else {
            setStatus(Status.NEW);
        }
    }

    public boolean isDoneCheck() {
        boolean flag = true;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus().equals(Status.IN_PROGRESS) || subTask.getStatus().equals(Status.NEW)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss");
        return String.format("%d,Task,%s,%s,%s,%s,%d", id, title, status, description, startTime.format(formatter),
                duration.getSeconds());
    }


}
