import com.google.gson.Gson;
import manager.TaskManager;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import util.Managers;

import java.time.LocalDateTime;

public class Runner {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task1", "Task1 Desc"
                , LocalDateTime.of(2022, 1,1,0,0,0), 15);
        Task task2 = new Task("Task2", "Task2 Desc"
                , LocalDateTime.of(2022, 1,1,1,0,0), 15);
        Task task3 = new Task("Task3", "Task3 Desc"
                , LocalDateTime.of(2022, 1,1,2,0,0), 15);
        Task task4 = new Task("Task4", "Task4 Desc"
                , LocalDateTime.of(2022, 1,1,3,0,0), 15);
        EpicTask epicTask1 = new EpicTask("EpicTask1", "EpicTask1 Desc"
                , LocalDateTime.of(2022, 1,1,0,0,0), 15);
        EpicTask epicTask2 = new EpicTask("EpicTask2", "EpicTask2 Desc"
                , LocalDateTime.of(2022, 1,1,0,0,0), 15);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.createTask(task4);
        manager.createEpicTask(epicTask1);
        manager.createEpicTask(epicTask2);

        SubTask subTask1 = new SubTask("SubTask1", "SubTask1 Desc"
                , LocalDateTime.of(2022, 1,1,4,0,0), 15, epicTask1);
        SubTask subTask2 = new SubTask("SubTask2", "SubTask2 Desc"
                , LocalDateTime.of(2022, 1,1,5,0,0), 15, epicTask1);
        SubTask subTask3 = new SubTask("SubTask3", "SubTask3 Desc"
                , LocalDateTime.of(2022, 1,1,6,0,0), 15, epicTask2);


        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask3);
        manager.getEpicTaskById(5);
        manager.getSubTaskById(9);
        manager.getTaskById(3);
        manager.getEpicTaskById(6);
        manager.getTaskById(4);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getSubTaskById(7);
        manager.getSubTaskById(8);

        manager.removeEpicTask(5);

//        Gson gson = Managers.getDefaultGson();
//        String managerJson = gson.toJson(subTask1);
//        System.out.println(managerJson);
//        SubTask subtask = gson.fromJson(managerJson, SubTask.class);
//
//        manager.createSubTask(subtask);
//        List<SubTask> list = gson.fromJson(managerJson, ArrayList.class);
//
//        System.out.println(list);



        // Переделать на создание сабтаски по id эпика
    }
}
