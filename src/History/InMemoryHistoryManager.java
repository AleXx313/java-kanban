package History;

import Tasks.Task;

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
        /*
        Добавил вторую реализацию метода getTask(), без создания второго Листа с Нодами.
        Поскольку в лист изначально добавляются Таски, чтобы получить ссылку на Таску в следующей Ноде,
        <listName>.get(i).next.getTask() недостаточно. Приходится лазить в Мапу. Получается прям длинная цепочка
        присваиваний. Из за этого для повышения читаемости изначально и сделал реализацию через создание
        дополнительного Листа. Вроде бы и там и там получается одинаковая сложность. В одном случае каждый эллемент
        дополнительно вставляем в новый лист, а в другом, для каждого элемента лазим в мапу.

        На всякий случай оставлю 2 варианта=).
        P.S. Спасибо за ревью=)!
         */
        public List<Task> getTasks() {
            List<Task> viewHistory = new ArrayList<>();

            viewHistory.add(tail.getTask());
            int i = 0;
            if (tail.getNext() != null){
                while (historyMap.get(viewHistory.get(i).getId()).getNext() != null){
                    viewHistory.add(historyMap.get(viewHistory.get(i).getId()).getNext().getTask());
                    i++;
                }
            }
            return viewHistory;
        }

//        public List<Task> getTasks() {
//            List<Node> nodes = new ArrayList<>();
//
//            nodes.add(tail);
//            int i = 0;
//
//            if (tail.getNext() != null){
//                while (nodes.get(i).getNext() != null){
//                    nodes.add(nodes.get(i).getNext());
//                    i++;
//                }
//            }
//
//            List<Task> viewHistory = new ArrayList<>();
//            for (Node node : nodes) {
//                viewHistory.add(node.getTask());
//            }
//
//            return viewHistory;
//        }

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
