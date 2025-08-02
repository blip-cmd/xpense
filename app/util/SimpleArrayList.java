package app.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private T[] elements;
    private int size;

    @SuppressWarnings("unchecked")
    public SimpleArrayList() {
        this.elements = (T[]) new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public boolean add(T element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return elements[index];
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
        T old = elements[index];
        for (int i = index; i < size - 1; i++) elements[i] = elements[i + 1];
        elements[--size] = null;
        return old;
    }

    public int indexOf(T element) {
        if (element == null) return -1;
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(element)) return i;
        }
        return -1;
    }

    public int size() { return size; }

    public void set(int index, T element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        elements[index] = element;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= elements.length) {
            T[] bigger = (T[]) new Object[elements.length * 2];
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
                return elements[cur++];
            }
        };
    }
}