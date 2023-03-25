package manager;

import tasks.Task;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2){
        return task1.getStartTime().compareTo(task2.getStartTime());
    }

}
