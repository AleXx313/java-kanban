package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EpicTask extends Task {

    private transient final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private transient LocalDateTime endTime;

    public EpicTask(String title, String description, LocalDateTime startTime, int duration) {
        super(title, description, startTime, duration);
        this.startTime = LocalDateTime.MIN;
        this.duration = Duration.ofMinutes(0);

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
