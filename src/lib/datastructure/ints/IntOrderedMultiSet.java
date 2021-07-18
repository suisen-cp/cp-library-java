package lib.datastructure.ints;

import java.util.PrimitiveIterator;

import lib.util.itertools.IterUtil;

public class IntOrderedMultiSet implements Iterable<Integer> {
    private static final Object DUMMY = new Object();

    IntRandomizedBinarySearchTree<Object> root;
    public int kthElement(int k) {
        if (k < 0 || k >= size()) throw new IndexOutOfBoundsException();
        return IntRandomizedBinarySearchTree.getKthKey(root, k);
    }
    public int lowerElement(int key) {return kthElement(ltCount(key) - 1);}
    public int floorElement(int key) {return kthElement(leqCount(key) - 1);}
    public int higherElement(int key) {return kthElement(leqCount(key));}
    public int ceilingElement(int key) {return kthElement(ltCount(key));}
    public int first() {return kthElement(0);}
    public int last() {return kthElement(size() - 1);}
    public void add(int key) {root = IntRandomizedBinarySearchTree.insert(root, leqCount(root, key), key, DUMMY);}
    static IntRandomizedBinarySearchTree<Object> eraseEntry(IntRandomizedBinarySearchTree<Object> t, int key) {
        return IntRandomizedBinarySearchTree.erase(t, leqCount(t, key) - 1);
    }
    public boolean remove(int key) {
        if (contains(key)) {
            root = IntRandomizedBinarySearchTree.erase(root, leqCount(root, key) - 1);
            return true;
        }
        return false;
    }
    public int removeRange(int fromElement, int toElement) {
        return removeRangeIndex(ltCount(fromElement), ltCount(toElement));
    }
    public boolean removeKthElement(int k) {
        if (k < 0 || k >= size()) return false;
        root = IntRandomizedBinarySearchTree.erase(root, k);
        return true;
    }
    public int removeRangeIndex(int fromIndex, int toIndex) {
        int l = Math.max(fromIndex, 0);
        int r = Math.min(toIndex, size());
        if (l >= r) return 0;
        root = IntRandomizedBinarySearchTree.eraseRange(root, l, r);
        return r - l;
    }
    public boolean contains(int key) {
        IntRandomizedBinarySearchTree<Object> t = root;
        while (t != null) {
            if (t.key == key) return true;
            t = t.key > key ? t.l : t.r;
        }
        return false;
    }
    static int leqCount(IntRandomizedBinarySearchTree<?> t, int key) {
        if (t == null) return 0;
        if (key < t.key) return leqCount(t.l, key);
        return leqCount(t.r, key) + IntRandomizedBinarySearchTree.size(t.l) + 1;
    }
    public int leqCount(int key) {return leqCount(root, key);}
    static int ltCount(IntRandomizedBinarySearchTree<?> t, int key) {
        if (t == null) return 0;
        if (key <= t.key) return ltCount(t.l, key);
        return ltCount(t.r, key) + IntRandomizedBinarySearchTree.size(t.l) + 1;
    }
    public int ltCount(int key) { return ltCount(root, key); }
    public int geqCount(int key) { return size() - ltCount(key); }
    public int gtCount(int key) { return size() - leqCount(key); }
    public int count(int key) { return leqCount(key) - ltCount(key); }
    public int size() { return IntRandomizedBinarySearchTree.size(root); }
    public boolean isEmpty() { return size() == 0; }
    public void clear() { this.root = null; }
    public PrimitiveIterator.OfInt iterator() { return IntRandomizedBinarySearchTree.keyIterator(root); }
}