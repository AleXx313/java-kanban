import java.util.HashMap;

public class EpicTask extends Task {

    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public EpicTask(String title, String description) {
        super(title, description);
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }


    public void setStatusBySubtasks(){
        if (isDoneCheck()){
            setStatus(Status.DONE);
            return;
        }
        boolean isIn_Progress = false;
        for (SubTask subTask : subTasks.values()){
            if (subTask.getStatus().equals(Status.IN_PROGRESS) || subTask.getStatus().equals(Status.DONE)){
                isIn_Progress = true;
                break;
            }
        }
        if (isIn_Progress){
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

    public boolean isEmptyCheck(){
        return subTasks.isEmpty();
    }



}
