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
            // Update existing key - need to replace value at the same index
            // Since SimpleArrayList doesn't have set(index, element), we'll remove and recreate
            SimpleArrayList<V> tempValues = new SimpleArrayList<>();
            for (int i = 0; i < values.size(); i++) {
                if (i == idx) {
                    tempValues.add(value);
                } else {
                    tempValues.add(values.get(i));
                }
            }
            // Clear the original values and copy back
            while (values.size() > 0) {
                values.remove(0);
            }
            for (int i = 0; i < tempValues.size(); i++) {
                values.add(tempValues.get(i));
            }
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