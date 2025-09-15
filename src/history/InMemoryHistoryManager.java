package history;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final HashMap<Integer, Node<Task>> hashMap;

    class Node<V> {
        Node<V> next;
        Node<V> prev;
        V value;

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Node<V> node = (Node<V>) o;
            return Objects.equals(next, node.next) && Objects.equals(prev, node.prev) && Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(next, prev, value);
        }

        public Node(V value, Node<V> prev, Node<V> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    public InMemoryHistoryManager() {
        hashMap = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        var list = new ArrayList<Task>();
        var node = tail;
        while (node != null) {
            list.add(node.value);
            node = node.prev;
        }
        return list;
    }


    @Override
    public void add(Task value) {
        if (hashMap.containsKey(value.getId())) {
            var oldValue = hashMap.get(value.getId());
            oldValue.value = value;
        }
        var newTail = new Node<>(value, tail, null);
        if (head == null) {
            head = newTail;
            newTail.prev = null;
        } else if (tail == null) {
            tail = newTail;
            newTail.prev = head;
            head.next = tail;
        } else {
            tail.next = newTail;
            tail = newTail;
        }
        hashMap.put(value.getId() , newTail);
    }

    @Override
    public void remove(int key) {
        var i = hashMap.get(key);

        if (i == null) {
            return;
        }

        if (i == tail) {
            tail = i.prev;
            tail.next = null;
        } else if (i == head) {
            head = i.next;
            if (head != null) {
                head.prev = null;
            }
        } else {
            i.prev.next = i.next;
            i.next.prev = i.prev;
        }
        hashMap.remove(key);
    }

}
