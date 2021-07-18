package lib.datastructure.ints;

import java.util.PrimitiveIterator;
import java.util.function.IntUnaryOperator;

import lib.util.pair.Pair;

public class DynamicIntArray implements Cloneable {
    private static final Object DUMMY = new Object();
    private IntRandomizedBinarySearchTree<Object> root;

    public DynamicIntArray() {}
    public DynamicIntArray(int n, IntUnaryOperator generator) {
        for (int i = 0; i < n; i++) addLast(generator.applyAsInt(i));
    }
    public DynamicIntArray(int n, int initialValue) { this(n, i -> initialValue); }
    public DynamicIntArray(int[] a) { this(a.length, i -> a[i]); }

    private DynamicIntArray(IntRandomizedBinarySearchTree<Object> root) {
        this.root = root;
    }

    public int size() {
        return IntRandomizedBinarySearchTree.size(root);
    }
    public int get(int index) {
        return IntRandomizedBinarySearchTree.getKthKey(root, index);
    }
    public int set(int index, int newVal) {
        indexBoundsCheck(index);
        return IntRandomizedBinarySearchTree.setKthKey(root, index, newVal);
    }
    public void insert(int index, int val) {
        root = IntRandomizedBinarySearchTree.insert(root, index, val, DUMMY);
    }
    public void addLast(int val) {
        root = IntRandomizedBinarySearchTree.insert(root, size(), val, DUMMY);
    }
    public void eraseRange(int l, int r) {
        root = IntRandomizedBinarySearchTree.eraseRange(root, l, r);
    }
    public Pair<DynamicIntArray, DynamicIntArray> split(int k) {
        Pair<IntRandomizedBinarySearchTree<Object>, IntRandomizedBinarySearchTree<Object>> p = IntRandomizedBinarySearchTree.split(root, k);
        DynamicIntArray fst = new DynamicIntArray(p.fst);
        DynamicIntArray snd = new DynamicIntArray(p.snd);
        return new Pair<>(fst, snd);
    }
    public void mergeRight(DynamicIntArray a) {
        root = IntRandomizedBinarySearchTree.merge(root, a.root);
    }
    public void mergeLeft(DynamicIntArray a) {
        root = IntRandomizedBinarySearchTree.merge(a.root, root);
    }

    private void indexBoundsCheck(int i) {
        if (i < 0 || i >= size()) throw new IndexOutOfBoundsException(
            String.format("index %d out of bounds for the length %d.", i, size())
        );
    }

    public PrimitiveIterator.OfInt iterator() {
        return IntRandomizedBinarySearchTree.keyIterator(root);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append('[');
        int n = size();
        for (PrimitiveIterator.OfInt it = iterator(); it.hasNext();) {
            sb.append(it.nextInt());
            if (0 <-- n) sb.append(',');
        }
        return sb.append(']').toString();
    }

    @Override
    public DynamicIntArray clone() {
        return new DynamicIntArray(size(), i -> get(i));
    }
}
