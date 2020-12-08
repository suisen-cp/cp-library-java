package lib.datastructure.ints;

import lib.util.pair.IntEntry;

public class IntOrderedMap<V> extends IntRandomizedBinarySearchTree<V> {
    Node root;
    IntEntry<V> kthEntry(Node t, int k) {
        if (t == null) return null;
        int c = size(t.l);
        if (k < c) return kthEntry(t.l, k);
        if (k == c) return t;
        return kthEntry(t.r, k - c - 1);
    }
    public IntEntry<V> kthEntry(int k) {
        return kthEntry(root, k);
    }
    public IntEntry<V> lowerEntry(int key) {
        return kthEntry(ltCount(key) - 1);
    }
    public IntEntry<V> higherEntry(int key) {
        return kthEntry(leqCount(key));
    }
    public IntEntry<V> firstEntry() {
        return kthEntry(0);
    }
    public IntEntry<V> lastEntry() {
        return kthEntry(size() - 1);
    }
    public V get(int key) {
        return containsKey(key) ? kthEntry(ltCount(key)).val : null;
    }
    IntEntry<V> getEntry(int key) {
        return containsKey(key) ? kthEntry(ltCount(key)) : null;
    }
    Node insertEntry(Node t, int key, V val) {
        return insert(t, leqCount(t, key), key, val);
    }
    public void put(int key, V val) {
        if (containsKey(key)) {
            getEntry(key).val = val;
            return;
        }
        root = insertEntry(root, key, val);
    }
    Node eraseEntry(Node t, int key) {
        return super.erase(t, leqCount(t, key) - 1);
    }
    public void remove(int key) {
        if (containsKey(key)) root = eraseEntry(root, key);
    }
    boolean containsKey(Node t, int key) {
        while (t != null) {
            if (t.key == key) return true;
            t = t.key > key ? t.l : t.r;
        }
        return false;
    }
    public boolean containsKey(int key) {
        return containsKey(root, key);
    }
    int leqCount(Node t, int key) {
        if (t == null) return 0;
        if (key < t.key) return leqCount(t.l, key);
        return leqCount(t.r, key) + size(t.l) + 1;
    }
    public int leqCount(int key) {
        return leqCount(root, key);
    }
    int ltCount(Node t, int key) {
        if (t == null) return 0;
        if (key <= t.key) return ltCount(t.l, key);
        return ltCount(t.r, key) + size(t.l) + 1;
    }
    public int ltCount(int key) {
        return ltCount(root, key);
    }
    public int size() {
        return size(root);
    }
    public void clear() {
        this.root = null;
    }
}