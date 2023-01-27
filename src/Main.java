public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        EpicTask epicTask1 = new EpicTask("Поклеить обои", "Нужно купить и поклеить обои в гостинной");
        SubTask subTask1 = new SubTask("Покупка обоев", "Нужно купить обои", epicTask1);
        SubTask subTask2 = new SubTask("Поклейка", "Нужно поклеить обои", epicTask1);
        EpicTask epicTask2 = new EpicTask("Поход за продуктами", "Необходимо сходить за продуктами");
        SubTask subTask3 = new SubTask("Продукты", "Нужно пойти за продуктами", epicTask2);
        Task task1 = new Task("ТЗ", "Нужно дописать ТЗ");
        Task task2 = new Task("Книга", "Нужно дочитать книгу");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpicTask(epicTask1);
        taskManager.createEpicTask(epicTask2);
        taskManager.createSubTask(subTask3);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask1);

        System.out.println("Проверка создания и хранения методов");
        System.out.println("-------------------------------------------------------");
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicTaskList());
        System.out.println("Проверка получения списка сабов по эпику");
        System.out.println("-------------------------------------------------------");
        System.out.println(taskManager.getSubTaskListByEpic(epicTask1));
        System.out.println("_______________________________________________________");

        System.out.println("Проверка обновления методов");
        System.out.println("-------------------------------------------------------");
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        taskManager.updateTask(task2);

        System.out.println(taskManager.getTaskList());
        System.out.println("_______________________________________________________");
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        System.out.println(taskManager.getTaskList());
        System.out.println("_______________________________________________________");

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubTask(subTask3);
        taskManager.updateSubTask(subTask2);
        taskManager.updateSubTask(subTask1);

        subTask3.setStatus(Status.DONE);
        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask3);
        taskManager.updateSubTask(subTask1);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicTaskList());
        System.out.println("_______________________________________________________");

        System.out.println("Находим и удаляем по id");
        System.out.println("-------------------------------------------------------");
        System.out.println("Найдены задачи 4 и 1");
        System.out.println("-------------------------------------------------------");
        System.out.println(taskManager.getEpicTaskById(4));
        System.out.println(taskManager.getTaskById(1));
        System.out.println("_______________________________________________________");
        System.out.println("Удалены задачи 4 и 1");
        System.out.println("-------------------------------------------------------");
        taskManager.removeEpicTask(4);
        taskManager.removeTask(1);
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubTaskList());
        System.out.println(taskManager.getEpicTaskList());
        System.out.println("_______________________________________________________");
    }
}
