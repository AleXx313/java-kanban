package memory;

import manager.InMemoryTaskManager;
import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File data = new File("src/data.csv");

    public String toString(Task task){
        return task.toString();
    }

    public List<String> convertTasksToStrings(){
        List<Task> tasks = getTaskList();
        List<EpicTask> epics = getEpicTaskList();
        List<SubTask> subTasks = getSubTaskList();

        List<String> tasksToString = new ArrayList<>();

        for (Task task : tasks){
            tasksToString.add(task.toString());
        }
        for (EpicTask task : epics){
            tasksToString.add(task.toString());
        }
        for (SubTask task : subTasks){
            tasksToString.add(task.toString());
        }
        return tasksToString;
    }
    public String historyToString(){
        List<Task> tasks = historyManager.getHistory();
        StringBuilder builder = new StringBuilder();
        for (Task task : tasks){
            builder.append(task.getId() + ",");
        }
        return builder.substring(0, builder.length()-1);
    }
    public void save(){
        List<String> tasks = convertTasksToStrings();
        try (FileWriter writer = new FileWriter(data, StandardCharsets.UTF_8)){
            writer.write("id,type,name,status,description,epic\n");
            for(String task :tasks){
                writer.write(task + "\n");
            }
            writer.write("\n");
            writer.write(historyToString());

        }catch (IOException e){
            throw new ManagerSaveException();
        }
    }
    // Task fromString(string)
    //save()
    //tSave
    //eSave
    //subSave
    //historySave
    //load


}
