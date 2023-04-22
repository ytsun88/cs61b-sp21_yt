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

    public void addFirst(T t) {
        Node<T> firstNode = sentinel.next;
        sentinel.next = new Node<>(sentinel, sentinel.next, t);
        firstNode.prev = sentinel.next;
        size += 1;
    }

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

    public int size() {
        return size;
    }


    public void printDeque() {
        Node<T> p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.object + " ");
            p = p.next;
        }
        System.out.println();
    }

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

    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<?> lld = (LinkedListDeque<?>) o;
        if (lld.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (lld.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node<T> p;

        LinkedListDequeIterator() {
            p = sentinel.next;
        }

        public boolean hasNext() {
            return p == sentinel;
        }

        public T next() {
            T item = p.object;
            p = p.next;
            return item;
        }
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(10);
        lld.addLast(20);
        lld.addLast(30);
        lld.printDeque();
        System.out.println(lld.getRecursive(2));
    }
}
