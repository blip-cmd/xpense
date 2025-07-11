package app.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Simple generic Set implementation using ArrayList.
 */
public class SimpleSet<T> implements Iterable<T> {
    private ArrayList<T> items;

    public SimpleSet() {
        items = new ArrayList<>();
    }

    public boolean add(T item) {
        if (!contains(item)) {
            items.add(item);
            return true;
        }
        return false;
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public int size() {
        return items.size();
    }

    public ArrayList<T> toList() {
        return new ArrayList<>(items);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }
}