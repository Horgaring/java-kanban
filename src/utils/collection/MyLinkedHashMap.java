package utils.collection;


import java.util.*;

public class MyLinkedHashMap<K, V> implements Map<K, V> {

    private Node<V>  head;
    private Node<V> tail;
    private final HashMap<K, Node<V>> hashMap;

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

    public MyLinkedHashMap() {
        hashMap = new HashMap<>();
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return hashMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        var next = head;
        while (next != null) {
            if (next.value != null
                && next.value.equals(value)) {
                return true;
            } else {
                if (next.value == null
                    && value == null) {
                    return true;
                }
                next = next.next;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if (!hashMap.containsKey(key)) {
            return null;
        }
        var i = hashMap.get(key);
        return i.value;
    }

    @Override
    public V put(K key, V value) {
        if (hashMap.containsKey(key)) {
            var oldValue = hashMap.get(key);
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
        var res = hashMap.put(key, newTail);
        if (res == null) {
            return null;
        } else {
            return res.value;
        }
    }

    @Override
    public V remove(Object key) {
        var i = hashMap.get(key);
        if (i == null) {
            return null;
        }
        if (i == tail) {
            tail = i.prev;
            tail.next = null;
        } else if (i == head) {
            head = i.next;
            if (head != null) {
                head.prev = null;
            }
        } else  {
            i.prev.next = i.next;
            i.next.prev = i.prev;
        }
        hashMap.remove(key);
        return i.value;

    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        tail = null;
        head = null;
        hashMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return hashMap.keySet();
    }

    @Override
    public Collection<V> values() {
        var list = new ArrayList<V>();
        var node = head;
        while (node != null) {
            list.add(node.value);
            node = node.next;
        }
        return list;
    }

    public Collection<V> reverseValues() {
        var list = new ArrayList<V>();
        var node = tail;
        while (node != null) {
            list.add(node.value);
            node = node.prev;
        }
        return list;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        var entrySet = new HashSet<Entry<K, V>>();
        for (var entry : hashMap.entrySet()) {
            var newEntry = new AbstractMap.SimpleEntry<K, V>(entry.getKey(), entry.getValue().value);
            entrySet.add(newEntry);
        }
        return entrySet;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MyLinkedHashMap<K, V> that = (MyLinkedHashMap<K, V>) o;
        return Objects.equals(head, that.head) && Objects.equals(tail, that.tail) && Objects.equals(hashMap, that.hashMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, hashMap);
    }
}


