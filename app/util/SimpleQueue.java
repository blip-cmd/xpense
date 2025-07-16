package app.util;

public class SimpleQueue<T> {
    private final SimpleArrayList<T> arr;
    public SimpleQueue() { arr = new SimpleArrayList<>(); }
    public void offer(T value) { arr.add(value); }
    public T poll() {
        if (arr.size() == 0) return null;
        return arr.remove(0);
    }
    public boolean isEmpty() { return arr.size() == 0; }
    public int size() { return arr.size(); }
    public void clear() { while (!isEmpty()) poll(); }
}