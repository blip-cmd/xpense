package app.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] elements;
    private int size;

    public SimpleArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public boolean add(T element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return (T) elements[index];
    }

    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T old = (T) elements[index];
        for (int i = index; i < size - 1; i++) elements[i] = elements[i + 1];
        elements[--size] = null;
        return old;
    }

    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) return i;
        }
        return -1;
    }

    public int size() { return size; }

    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] bigger = new Object[elements.length * 2];
            System.arraycopy(elements, 0, bigger, 0, elements.length);
            elements = bigger;
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int cur = 0;
            public boolean hasNext() { return cur < size; }
            public T next() {
                if (cur >= size) throw new NoSuchElementException();
                return (T) elements[cur++];
            }
        };
    }
}