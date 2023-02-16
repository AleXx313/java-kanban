public class Node {
    //Данные хранимые в узле - объекты класса Task
    private final Task task;
    //Ссылка на предыдущую ноду. Если это хвост, то ссылка на null
    Node prev;
    //Ссылка на следующую ноду. Если это голова, то ссылка на null
    Node next;

    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    public Task getTask() {
        return task;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }
}
