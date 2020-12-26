package lib.persistent;

import java.util.function.IntFunction;

import lib.util.collections.Deque;

public final class PersistentArray<T> {
    private static final int[] stack = new int[10];
    private static int ptr = 0;
    private static final int DIV = 20;
    private final int n;
    private final Node<T> root;
    public PersistentArray(T[] array) {
        this(array.length, i -> array[i]);
    }
    public PersistentArray(int n, T initialValue) {
        this(n, i -> initialValue);
    }
    public PersistentArray(int n, IntFunction<T> generator) {
        //noinspection Convert2Diamond
        this.root = new Node<T>();
        this.n = n;
        build(n, generator);
    }
    private PersistentArray(Node<T> root, int n) {this.root = root; this.n = n;}
    public Node<T> getRoot() {return root;}
    private void build(int n, IntFunction<T> generator) {
        @SuppressWarnings("Convert2Diamond") Deque<Node<T>> q = new Deque<Node<T>>(n);
        int i = 0;
        q.addLast(root);
        while (q.size() > 0) {
            Node<T> p = q.removeFirst();
            p.e = generator.apply(i++);
            for (int j = 0; i + q.size() < n && j < DIV; j++) {
                //noinspection Convert2Diamond
                p.ch[j] = new Node<T>();
                q.addLast(p.ch[j]);
            }
        }
    }
    public PersistentArray<T> set(int i, T v) {
        rangeCheck(i);
        Node<T> oldNode = root;
        @SuppressWarnings("Convert2Diamond") Node<T> top = new Node<T>();
        if (i == 0) {
            top.ch = oldNode.ch;
            top.e = v;
            //noinspection Convert2Diamond
            return new PersistentArray<T>(top, n);
        }
        Node<T> newNode = top;
        calcPath(i);
        while (ptr > 0) {
            int j = stack[--ptr];
            System.arraycopy(oldNode.ch, 0, newNode.ch, 0, DIV);
            newNode.e = oldNode.e;
            //noinspection Convert2Diamond
            newNode.ch[j] = new Node<T>();
            newNode = newNode.ch[j];
            oldNode = oldNode.ch[j];
        }
        newNode.e = v;
        newNode.ch = oldNode.ch;
        //noinspection Convert2Diamond
        return new PersistentArray<T>(top, n);
    }
    public T get(int i) {
        rangeCheck(i);
        Node<T> nd = root;
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
    @SuppressWarnings("unchecked")
    private static final class Node<T> {
        private T e;
        private Node<T>[] ch = new Node[DIV];
    }
}