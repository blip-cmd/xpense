package app.util;

public class SimpleStack<T> {
    private final SimpleArrayList<T> arr;
    public SimpleStack() { arr = new SimpleArrayList<>(); }
    public void push(T value) { arr.add(value); }
    public T pop() {
        if (arr.size() == 0) return null;
        return arr.remove(arr.size() - 1);
    }
    public boolean isEmpty() { return arr.size() == 0; }
    public int size() { return arr.size(); }
    public T peek() {
        if (arr.size() == 0) return null;
        return arr.get(arr.size() - 1);
    }
    public void clear() { while (!isEmpty()) pop(); }
}