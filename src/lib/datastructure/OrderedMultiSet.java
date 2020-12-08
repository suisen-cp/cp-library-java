package lib.datastructure;

import java.util.Comparator;

public class OrderedMultiSet<T> extends RandomizedBinarySearchTree<T, Object> {
    Node root;
    final Comparator<? super T> comparator;
    public OrderedMultiSet(Comparator<? super T> comparator) {this.comparator = comparator;}
    public OrderedMultiSet() {this(null);}
    T kthElement(Node t, int k) {
        int c = size(t.l);
        if (k < c) return kthElement(t.l, k);
        if (k == c) return t.key;
        return kthElement(t.r, k - c - 1);
    }
    public T kthElement(int k) {
        return kthElement(root, k);
    }
    Node insertKey(Node t, T key) {
        return insert(t, leqCount(t, key), key, null);
    }
    public void insertKey(T key) {
        root = insert(root, leqCount(key), key, null);
    }
    Node eraseKey(Node t, T key) {
        if (count(t, key) == 0) return t;
        return super.erase(t, leqCount(t, key) - 1);
    }
    public void eraseKey(T key) {
        if (count(root, key) == 0) return;
        root = super.erase(root, leqCount(key) - 1);
    }
    int count(Node t, T key) {
        return leqCount(t, key) - ltCount(t, key);
    }
    public int count(T key) {
        return leqCount(key) - ltCount(key);
    }
    int leqCount(Node t, T key) {
        if (t == null) return 0;
        if (comparator.compare(key, t.key) < 0) return leqCount(t.l, key);
        return leqCount(t.r, key) + size(t.l) + 1;
    }
    int leqCountComparable(Node t, Comparable<? super T> key) {
        if (t == null) return 0;
        if (key.compareTo(t.key) < 0) return leqCountComparable(t.l, key);
        return leqCountComparable(t.r, key) + size(t.l) + 1;
    }
    @SuppressWarnings("unchecked")
    public int leqCount(T key) {
        if (comparator != null) return leqCount(root, key);
        Comparable<? super T> cmpkey = (Comparable<? super T>) key;
        return leqCountComparable(root, cmpkey);
    }
    int ltCount(Node t, T key) {
        if (t == null) return 0;
        if (comparator.compare(key, t.key) <= 0) return ltCount(t.l, key);
        return ltCount(t.r, key) + size(t.l) + 1;
    }
    int ltCountComparable(Node t, Comparable<? super T> key) {
        if (t == null) return 0;
        if (key.compareTo(t.key) <= 0) return ltCountComparable(t.l, key);
        return ltCountComparable(t.r, key) + size(t.l) + 1;
    }
    @SuppressWarnings("unchecked")
    public int ltCount(T key) {
        if (comparator != null) return ltCount(root, key);
        Comparable<? super T> cmpkey = (Comparable<? super T>) key;
        return ltCountComparable(root, cmpkey);
    }
    public int geqCount(T key) {
        return size() - ltCount(key);
    }
    public int gtCount(T key) {
        return size() - leqCount(key);
    }
    public int size() {
        return size(root);
    }
    public void clear() {
        root = null;
    }
}