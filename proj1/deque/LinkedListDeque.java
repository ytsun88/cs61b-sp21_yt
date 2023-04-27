package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private static class Node<T> {
        private Node<T> prev;
        private Node<T> next;
        private T object;

        private Node(Node<T> prev, Node<T> next, T object) {
            this.prev = prev;
            this.next = next;
            this.object = object;
        }
    }

    private Node<T> sentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node<>(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    @Override
    public void addFirst(T t) {
        Node<T> firstNode = sentinel.next;
        sentinel.next = new Node<>(sentinel, sentinel.next, t);
        firstNode.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(T t) {
        Node<T> lastNode = sentinel.prev;
        sentinel.prev = new Node<>(sentinel.prev, sentinel, t);
        lastNode.next = sentinel.prev;
        size += 1;
    }

    /*
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
     */

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.object + " ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        T removedObject = sentinel.next.object;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return removedObject;
    }

    @Override
    public T removeLast() {
        if (sentinel.prev == sentinel) {
            size = 0;
            return null;
        }
        T removedObject = sentinel.prev.object;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return removedObject;
    }

    @Override
    public T get(int index) {
        Node<T> p = sentinel.next;
        if (index > size) {
            return null;
        }
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.object;
    }

    public T getRecursive(int index) {
        Node<T> p = sentinel.next;
        if (index > size - 1 || index < 0) {
            return null;
        }
        return getRecursiveHelper(index, p);
    }

    private T getRecursiveHelper(int index, Node<T> currentNode) {
        if (index == 0) {
            return currentNode.object;
        }
        return getRecursiveHelper(index - 1, currentNode.next);
    }

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
        Deque<T> lld = (Deque<T>) o;
        if (lld.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!(lld.get(i).equals(get(i)))) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node<T> p;

        public boolean hasNext() {
            return p.next != sentinel;
        }

        public T next() {
            T nextItem = p.object;
            p = p.next;
            return nextItem;
        }
    }
}
