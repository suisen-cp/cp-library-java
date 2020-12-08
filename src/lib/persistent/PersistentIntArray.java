package lib.persistent;

import java.util.function.IntUnaryOperator;

import lib.util.collections.Deque;

public final class PersistentIntArray {
    private static final int[] stack = new int[10];
    private static int ptr = 0;
    private static final int DIV = 20;
    private final int n;
    private final Node root;
    public PersistentIntArray(int[] array) {
        this(array.length, i -> array[i]);
    }
    public PersistentIntArray(int n, int initialValue) {
        this(n, i -> initialValue);
    }
    public PersistentIntArray(int n, IntUnaryOperator generator) {
        this.root = new Node();
        this.n = n;
        build(n, generator);
    }
    private PersistentIntArray(Node root, int n) {this.root = root; this.n = n;}
    public Node getRoot() {return root;}
    private void build(int n, IntUnaryOperator generator) {
        @SuppressWarnings("Convert2Diamond") Deque<Node> q = new Deque<Node>(n);
        int i = 0;
        q.addLast(root);
        while (q.size() > 0) {
            Node p = q.removeFirst();
            p.e = generator.applyAsInt(i++);
            for (int j = 0; i + q.size() < n && j < DIV; j++) {
                p.ch[j] = new Node();
                q.addLast(p.ch[j]);
            }
        }
    }
    public PersistentIntArray set(int i, int v) {
        rangeCheck(i);
        Node oldNode = root;
        Node top = new Node();
        if (i == 0) {
            top.ch = oldNode.ch;
            top.e = v;
            return new PersistentIntArray(top, n);
        }
        Node newNode = top;
        calcPath(i);
        while (ptr > 0) {
            int j = stack[--ptr];
            System.arraycopy(oldNode.ch, 0, newNode.ch, 0, DIV);
            newNode.e = oldNode.e;
            newNode.ch[j] = new Node();
            newNode = newNode.ch[j];
            oldNode = oldNode.ch[j];
        }
        newNode.e = v;
        newNode.ch = oldNode.ch;
        return new PersistentIntArray(top, n);
    }
    public int get(int i) {
        rangeCheck(i);
        Node nd = root;
        calcPath(i);
        while (ptr > 0) {
            int j = stack[--ptr];
            nd = nd.ch[j];
        }
        return nd.e;
    }
    private static void calcPath(int i) {
        while (i > 0) {
            i--;
            stack[ptr++] = i % DIV;
            i /= DIV;
        }
    }
    private void rangeCheck(int i) {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", i, n));
        }
    }
    private static final class Node {
        private int e;
        private Node[] ch = new Node[DIV];
    }
}