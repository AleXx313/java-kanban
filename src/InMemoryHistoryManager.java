import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList viewHistory = new CustomLinkedList();

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

    class CustomLinkedList {

        Node head = null;
        Node tail = null;
        //Таблица для хранения и поиска Ноды по Id
        Map<Integer, Node> historyMap = new HashMap<>();

        public void linkLast(Task task) {
            int id = task.getId();
            if (historyMap.containsKey(id)) {
                removeNode(historyMap.get(id));
                historyMap.remove(id);
            }
            Node t = head;
            Node newNode = new Node(task, t, null);
            head = newNode;
            if (t == null) {
                tail = newNode;
            } else {
                t.next = newNode;
            }
            historyMap.put(id, newNode);
        }

        public List<Task> getTasks() {
            List<Node> nodes = new ArrayList<>();

            nodes.add(tail);
            int i = 0;

            if (tail.next != null){
                do {
                    nodes.add(nodes.get(i).next);
                    i++;
                } while (nodes.get(i).next != null);
            }

            List<Task> viewHistory = new ArrayList<>();

            for (Node node : nodes) {
                viewHistory.add(node.getTask());
            }

            return viewHistory;
        }

        public void removeNode(Node node) {
            Node prev = node.prev; //Сохраняем ссылку на предыдущую ноду
            Node next = node.next; //Сохраняем сслыку на следующую ноду

            if (prev == null) { // Если предыдущая нода null
                tail = next;  //текущая удаляется, а следующая становится хвостом
            } else { //Иначе
                prev.next = next; // Следующая становится следующей для предыдущей
                node.prev = null; // Разрываем ссылку на предыдущую у текущей (По аналогии с кодом в LinkedList)
            }
            if (next == null) { //По аналогии для следующей Ноды.
                head = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
        }
    }
}
