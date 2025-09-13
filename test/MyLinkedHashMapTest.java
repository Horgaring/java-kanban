import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;
import utils.collection.MyLinkedHashMap;

import java.util.*;

public class MyLinkedHashMapTest {
    private MyLinkedHashMap<Integer, Task> map;

    @BeforeEach
    void setup() {
        map = new MyLinkedHashMap<>();
    }





    @Test
    void shouldGetElementByKey() {
        Task task = new Task(1, "Test Task", "Description");
        map.put(1, task);
        
        Task retrieved = map.get(1);
        
        Assertions.assertEquals(task, retrieved);
        Assertions.assertEquals("Test Task", retrieved.getName());
    }

    @Test
    void shouldReturnNullForNonExistentKey() {
        Task result = map.get(999);
        Assertions.assertNull(result);
    }

    @Test
    void shouldReplaceValueWhenSameKeyUsed() {
        Task task1 = new Task(1, "First Task", "Description 1");
        Task task2 = new Task(1, "Second Task", "Description 2");
        
        map.put(1, task1);
        Task oldValue = map.put(1, task2);
        
        Assertions.assertEquals(task1, oldValue);
        Assertions.assertEquals(task2, map.get(1));
        Assertions.assertEquals(1, map.size());
    }

    @Test
    void shouldRemoveElementCorrectly() {
        Task task = new Task(1, "Test Task", "Description");
        map.put(1, task);
        
        Task removed = map.remove(1);
        
        Assertions.assertEquals(task, removed);
        Assertions.assertTrue(map.isEmpty());
        Assertions.assertFalse(map.containsKey(1));
        Assertions.assertFalse(map.containsValue(task));
    }

    @Test
    void shouldReturnNullWhenRemovingNonExistentKey() {
        Task result = map.remove(999);
        Assertions.assertNull(result);
    }

    @Test
    void shouldMaintainInsertionOrderInValues() {
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        Task task3 = new Task(3, "Third", "Description 3");
        
        map.put(1, task1);
        map.put(2, task2);
        map.put(3, task3);
        
        Collection<Task> values = map.values();
        List<Task> valueList = new ArrayList<>(values);
        
        Assertions.assertEquals(3, valueList.size());
        Assertions.assertEquals(task1, valueList.get(0));
        Assertions.assertEquals(task2, valueList.get(1));
        Assertions.assertEquals(task3, valueList.get(2));
    }

    @Test
    void shouldReturnCorrectKeySet() {
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        
        map.put(1, task1);
        map.put(2, task2);
        
        Set<Integer> keys = map.keySet();
        
        Assertions.assertEquals(2, keys.size());
        Assertions.assertTrue(keys.contains(1));
        Assertions.assertTrue(keys.contains(2));
    }

    @Test
    void shouldReturnCorrectEntrySet() {
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        
        map.put(1, task1);
        map.put(2, task2);
        
        Set<Map.Entry<Integer, Task>> entries = map.entrySet();
        
        Assertions.assertEquals(2, entries.size());
        Assertions.assertTrue(entries.stream().anyMatch(entry -> entry.getKey().equals(1) && entry.getValue().equals(task1)));
        Assertions.assertTrue(entries.stream().anyMatch(entry -> entry.getKey().equals(2) && entry.getValue().equals(task2)));
    }

    @Test
    void shouldClearAllElements() {
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        
        map.put(1, task1);
        map.put(2, task2);
        
        map.clear();
        
        Assertions.assertTrue(map.isEmpty());
        Assertions.assertEquals(0, map.size());
        Assertions.assertFalse(map.containsKey(1));
        Assertions.assertFalse(map.containsKey(2));
    }

    @Test
    void shouldPutAllElementsFromAnotherMap() {
        Map<Integer, Task> otherMap = new HashMap<>();
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        
        otherMap.put(1, task1);
        otherMap.put(2, task2);
        
        map.putAll(otherMap);
        
        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals(task1, map.get(1));
        Assertions.assertEquals(task2, map.get(2));
    }

    @Test
    void shouldHandleSingleElementCorrectly() {
        Task task = new Task(1, "Single Task", "Description");
        
        map.put(1, task);
        
        Assertions.assertEquals(1, map.size());
        Assertions.assertFalse(map.isEmpty());
        Assertions.assertEquals(task, map.get(1));
        Assertions.assertTrue(map.containsKey(1));
        Assertions.assertTrue(map.containsValue(task));
        
        Task removed = map.remove(1);
        Assertions.assertEquals(task, removed);
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    void shouldMaintainOrderAfterRemoval() {
        Task task1 = new Task(1, "First", "Description 1");
        Task task2 = new Task(2, "Second", "Description 2");
        Task task3 = new Task(3, "Third", "Description 3");
        
        map.put(1, task1);
        map.put(2, task2);
        map.put(3, task3);
        
        // Удаляем средний элемент
        map.remove(2);
        
        Collection<Task> values = map.values();
        List<Task> valueList = new ArrayList<>(values);
        
        Assertions.assertEquals(2, valueList.size());
        Assertions.assertEquals(task1, valueList.get(0));
        Assertions.assertEquals(task3, valueList.get(1));
    }

    @Test
    void shouldHandleNullValues() {
        // Проверяем, что карта может работать с null значениями
        map.put(1, null);
        
        Assertions.assertTrue(map.containsKey(1));
        Assertions.assertNull(map.get(1));
        Assertions.assertEquals(1, map.size());
    }

    @Test
    void shouldTestContainsValueWithNull() {
        map.put(1, null);
        
        Assertions.assertTrue(map.containsValue(null));
        Assertions.assertFalse(map.containsValue(new Task(999, "Non-existent", "Description")));
    }
}
