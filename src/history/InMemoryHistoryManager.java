package history;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static final CustomLinkedList viewHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        viewHistory.linkLast(task);
    }

    @Override
    public void remove(int id) {
        viewHistory.removeNode(viewHistory.historyMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return viewHistory.getTasks();
    }

    public static class CustomLinkedList {

        Node head = null;
        Node tail = null;
        //Таблица для хранения и поиска Ноды по Id
        Map<Integer, Node> historyMap = new HashMap<>();

        public void linkLast(Task task) {
            int id = task.getId();
            if (historyMap.containsKey(id)) {
                removeNode(historyMap.get(id));
            }
            Node t = head;
            Node newNode = new Node(task, t, null);
            head = newNode;
            if (t == null) {
                tail = newNode;
            } else {
                t.setNext(newNode);
            }
            historyMap.put(id, newNode);
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            Node node = tail;
            while (node != null){
                tasks.add(node.getTask());
                node = node.getNext();
            }
            return tasks;
        }

        public void removeNode(Node node) {
            Node prev = node.getPrev(); //Сохраняем ссылку на предыдущую ноду
            Node next = node.getNext(); //Сохраняем сслыку на следующую ноду

            if (prev == null) { // Если предыдущая нода null
                tail = next;  //текущая удаляется, а следующая становится хвостом
            } else { //Иначе
                prev.setNext(next); // Следующая становится следующей для предыдущей
                node.setPrev(null); // Разрываем ссылку на предыдущую у текущей (По аналогии с кодом в LinkedList)
            }
            if (next == null) { //По аналогии для следующей Ноды.
                head = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
            historyMap.remove(node.getTask().getId());

        }
    }
}
