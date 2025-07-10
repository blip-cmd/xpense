package app.util;

public class SimpleStack<T> {
    private static final int CAPACITY = 512;
    private Object[] elements = new Object[CAPACITY];
    private int top = 0;

    public void push(T value) {
        elements[top++] = value;
    }

    public T pop() {
        if (top == 0)
            return null;
        T val = (T) elements[--top];
        elements[top] = null;
        return val;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    public T peek() {
        if (top == 0)
            return null;
        return (T) elements[top - 1];
    }

    public void clear() {
        for (int i = 0; i < top; i++)
            elements[i] = null;
        top = 0;
    }

    public Object[] toArray() {
        Object[] arr = new Object[top];
        System.arraycopy(elements, 0, arr, 0, top);
        return arr;
    }

    // For-each style iteration (not strictly required, but handy)
    public Object[] elements() {
        return toArray();
    }
}