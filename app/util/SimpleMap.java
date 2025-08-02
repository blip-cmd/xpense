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
            // Update existing key
            values.set(idx, value);
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
        if (key == null) return -1;
        for (int i = 0; i < keys.size(); i++) {
            K k = keys.get(i);
            if (k != null && k.equals(key)) return i;
        }
        return -1;
    }

    public int size() {
        return keys.size();
    }

    public V getAt(int index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }

    public K getKeyAt(int index) {
        if (index < 0 || index >= keys.size()) {
            return null;
        }
        return keys.get(index);
    }
}