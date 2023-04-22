package deque;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        public Node prev;
        public Node next;
        public T object;

        public Node(Node prev, Node next, T object) {
            this.prev = prev;
            this.next = next;
            this.object = object;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T t) {
        Node firstNode = sentinel.next;
        sentinel.next = new Node(sentinel, sentinel.next, t);
        firstNode.prev = sentinel.next;
        size += 1;
    }

    public void addLast(T t) {
        Node lastNode = sentinel.prev;
        sentinel.prev = new Node(sentinel.prev, sentinel, t);
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
        Node p = sentinel.next;
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
        size -= 1;
        return removedObject;
    }

    public T get(int index) {
        Node p = sentinel.next;
        if (index > size) {
            return null;
        }
        while (index >= 0) {
            p = p.next;
            index -= 1;
        }
        return p.object;
    }

    public T getRecursive(int index) {
        Node p = sentinel.next;
        if (index > size) {
            return null;
        }
        if (index == 0) {
            return p.object;
        }
        p = p.next;
        return getRecursive(index - 1);
    }
}
