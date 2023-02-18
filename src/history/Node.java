package history;

import tasks.Task;

public class Node {
    //Данные хранимые в узле - объекты класса Tasks.Task
    private final Task task;
    //Ссылка на предыдущую ноду. Если это хвост, то ссылка на null

    private Node prev;
    //Ссылка на следующую ноду. Если это голова, то ссылка на null
    private Node next;

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

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
