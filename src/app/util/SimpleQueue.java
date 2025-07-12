package app.util;

@SuppressWarnings("unchecked")
public class SimpleQueue<T> {
    private static final int CAPACITY = 512;
    private Object[] elements = new Object[CAPACITY];
    private int head = 0, tail = 0, size = 0;

    public void offer(T value) {
        if (size == CAPACITY)
            throw new RuntimeException("Queue full");
        elements[tail] = value;
        tail = (tail + 1) % CAPACITY;
        size++;
    }

    public T poll() {
        if (size == 0)
            return null;
        T val = (T) elements[head];
        elements[head] = null;
        head = (head + 1) % CAPACITY;
        size--;
        return val;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        while (!isEmpty())
            poll();
    }
}