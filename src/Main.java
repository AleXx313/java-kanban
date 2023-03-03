import manager.*;
import memory.FileBackedTaskManager;
import tasks.*;
import util.Managers;

public class Main {

    public static void main(String[] args) {

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

        //создайте две задачи
        Task task1 = new Task("ТЗ", "Нужно дописать ТЗ");
        Task task2 = new Task("Книга", "Нужно дочитать книгу");
        fileBackedTaskManager.createTask(task1);  //1
        fileBackedTaskManager.createTask(task2);  //2
        //эпик с тремя подзадачами
        EpicTask epicTask1 = new EpicTask("Поклеить обои", "Нужно поклеить обои");
        SubTask subTask1 = new SubTask("Покупка обоев", "Нужно купить обои", epicTask1);
        SubTask subTask2 = new SubTask("Поклейка", "Нужно поклеить обои", epicTask1);
        SubTask subTask3 = new SubTask("Любование", "Нужно полюбоваться новыми обоями", epicTask1);
        fileBackedTaskManager.createEpicTask(epicTask1); //3
        fileBackedTaskManager.createSubTask(subTask1); //4
        fileBackedTaskManager.createSubTask(subTask2); //5
        fileBackedTaskManager.createSubTask(subTask3); //6
        //эпик без подзадач
        EpicTask epicTask2 = new EpicTask("Поход за продуктами", "Необходимо сходить за продуктами");
        fileBackedTaskManager.createEpicTask(epicTask2); //7

        fileBackedTaskManager.getTaskById(1);
        fileBackedTaskManager.getTaskById(2);
        fileBackedTaskManager.getEpicTaskById(3);
        fileBackedTaskManager.getEpicTaskById(7);
        fileBackedTaskManager.getSubTaskById(4);
        fileBackedTaskManager.getSubTaskById(5);
        fileBackedTaskManager.getSubTaskById(6);

        fileBackedTaskManager.save();
    }
}
