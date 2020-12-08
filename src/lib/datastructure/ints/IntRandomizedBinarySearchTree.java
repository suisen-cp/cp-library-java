package lib.datastructure.ints;

import lib.util.Random;
import lib.util.pair.IntEntry;

public class IntRandomizedBinarySearchTree<V> {
    private Node splitLeft, splitRight;
    private final Random rnd = new Random();
    Node merge(Node l, Node r) {
        if (l == null) return r;
        if (r == null) return l;
        if (rnd.nextInt(l.size + r.size) < l.size) {
            l.r = merge(l.r, r);
            return l.update();
        } else {
            r.l = merge(l, r.l);
            return r.update();
        }
    }
    void split(Node x, int k) {
        if (k < 0 || k > size(x)) {
            throw new IndexOutOfBoundsException(
                String.format("index %d is out of bounds for the length of %d", k, size(x))
            );
        }
        if (x == null) {
            splitLeft = null;
            splitRight = null;
        } else if (k <= size(x.l)) {
            split(x.l, k);
            x.l = splitRight;
            splitRight = x.update();
        } else {
            split(x.r, k - size(x.l) - 1);
            x.r = splitLeft;
            splitLeft = x.update();
        }
    }
    Node insert(Node t, int k, int key, V val) {
        split(t, k);
        return merge(merge(splitLeft, new Node(key, val)), splitRight);
    }
    Node erase(Node t, int k) {
        split(t, k);
        Node l = splitLeft;
        split(splitRight, 1);
        return merge(l, splitRight);
    }
    int size(Node nd) {return nd == null ? 0 : nd.size;}
    class Node extends IntEntry<V> {
        Node l, r;
        int size;
        private Node(int key, V val) {super(key, val); this.size = 1;}
        private Node update() {
            size = size(l) + size(r) + 1;
            return this;
        }
    }
}