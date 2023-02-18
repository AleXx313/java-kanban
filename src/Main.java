import manager.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();


        //создайте две задачи
        Task task1 = new Task("ТЗ", "Нужно дописать ТЗ");
        Task task2 = new Task("Книга", "Нужно дочитать книгу");
        taskManager.createTask(task1);  //1
        taskManager.createTask(task2);  //2
        //эпик с тремя подзадачами
        EpicTask epicTask1 = new EpicTask("Поклеить обои", "Нужно поклеить обои");
        SubTask subTask1 = new SubTask("Покупка обоев", "Нужно купить обои", epicTask1);
        SubTask subTask2 = new SubTask("Поклейка", "Нужно поклеить обои", epicTask1);
        SubTask subTask3 = new SubTask("Любование", "Нужно полюбоваться новыми обоями", epicTask1);
        taskManager.createEpicTask(epicTask1); //3
        taskManager.createSubTask(subTask1); //4
        taskManager.createSubTask(subTask2); //5
        taskManager.createSubTask(subTask3); //6
        //эпик без подзадач
        EpicTask epicTask2 = new EpicTask("Поход за продуктами", "Необходимо сходить за продуктами");
        taskManager.createEpicTask(epicTask2); //7

        //запросите созданные задачи несколько раз в разном порядке;
        //после каждого запроса выведите историю и убедитесь, что в ней нет повторов;
        taskManager.getTaskById(1);
        taskManager.getEpicTaskById(3);
        taskManager.getEpicTaskById(7);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(6); // 6
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(4);
        taskManager.getEpicTaskById(3);
        taskManager.getSubTaskById(4);
        taskManager.getEpicTaskById(7); // 7
        taskManager.getTaskById(1);
        taskManager.getTaskById(2); // 2
        taskManager.getTaskById(1); // 1
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(4); // 4
        taskManager.getEpicTaskById(3); // 3
        taskManager.getSubTaskById(5); // 5
        System.out.println(taskManager.getHistory()); // 6, 7, 2, 1, 4, 3, 5

        //удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться
        taskManager.removeTask(2);
        taskManager.removeEpicTask(7);
        System.out.println(taskManager.getHistory()); // 6, 1, 4, 3, 5
        //удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
        taskManager.removeEpicTask(3);
        System.out.println(taskManager.getHistory()); // 1
    }
}
