package app.util;

public abstract class List<E> {
    public abstract void add(E e);

    public abstract E get(int index);

    public abstract E remove(int index);

    public abstract int size();
}
