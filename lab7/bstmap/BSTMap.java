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


    }

    private BSTNode<K, V> root;
    private int size = 0;

    private BSTNode<K, V> binarySearch(K k) {
        return binarySearch(root, k);
    }

    private BSTNode<K, V> binarySearch(BSTNode<K, V> node, K k) {
        if (node == null) {
            return null;
        }
        if (k == null) {
            return null;
        }
        if (k.equals(node.key)) {
            return node;
        } else if (k.compareTo(node.key) < 0) {
            if (node.left != null) {
                return binarySearch(node.left, k);
            } else {
                return node;
            }
        } else if (k.compareTo(node.key) > 0) {
            if (node.right != null) {
                return binarySearch(node.right, k);
            } else {
                return node;
            }
        }
        return null;
    }

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
        return binarySearch(key).key.equals(key);
    }

    @Override
    public V get(K key) {
        if (root == null) {
            return null;
        }
        BSTNode<K, V> lookup = binarySearch(key);
        if (!lookup.key.equals(key)) {
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
            BSTNode<K, V> lookup = binarySearch(key);
            if (lookup.key != key) {
                if (lookup.key.compareTo(key) > 0) {
                    lookup.left = new BSTNode<>(key, value, null, null);
                } else if (lookup.key.compareTo(key) < 0) {
                    lookup.right = new BSTNode<>(key, value, null, null);
                }
                size += 1;
            } else {
                lookup.value = value;
            }
        } else {
            root = new BSTNode<>(key, value, null, null);
            size = size + 1;
        }
    }

    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode<K, V> node) {
        if (node == null) {
            return;
        }
        printInOrder(node.left);
        System.out.println(node.key.toString() + " -> " + node.value.toString());
        printInOrder(node.right);
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

    public static void main(String[] args) {
        BSTMap<String, Integer> map = new BSTMap<String, Integer>();
        for (int i = 0; i < 10; i++) {
            map.put("hi" + i, 1 + i);
            System.out.println(map.get("hi" + i));
        }
        map.printInOrder();
        System.out.println(map.size());

    }
}
