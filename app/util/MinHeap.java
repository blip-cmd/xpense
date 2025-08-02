/**
 * MinHeap.java
 * 
 * A custom implementation of a minimum heap (priority queue) data structure for the
 * Nkwa Real Estate Expenditure Management System. This heap implementation is used
 * primarily by the AlertSystem to manage alerts by priority, ensuring that the most
 * critical alerts are processed first.
 * 
 * This implementation features:
 * - Generic type support with custom comparator
 * - Dynamic array resizing for unlimited capacity
 * - Efficient O(log n) insertion and removal operations
 * - 1-based indexing for simplified parent/child calculations
 * - Custom PriorityComparator interface for flexible ordering
 * 
 * The heap maintains the min-heap property where the parent node is always
 * smaller than or equal to its children according to the provided comparator.
 * 
 * @param <T> The type of elements stored in this heap
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app.util;

/**
 * MinHeap is a priority queue implementation using a binary heap structure.
 * 
 * This class provides:
 * - Efficient priority-based element management
 * - O(log n) insertion and removal operations
 * - Dynamic capacity management
 * - Customizable priority ordering through comparator
 * 
 * The heap uses 1-based indexing internally for simpler parent/child
 * index calculations:
 * - Parent of node i: i / 2
 * - Left child of node i: 2 * i
 * - Right child of node i: 2 * i + 1
 * 
 * The heap maintains the min-heap invariant where each parent node
 * has a priority less than or equal to its children.
 * 
 * @param <T> The type of elements held in this heap
 */
public class MinHeap<T> {
    /** Internal array to store heap elements (1-based indexing) */
    private Object[] heap;
    
    /** Current number of elements in the heap */
    private int size;
    
    /** Initial capacity for the heap array */
    private static final int INITIAL_CAPACITY = 16;
    
    /** Comparator for determining element priority */
    private final PriorityComparator<T> comparator;

    /**
     * Functional interface for comparing element priorities.
     * 
     * Implementations should return:
     * - Negative value if a has higher priority than b
     * - Zero if a and b have equal priority  
     * - Positive value if a has lower priority than b
     * 
     * @param <T> The type of elements being compared
     */
    public interface PriorityComparator<T> {
        /**
         * Compares two elements for priority ordering.
         * 
         * @param a The first element to compare
         * @param b The second element to compare
         * @return A negative, zero, or positive value indicating priority order
         */
        int compare(T a, T b);
    }

    /**
     * Constructs a new MinHeap with the specified comparator.
     * 
     * The heap is initialized with a default capacity and uses 1-based
     * indexing internally. Index 0 is unused to simplify parent/child
     * index calculations.
     * 
     * @param comparator The comparator to determine element priority
     */
    public MinHeap(PriorityComparator<T> comparator) {
        this.comparator = comparator;
        this.heap = new Object[INITIAL_CAPACITY + 1];  // +1 for 1-based indexing
        this.size = 0;
    }

    /**
     * Ensures the heap array has sufficient capacity for new elements.
     * 
     * If the array is nearly full, creates a new array with double the
     * capacity and copies all existing elements.
     */
    private void ensureCapacity() {
        if (size >= heap.length - 1) {
            Object[] bigger = new Object[heap.length * 2];
            System.arraycopy(heap, 0, bigger, 0, heap.length);
            heap = bigger;
        }
    }

    /**
     * Swaps two elements in the heap array.
     * 
     * @param i Index of the first element
     * @param j Index of the second element
     */
    private void swap(int i, int j) {
        Object temp = heap[i]; 
        heap[i] = heap[j]; 
        heap[j] = temp;
    }

    /**
     * Restores the heap property by moving an element up the tree.
     * 
     * This method is called after insertion to ensure the newly added
     * element is positioned correctly according to its priority.
     * The element is repeatedly compared with its parent and swapped
     * if it has higher priority.
     * 
     * @param idx The index of the element to sift up
     */
    @SuppressWarnings("unchecked")
    private void siftUp(int idx) {
        while (idx > 1) {
            int parent = idx / 2;
            // If current element has higher priority than parent, swap
            if (comparator.compare((T)heap[idx], (T)heap[parent]) < 0) {
                swap(idx, parent); 
                idx = parent;
            } else break;
        }
    }

    /**
     * Restores the heap property by moving an element down the tree.
     * 
     * This method is called after removal to ensure the heap property
     * is maintained. The element is repeatedly compared with its children
     * and swapped with the child having the highest priority.
     * 
     * @param idx The index of the element to sift down
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int idx) {
        while (2 * idx <= size) {
            int left = 2 * idx;           // Left child index
            int right = left + 1;         // Right child index
            int smallest = left;          // Assume left child has highest priority
            
            // Check if right child exists and has higher priority than left child
            if (right <= size &&
                comparator.compare((T)heap[right], (T)heap[left]) < 0) {
                smallest = right;
            }
            
            // If the child with highest priority has higher priority than parent, swap
            if (comparator.compare((T)heap[smallest], (T)heap[idx]) < 0) {
                swap(idx, smallest); 
                idx = smallest;
            } else break;
        }
    }

    /**
     * Inserts a new element into the heap.
     * 
     * The element is added at the end of the heap and then sifted up
     * to its correct position to maintain the heap property.
     * Time complexity: O(log n)
     * 
     * @param value The element to insert
     */
    public void insert(T value) {
        ensureCapacity();           // Ensure space is available
        heap[++size] = value;       // Add element at the end
        siftUp(size);              // Restore heap property
    }

    /**
     * Removes and returns the element with the highest priority (minimum).
     * 
     * The root element (minimum) is removed and replaced with the last
     * element in the heap, which is then sifted down to its correct position.
     * Time complexity: O(log n)
     * 
     * @return The element with the highest priority, or null if the heap is empty
     */
    @SuppressWarnings("unchecked")
    public T removeMin() {
        if (size == 0) return null;
        
        T min = (T)heap[1];         // Store the minimum element
        heap[1] = heap[size--];     // Move last element to root and decrease size
        siftDown(1);               // Restore heap property
        return min;
    }

    /**
     * Checks if the heap is empty.
     * 
     * @return true if the heap contains no elements, false otherwise
     */
    public boolean isEmpty() { 
        return size == 0; 
    }
}