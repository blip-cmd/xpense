package app.util;

public class MinHeap<T> {
    private T[] heap;
    private int size;
    private static final int INITIAL_CAPACITY = 16;
    private final PriorityComparator<T> comparator;

    public interface PriorityComparator<T> {
        int compare(T a, T b);
    }

    @SuppressWarnings("unchecked")
    public MinHeap(PriorityComparator<T> comparator) {
        this.comparator = comparator;
        this.heap = (T[]) new Object[INITIAL_CAPACITY + 1];
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    private void ensureCapacity() {
        if (size >= heap.length - 1) {
            T[] bigger = (T[]) new Object[heap.length * 2];
            System.arraycopy(heap, 0, bigger, 0, heap.length);
            heap = bigger;
        }
    }

    private void swap(int i, int j) {
        T temp = heap[i]; heap[i] = heap[j]; heap[j] = temp;
    }

    private void siftUp(int idx) {
        while (idx > 1) {
            int parent = idx / 2;
            if (comparator.compare(heap[idx], heap[parent]) < 0) {
                swap(idx, parent); idx = parent;
            } else break;
        }
    }

    private void siftDown(int idx) {
        while (2 * idx <= size) {
            int left = 2 * idx, right = left + 1, smallest = left;
            if (right <= size &&
                comparator.compare(heap[right], heap[left]) < 0) {
                smallest = right;
            }
            if (comparator.compare(heap[smallest], heap[idx]) < 0) {
                swap(idx, smallest); idx = smallest;
            } else break;
        }
    }

    public void insert(T value) {
        ensureCapacity();
        heap[++size] = value;
        siftUp(size);
    }

    public T removeMin() {
        if (size == 0) return null;
        T min = heap[1];
        heap[1] = heap[size--];
        siftDown(1);
        return min;
    }

    public boolean isEmpty() { return size == 0; }
}