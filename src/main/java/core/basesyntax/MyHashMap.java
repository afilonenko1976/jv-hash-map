package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {

    static final int MY_DEFAULT_CAPACITY = 16;
    static final float MY_LOAD_FACTOR = 0.75f;
    private int size;

    private Node<K,V>[] table;

    static class Node<K,V> {

        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        Node(int hash,K key,V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    @Override
    public void put(K key, V value) {

        Node<K,V>[] newTab;

        //Node<K,V> node;
        Node<K,V> currentNode;
        Node<K,V> newCurrentNode;

        int hash;
        int countBucket;

        if (table == null) {
            newTab = (Node<K, V>[]) new Node[MY_DEFAULT_CAPACITY];
            table = newTab;
        } else if (size + 1 == (int) table.length * MY_LOAD_FACTOR) {
            newTab = resize();
            table = newTab;
        }

        hash = hashKey(key);
        countBucket = (table.length - 1) & hash;

        if (table[countBucket] == null) {

            Node<K,V> node = new Node<K,V>(hash, key, value, null);
            table[countBucket] = node;
            size++;

        } else {

            currentNode = table[countBucket];

            if (hash == currentNode.hash
                    && (key == currentNode.key || key != null && key.equals(currentNode.key))) {
                currentNode.value = value;    
            } else {

                if (currentNode.next != null) {

                    currentNode = currentNode.next;

                    do {
                        if (hash == currentNode.hash
                                && (key == currentNode.key || key != null
                                && key.equals(currentNode.key))) {
                            currentNode.value = value;
                            currentNode = null;
                            break;
                        }

                        if (currentNode.next == null) {
                            break;
                        } else {
                            currentNode = currentNode.next;
                        }
                    } while (true);

                    if (currentNode != null) {
                        Node<K,V> node = new Node<K,V>(hash, key, value, null);
                        size++;
                        currentNode.next = node;
                        currentNode = null;
                    }

                } else {

                    Node<K,V> node = new Node<K,V>(hash, key, value, null);
                    currentNode.next = node;
                    size++;

                }
            }
        }
    }

    @Override
    public V getValue(K key) {

        if (table == null) {
            return null;
        }

        int hash = hashKey(key);
        int countBucket = (table.length - 1) & hash;

        Node<K,V> currentNode = table[countBucket];

        if (currentNode == null) {
            return null;
        }

        if (hash == currentNode.hash
                && (key == currentNode.key || key != null && key.equals(currentNode.key))) {
            return currentNode.value;

        } else {

            if (currentNode.next != null) {

                currentNode = currentNode.next;

                do {
                    if (hash == currentNode.hash
                            && (key == currentNode.key || key != null
                            && key.equals(currentNode.key))) {
                        return currentNode.value;
                    }

                    if (currentNode.next == null) {
                        break;
                    } else {
                        currentNode = currentNode.next;
                    }
                } while (true);
            }
        }

        return null;

    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getHash() {
        return 0;
    }

    final int hashKey(K key) {
        int h = 0;
        int b = 0;

        if (key != null) {
            h = key.hashCode();
            b = h >>> 16;
            return (int) h ^ b;
        }

        return 0;

    }

    final Node<K,V>[] resize() {

        Node<K,V>[] newTab;

        Node<K,V> currentNode;
        Node<K,V> newCurrentNode;

        int hash;
        int countBucket;

        newTab = (Node<K, V>[]) new Node[table.length * 2];
        for (int i = 0;i < table.length;i++) {

            if (table[i] != null) {

                currentNode = table[i];
                while (currentNode != null) {

                    Node<K,V> oldNextNode = null;

                    if (currentNode.next != null) {
                        oldNextNode = currentNode.next;
                        currentNode.next = null;
                    }

                    hash = hashKey(currentNode.key);
                    countBucket = (newTab.length - 1) & hash;

                    if (newTab[countBucket] != null) {
                        newCurrentNode = newTab[countBucket];
                        do {
                            if (newCurrentNode.next != null) {
                                newCurrentNode = newCurrentNode.next;
                            } else {
                                newCurrentNode.next = currentNode;
                                break;
                            }

                        } while (true);

                    } else {
                        newTab[countBucket] = currentNode;
                        currentNode.next = null;
                    }

                    if (oldNextNode != null) {
                        currentNode = oldNextNode;
                    } else {
                        currentNode = null;
                    }
                }
            }
        }

        return newTab;
    }
}
