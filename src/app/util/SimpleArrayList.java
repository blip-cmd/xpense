package app.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A custom implementation of ArrayList using a dynamic array.
 * This class provides a resizable array implementation similar to Java's ArrayList.
 * 
 * @param <T> the type of elements held in this list
 */
public class SimpleArrayList<T> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROWTH_FACTOR = 2;
    
    private Object[] elements;
    private int size;
    
    /**
     * Constructs an empty list with the default initial capacity of 10.
     */
    public SimpleArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }
    
    /**
     * Constructs an empty list with the specified initial capacity.
     * 
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    public SimpleArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }
    
    /**
     * Appends the specified element to the end of this list.
     * 
     * @param element the element to be appended to this list
     * @return true (as specified by Collection.add)
     */
    public boolean add(T element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }
    
    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent elements to the right.
     * 
     * @param index the index at which the specified element is to be inserted
     * @param element the element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        ensureCapacity();
        
        // Shift elements to the right
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        
        elements[index] = element;
        size++;
    }
    
    /**
     * Returns the element at the specified position in this list.
     * 
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }
    
    /**
     * Replaces the element at the specified position in this list with the specified element.
     * 
     * @param index the index of the element to replace
     * @param element the element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T oldElement = (T) elements[index];
        elements[index] = element;
        return oldElement;
    }
    
    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     * 
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        T removedElement = (T) elements[index];
        
        // Shift elements to the left
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        
        elements[--size] = null; // Clear the reference and decrement size
        return removedElement;
    }
    
    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     * 
     * @param element the element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }
    
    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     * 
     * @param element the element to search for
     * @return the index of the first occurrence of the specified element in this list,
     *         or -1 if this list does not contain the element
     */
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == null ? element == null : elements[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Returns true if this list contains the specified element.
     * 
     * @param element the element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    public boolean contains(T element) {
        return indexOf(element) >= 0;
    }
    
    /**
     * Returns the number of elements in this list.
     * 
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns true if this list contains no elements.
     * 
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Removes all of the elements from this list. The list will be empty after this call returns.
     */
    public void clear() {
        // Clear all references to help GC
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }
    
    /**
     * Returns an array containing all of the elements in this list in proper sequence.
     * 
     * @return an array containing all of the elements in this list in proper sequence
     */
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }
    
    /**
     * Ensures that the internal array has enough capacity to hold more elements.
     * If the current capacity is not sufficient, the array is resized.
     */
    private void ensureCapacity() {
        if (size >= elements.length) {
            int newCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length * GROWTH_FACTOR;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
    
    /**
     * Returns an iterator over the elements in this list in proper sequence.
     * 
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<T> iterator() {
        return new SimpleArrayListIterator();
    }
    
    /**
     * Inner class that implements Iterator for SimpleArrayList.
     */
    private class SimpleArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;
        private boolean canRemove = false;
        
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true;
            return (T) elements[currentIndex++];
        }
        
        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("remove() can only be called once per call to next()");
            }
            SimpleArrayList.this.remove(--currentIndex);
            canRemove = false;
        }
    }
    
    /**
     * Returns a string representation of this list.
     * 
     * @return a string representation of this list
     */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Compares the specified object with this list for equality.
     * 
     * @param obj the object to be compared for equality with this list
     * @return true if the specified object is equal to this list
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SimpleArrayList<?> other = (SimpleArrayList<?>) obj;
        if (size != other.size) return false;
        
        for (int i = 0; i < size; i++) {
            Object thisElement = elements[i];
            Object otherElement = other.elements[i];
            if (thisElement == null ? otherElement != null : !thisElement.equals(otherElement)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the hash code value for this list.
     * 
     * @return the hash code value for this list
     */
    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            Object element = elements[i];
            result = 31 * result + (element == null ? 0 : element.hashCode());
        }
        return result;
    }
}