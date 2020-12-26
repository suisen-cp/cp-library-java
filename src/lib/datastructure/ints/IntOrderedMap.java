package lib.datastructure.ints;

import java.util.Iterator;

import lib.util.itertools.IterUtil;
import lib.util.pair.IntEntry;

public class IntOrderedMap<V> implements Iterable<IntEntry<V>> {
    IntRandomizedBinarySearchTree<V> root;
    IntEntry<V> kthEntry(IntRandomizedBinarySearchTree<V> t, int k) {
        if (t == null) return null;
        int c = IntRandomizedBinarySearchTree.size(t.l);
        if (k < c) return kthEntry(t.l, k);
        if (k == c) return t;
        return kthEntry(t.r, k - c - 1);
    }
    public IntEntry<V> kthEntry(int k) {return kthEntry(root, k);}
    public IntEntry<V> lowerEntry(int key) {return kthEntry(ltCount(key) - 1);}
    public IntEntry<V> higherEntry(int key) {return kthEntry(leqCount(key));}
    public IntEntry<V> firstEntry() {return kthEntry(0);}
    public IntEntry<V> lastEntry() {return kthEntry(size() - 1);}
    public V get(int key) {return containsKey(key) ? kthEntry(ltCount(key)).val : null;}
    IntEntry<V> getEntry(int key) {return containsKey(key) ? kthEntry(ltCount(key)) : null;}
    IntRandomizedBinarySearchTree<V> insertEntry(IntRandomizedBinarySearchTree<V> t, int key, V val) {
        return IntRandomizedBinarySearchTree.insert(t, leqCount(t, key), key, val);
    }
    public boolean put(int key, V val) {
        if (containsKey(key)) {
            getEntry(key).val = val;
            return false;
        }
        root = insertEntry(root, key, val);
        return true;
    }
    IntRandomizedBinarySearchTree<V> eraseEntry(IntRandomizedBinarySearchTree<V> t, int key) {
        return IntRandomizedBinarySearchTree.erase(t, leqCount(t, key) - 1);
    }
    public boolean remove(int key) {
        if (containsKey(key)) {
            root = eraseEntry(root, key);
            return true;
        }
        return false;
    }
    boolean containsKey(IntRandomizedBinarySearchTree<V> t, int key) {
        while (t != null) {
            if (t.key == key) return true;
            t = t.key > key ? t.l : t.r;
        }
        return false;
    }
    public boolean containsKey(int key) {return containsKey(root, key);}
    int leqCount(IntRandomizedBinarySearchTree<V> t, int key) {
        if (t == null) return 0;
        if (key < t.key) return leqCount(t.l, key);
        return leqCount(t.r, key) + IntRandomizedBinarySearchTree.size(t.l) + 1;
    }
    public int leqCount(int key) {return leqCount(root, key);}
    int ltCount(IntRandomizedBinarySearchTree<V> t, int key) {
        if (t == null) return 0;
        if (key <= t.key) return ltCount(t.l, key);
        return ltCount(t.r, key) + IntRandomizedBinarySearchTree.size(t.l) + 1;
    }
    public int ltCount(int key) {return ltCount(root, key);}
    public int size() {return IntRandomizedBinarySearchTree.size(root);}
    public boolean isEmpty() {return size() == 0;}
    public void clear() {this.root = null;}
    public Iterator<IntEntry<V>> iterator() {return isEmpty() ? IterUtil.emptyIterator() : root.iterator();}
}