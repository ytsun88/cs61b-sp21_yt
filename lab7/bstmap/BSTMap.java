package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private static class BSTNode<K extends Comparable<K>, V> {
        public K key;
        public V value;
        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        public BSTNode(K key, V value, BSTNode<K, V> left, BSTNode<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public BSTNode<K, V> get(K k) {
            if (k == null) {
                return null;
            }
            if (k.equals(key)) {
                return this;
            } else if (k.compareTo(key) < 0) {
                return left.get(k);
            } else if (k.compareTo(key) > 0) {
                return right.get(k);
            }
            return null;
        }
    }

    private BSTNode<K, V> root;
    private int size = 0;

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return root.get(key) != null;
    }

    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        BSTNode<K, V> lookup = root.get(key);
        if (lookup == null) {
            return null;
        }
        return lookup.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (root != null) {
            BSTNode<K, V> lookup = root.get(key);
            if (lookup == null) {
                root = new BSTNode<>(key, value, root.left, root.right);
            } else {
                lookup.value = value;
            }
        } else {
            root = new BSTNode<>(key, value, null, null);
            size = size + 1;
        }
    }

    public void printInOrder() {

    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
}
