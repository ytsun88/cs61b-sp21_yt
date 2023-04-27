package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = items.length - 1;
        nextLast = 0;
    }

    private void incrSize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, nextLast, a, 0, size - nextLast);
        System.arraycopy(items, 0, a, size - nextLast, nextLast);
        items = a;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    private void decrSize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        if (nextFirst < nextLast) {
            System.arraycopy(items, nextFirst + 1, a, 0, size);
            items = a;
            nextFirst = items.length - 1;
            nextLast = size;
        } else {
            System.arraycopy(items, nextFirst + 1, a, 0, items.length - nextFirst - 1);
            System.arraycopy(items, 0, a, items.length - nextFirst - 1, nextLast);
            items = a;
            nextFirst = items.length - 1;
            nextLast = size;
        }
    }

    @Override
    public void addFirst(T t) {
        if (size == items.length) {
            incrSize(size * 2);
        }
        items[nextFirst] = t;
        nextFirst -= 1;
        if (nextFirst < 0) {
            nextFirst += items.length;
        }
        size += 1;
    }

    @Override
    public void addLast(T t) {
        if (size == items.length) {
            incrSize(size * 2);
        }
        items[nextLast] = t;
        nextLast += 1;
        if (nextLast > items.length - 1) {
            nextLast = 0;
        }
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T t;
            if (nextFirst + 1 > items.length - 1) {
                nextFirst = 0;
                t = items[0];
                items[0] = null;
            } else {
                t = items[nextFirst + 1];
                items[nextFirst + 1] = null;
                nextFirst += 1;
            }
            size -= 1;
            if (items.length > 8 && size < items.length / 4) {
                decrSize(items.length / 2);
            }
            return t;
        }
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            T t;
            if (nextLast - 1 < 0) {
                nextLast = items.length - 1;
                t = items[items.length - 1];
                items[items.length - 1] = null;
            } else {
                t = items[nextLast - 1];
                items[nextLast - 1] = null;
                nextLast -= 1;
            }
            size -= 1;
            if (size < items.length / 4) {
                decrSize(items.length / 2);
            }
            return t;
        }
    }

    @Override
    public T get(int i) {
        if (i > size) {
            return null;
        }
        if (nextFirst + 1 + i > items.length - 1) {
            return items[nextFirst + 1 + i - items.length];
        }
        return items[nextFirst + 1 + i];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> ad = (Deque<T>) o;
        if (ad.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(ad.get(i).equals(get(i)))) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int index;

        ArrayDequeIterator() {
            index = 0;
        }

        public boolean hasNext() {
            return index < size;
        }

        public T next() {
            T item = get(index);
            index += 1;
            return item;
        }
    }

    /*
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
     */

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }
}
