/**
 * SimpleArrayList.java
 * 
 * A custom implementation of a dynamic array list data structure for the
 * Nkwa Real Estate Expenditure Management System. This class provides
 * functionality similar to Java's ArrayList but is implemented from scratch
 * using only arrays and basic Java features.
 * 
 * This implementation features:
 * - Dynamic resizing with automatic capacity expansion
 * - Generic type support for type safety
 * - Iterator support for enhanced for-loop compatibility
 * - Standard list operations (add, remove, get, set)
 * - Index-based access and manipulation
 * - Memory-efficient growth strategy (doubling capacity)
 * 
 * The class is used throughout the Xpense system as the primary collection
 * type, replacing the need for external collection libraries while maintaining
 * good performance characteristics.
 * 
 * @param <T> The type of elements stored in this list
 * @author Group 68, University of Ghana
 * @version 1.0
 * @since 2025
 */
package app.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * SimpleArrayList is a dynamic array implementation providing resizable array functionality.
 * 
 * This class provides:
 * - Dynamic capacity management with automatic resizing
 * - Type-safe generic implementation
 * - Standard list operations (add, remove, get, set, indexOf)
 * - Iterator support for enhanced for-loop usage
 * - Efficient memory usage with capacity doubling strategy
 * 
 * The implementation uses an Object array internally and provides type safety
 * through generic type casting. Capacity is automatically doubled when the
 * array becomes full, ensuring amortized O(1) insertion performance.
 * 
 * @param <T> The type of elements held in this collection
 */
public class SimpleArrayList<T> implements Iterable<T> {
    /** Default initial capacity for new arrays */
    private static final int DEFAULT_CAPACITY = 10;
    
    /** Internal array to store elements */
    private Object[] elements;
    
    /** Current number of elements in the list */
    private int size;

    /**
     * Constructs an empty list with default initial capacity.
     * 
     * The list is initialized with a default capacity of 10 elements
     * and a size of 0. The capacity will automatically expand as needed.
     */
    public SimpleArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Adds an element to the end of the list.
     * 
     * If the internal array is full, it is automatically resized to
     * accommodate the new element. The capacity is doubled each time
     * resizing is needed.
     * 
     * @param element The element to add to the list
     * @return true (as specified by the Collection interface)
     */
    public boolean add(T element) {
        ensureCapacity();  // Expand array if necessary
        elements[size++] = element;
        return true;
    }

    /**
     * Returns the element at the specified position in the list.
     * 
     * @param index The index of the element to return
     * @return The element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return (T) elements[index];
    }

    /**
     * Removes the first occurrence of the specified element from the list.
     * 
     * @param element The element to remove
     * @return true if the element was found and removed, false otherwise
     */
    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx >= 0) {
            remove(idx);
            return true;
        }
        return false;
    }

    /**
     * Removes the element at the specified position in the list.
     * 
     * Shifts all elements after the removed element one position to the left
     * and decrements the size. The removed element is returned.
     * 
     * @param index The index of the element to remove
     * @return The element that was removed
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        T old = (T) elements[index];
        
        // Shift elements left to fill the gap
        for (int i = index; i < size - 1; i++) elements[i] = elements[i + 1];
        elements[--size] = null;  // Clear reference and decrement size
        return old;
    }

    /**
     * Returns the index of the first occurrence of the specified element.
     * 
     * @param element The element to search for
     * @return The index of the first occurrence, or -1 if not found
     */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) return i;
        }
        return -1;
    }

    /**
     * Returns the number of elements in the list.
     * 
     * @return The number of elements in this list
     */
    public int size() { return size; }

    /**
     * Replaces the element at the specified position with the specified element.
     * 
     * @param index The index of the element to replace
     * @param element The element to be stored at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void set(int index, T element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        elements[index] = element;
    }

    /**
     * Ensures that the internal array has sufficient capacity for new elements.
     * 
     * If the array is full, creates a new array with double the capacity
     * and copies all existing elements to the new array. This strategy
     * provides amortized O(1) insertion performance.
     */
    private void ensureCapacity() {
        if (size >= elements.length) {
            Object[] bigger = new Object[elements.length * 2];
            System.arraycopy(elements, 0, bigger, 0, elements.length);
            elements = bigger;
        }
    }

    /**
     * Returns an iterator over the elements in this list.
     * 
     * The iterator supports standard iteration operations and enables
     * the use of enhanced for-loops with this list.
     * 
     * @return An iterator over the elements in this list
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            /** Current position in the iteration */
            int cur = 0;
            
            /**
             * Returns true if there are more elements to iterate over.
             * 
             * @return true if there are more elements, false otherwise
             */
            public boolean hasNext() { return cur < size; }
            
            /**
             * Returns the next element in the iteration.
             * 
             * @return The next element
             * @throws NoSuchElementException if there are no more elements
             */
            @SuppressWarnings("unchecked")
            public T next() {
                if (cur >= size) throw new NoSuchElementException();
                return (T) elements[cur++];
            }
        };
    }
}