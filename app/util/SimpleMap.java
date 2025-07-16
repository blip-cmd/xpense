package app.util;

public class SimpleMap<K, V> {
    private final SimpleArrayList<K> keys;
    private final SimpleArrayList<V> values;

    public SimpleMap() {
        keys = new SimpleArrayList<>();
        values = new SimpleArrayList<>();
    }

    public void put(K key, V value) {
        int idx = indexOf(key);
        if (idx >= 0) {
            values.remove(idx);
            values.add(idx, value);
        } else {
            keys.add(key);
            values.add(value);
        }
    }

    public V get(K key) {
        int idx = indexOf(key);
        if (idx >= 0) return values.get(idx);
        return null;
    }

    public boolean containsKey(K key) {
        return indexOf(key) >= 0;
    }

    public void remove(K key) {
        int idx = indexOf(key);
        if (idx >= 0) {
            keys.remove(idx);
            values.remove(idx);
        }
    }

    public int indexOf(K key) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key)) return i;
        }
        return -1;
    }
}