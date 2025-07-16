package app.util;

public class SimpleSet<T> implements Iterable<T> {
    private final SimpleArrayList<T> items;

    public SimpleSet() {
        items = new SimpleArrayList<>();
    }

    public boolean add(T item) {
        if (!contains(item)) {
            items.add(item);
            return true;
        }
        return false;
    }

    public boolean contains(T item) {
        for (T t : items) if (t.equals(item)) return true;
        return false;
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public int size() { return items.size(); }

    public SimpleArrayList<T> toList() {
        SimpleArrayList<T> arr = new SimpleArrayList<>();
        for (T t : items) arr.add(t);
        return arr;
    }

    public java.util.Iterator<T> iterator() { return items.iterator(); }
}