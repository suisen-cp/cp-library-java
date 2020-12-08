package lib.datastructure.ints;

public class IntOrderedMultiSet extends IntRandomizedBinarySearchTree<Object> {
    Node root;
    int kthElement(Node t, int k) {
        int c = size(t.l);
        if (k < c) return kthElement(t.l, k);
        if (k == c) return t.key;
        return kthElement(t.r, k - c - 1);
    }
    public int kthElement(int k) {
        if (k < 0 || k >= size()) throw new IndexOutOfBoundsException();
        return kthElement(root, k);
    }
    Node insertKey(Node t, int key) {
        return insert(t, leqCount(t, key), key, null);
    }
    public void insertKey(int key) {
        root = insertKey(root, key);
    }
    Node eraseKey(Node t, int key) {
        if (count(t, key) == 0) return t;
        return super.erase(t, leqCount(t, key) - 1);
    }
    public void eraseKey(int key) {
        root = eraseKey(root, key);
    }
    int count(Node t, int key) {
        return leqCount(t, key) - ltCount(t, key);
    }
    public int count(int key) {
        return count(root, key);
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