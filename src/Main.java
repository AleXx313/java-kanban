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

        System.out.println("Проверка создания и хранения методов");
        System.out.println("-------------------------------------------------------");
        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());
        System.out.println("Проверка получения списка сабов по эпику");
        System.out.println("-------------------------------------------------------");
        System.out.println(manager.getSubTaskListByEpic(epicTask1));
        System.out.println("_______________________________________________________");

        System.out.println("Проверка обновления методов");
        System.out.println("-------------------------------------------------------");
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        manager.updateTask(task2);

        System.out.println(manager.getTaskList());
        System.out.println("_______________________________________________________");
        task1.setStatus(Status.DONE);
        manager.updateTask(task1);
        System.out.println(manager.getTaskList());
        System.out.println("_______________________________________________________");

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.IN_PROGRESS);

        manager.updateSubTask(subTask3);
        manager.updateSubTask(subTask2);
        manager.updateSubTask(subTask1);

        subTask3.setStatus(Status.DONE);
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask3);
        manager.updateSubTask(subTask1);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());
        System.out.println("_______________________________________________________");

        System.out.println("Находим и удаляем по id");
        System.out.println("-------------------------------------------------------");
        System.out.println("Найдены задачи 4 и 1");
        System.out.println("-------------------------------------------------------");
        System.out.println(manager.getEpicTaskById(4));
        System.out.println(manager.getTaskById(1));
        System.out.println("_______________________________________________________");
        System.out.println("Удалены задачи 4 и 1");
        System.out.println("-------------------------------------------------------");
        manager.removeEpicTask(4);
        manager.removeTask(1);
        System.out.println(manager.getTaskList());
        System.out.println(manager.getSubTaskList());
        System.out.println(manager.getEpicTaskList());
        System.out.println("_______________________________________________________");
    }
}
