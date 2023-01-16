public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        EpicTask epicTask1 = new EpicTask("Поклеить обои", "Нужно купить и поклеить обои в гостинной");
        SubTask subTask1 = new SubTask("Покупка обоев", "Нужно купить обои", epicTask1);
        SubTask subTask2 = new SubTask("Поклейка", "Нужно поклеить обои", epicTask1);
        EpicTask epicTask2 = new EpicTask("Поход за продуктами", "Необходимо сходить за продуктами");
        SubTask subTask3 = new SubTask("Продукты", "Нужно пойти за продуктами", epicTask2);
        Task task1 = new Task("ТЗ", "Нужно дописать ТЗ");
        Task task2 = new Task("Книга", "Нужно дочитать книгу");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpicTask(epicTask1);
        manager.createEpicTask(epicTask2);
        manager.createSubTask(subTask3);
        manager.createSubTask(subTask2);
        manager.createSubTask(subTask1);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());
        System.out.println("_______________________________________________________");
        manager.updateTask(task1);
        manager.updateTask(task2);

        manager.updateSubTask(subTask3);
        manager.updateSubTask(subTask3);

        manager.updateSubTask(subTask2);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        manager.updateSubTask(subTask2);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());
        System.out.println("_______________________________________________________");

        System.out.println(manager.getEpicTaskById(4));
        System.out.println(manager.getTaskById(1));
        System.out.println("_______________________________________________________");
        manager.removeEpicTask(4);
        manager.removeTask(1);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());
        System.out.println("_______________________________________________________");


    }
}
