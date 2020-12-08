package lib.persistent;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public class PersistentIntStack {
    private Node last;
    private int size = 0;
    private PersistentIntStack(Node last, int size) {
        this.last = last;
        this.size = size;
    }
    public PersistentIntStack() {}
    public PersistentIntStack add(int v) {
        return new PersistentIntStack(new Node(last, v), size + 1);
    }
    public PersistentIntStack poll() {
        requireNonEmpty();
        return new PersistentIntStack(last.prev, size - 1);
    }
    public int peek() {
        requireNonEmpty();
        return last.v;
    }
    public int size() {
        return size;
    }
    public PrimitiveIterator.OfInt iterator() {
        return new PersistentIntStackIterator();
    }
    private void requireNonEmpty() {
        if (last == null) throw new NoSuchElementException("Queue is empty.");
    }
    private static class Node {
        private final Node prev;
        private final int v;
        private Node(Node prev, int v) {
            this.prev = prev;
            this.v = v;
        }
    }
    private class PersistentIntStackIterator implements PrimitiveIterator.OfInt {
        private Node it = last;
        public boolean hasNext() {return it != null;}
        public int nextInt() {int ret = it.v; it = it.prev; return ret;}
    }
}