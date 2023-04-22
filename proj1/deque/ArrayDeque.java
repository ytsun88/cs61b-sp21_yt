package deque;

public class ArrayDeque<T> implements Deque<T> {
    T[] items;
    int nextFirst;
    int nextLast;
    int size;

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

    public T get(int i) {
        if (i > size) {
            return null;
        }
        if (nextFirst + 1 + i > items.length - 1) {
            return items[nextFirst + 1 + i - items.length];
        }
        return items[nextFirst + 1 + i];
    }

    public int size() {
        return size;
    }

    /*
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
     */

    public void printDeque() {
        int begin = nextFirst + 1;
        int end = nextLast - 1;
        if (isEmpty()) {
            System.out.println();
        } else {
            if (begin > items.length - 1) {
                begin = 0;
            }
            if (end < 0) {
                end = items.length - 1;
            }
            while (begin != end) {
                if (begin > items.length - 1) {
                    begin = 0;
                } else {
                    System.out.print(items[begin] + " ");
                    begin += 1;
                }
            }
            System.out.println(items[end]);
        }
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(10);
        ad.addFirst(20);
        ad.addFirst(30);
        ad.addLast(40);
        ad.addLast(50);
        ad.printDeque();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        ad.printDeque();
    }
}
