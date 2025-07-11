package app.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple generic Map implementation using two ArrayLists.
 */
public class SimpleMap<K, V> {
    private ArrayList<K> keys;
    private ArrayList<V> values;

    public SimpleMap() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void put(K key, V value) {
        int idx = keys.indexOf(key);
        if (idx >= 0) {
            values.set(idx, value);
        } else {
            keys.add(key);
            values.add(value);
        }
    }

    public V get(K key) {
        int idx = keys.indexOf(key);
        if (idx >= 0) {
            return values.get(idx);
        }
        return null;
    }

    public boolean containsKey(K key) {
        return keys.contains(key);
    }

    public void remove(K key) {
        int idx = keys.indexOf(key);
        if (idx >= 0) {
            keys.remove(idx);
            values.remove(idx);
        }
    }

    public List<K> keySet() {
        return new ArrayList<>(keys);
    }
}