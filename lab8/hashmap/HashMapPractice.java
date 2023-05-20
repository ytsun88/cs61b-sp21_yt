package hashmap;

import java.util.*;

public class HashMapPractice<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private int size = 0;
    private static final int DEFAULT_TABLE_LENGTH = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private Collection<Node>[] buckets;
    private final double loadFactor;

    /**
     * Constructors
     */
    public HashMapPractice() {
        this(DEFAULT_TABLE_LENGTH, DEFAULT_LOAD_FACTOR);
    }

    public HashMapPractice(int initialSize) {
        this(initialSize, DEFAULT_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public HashMapPractice(int initialSize, double maxLoad) {
        this.buckets = createTable(initialSize);
        this.loadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    @Override
    public void clear() {
        buckets = createTable(DEFAULT_TABLE_LENGTH);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int id = getKeyID(key);
        for (Node node : buckets[id]) {
            if (node.key == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int id = getKeyID(key);
        for (Node node : buckets[id]) {
            if (node.key == key) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("ICAST_IDIV_CAST_TO_DOUBLE")
    @Override
    public void put(K key, V value) {
        int id = getKeyID(key);
        if (buckets[id] == null) {
            buckets[id].add(createNode(key, value));
            size += 1;
        }
        for (Node node : buckets[id]) {
            if (node.key == key) {
                node.value = value;
            }
        }
        if ((double) (size / buckets.length) > loadFactor) {
            resize(2 * buckets.length);
        }
    }

    private void resize(int capacity) {
        Collection<Node>[] newBuckets = createTable(capacity);
        Iterator<Node> nodeIterator = new MyHashMapNodeIterator();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            int hashcode = node.key.hashCode();
            int bucketIndex = Math.floorMod(hashcode, newBuckets.length);
            newBuckets[bucketIndex].add(node);
        }
        buckets = newBuckets;
    }

    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private final Iterator<Node> nodeIterator = new MyHashMapNodeIterator();

        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        public K next() {
            return nodeIterator.next().key;
        }
    }

    private class MyHashMapNodeIterator implements Iterator<Node> {
        private final Iterator<Collection<Node>> bucketsIterator = Arrays.stream(buckets).iterator();
        private Iterator<Node> currentBucketIterator;
        private int nodesLeft = size;

        public boolean hasNext() {
            return nodesLeft > 0;
        }

        public Node next() {
            if (currentBucketIterator == null || !currentBucketIterator.hasNext()) {
                Collection<Node> currentBucket = bucketsIterator.next();
                while (currentBucket.size() == 0) {
                    currentBucket = bucketsIterator.next();
                }
                currentBucketIterator = currentBucket.iterator();
            }
            nodesLeft -= 1;
            return currentBucketIterator.next();
        }
    }

    private int getKeyID(K key) {
        int id = key.hashCode();
        return Math.floorMod(id, buckets.length);
    }

    public V remove(K key) {
        return null;
    }

    public V remove(K key, V value) {
        return null;
    }
}
