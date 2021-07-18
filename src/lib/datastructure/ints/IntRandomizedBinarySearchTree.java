package lib.datastructure.ints;

import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;

import lib.util.Random;
import lib.util.itertools.IterUtil;
import lib.util.pair.IntEntry;
import lib.util.pair.Pair;

public class IntRandomizedBinarySearchTree<V> extends IntEntry<V> {
    IntRandomizedBinarySearchTree<V> l, r;
    int size;
    IntRandomizedBinarySearchTree(int key, V val) {super(key, val); this.size = 1;}
    private IntRandomizedBinarySearchTree<V> update() {
        size = size(l) + size(r) + 1;
        return this;
    }

    private static final Random rnd = new Random();

    static int getKthKey(IntRandomizedBinarySearchTree<?> t, int k) {
        int c = IntRandomizedBinarySearchTree.size(t.l);
        if (k < c) return getKthKey(t.l, k);
        if (k == c) return t.key;
        return getKthKey(t.r, k - c - 1);
    }

    static int setKthKey(IntRandomizedBinarySearchTree<?> t, int k, int newKey) {
        int c = IntRandomizedBinarySearchTree.size(t.l);
        if (k < c) return setKthKey(t.l, k, newKey);
        if (k == c) {
            int oldKey = t.key;
            t.key = newKey;
            return oldKey;
        }
        return setKthKey(t.r, k - c - 1, newKey);
    }

    static <V> IntRandomizedBinarySearchTree<V> merge(IntRandomizedBinarySearchTree<V> l, IntRandomizedBinarySearchTree<V> r) {
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
    static <V> Pair<IntRandomizedBinarySearchTree<V>, IntRandomizedBinarySearchTree<V>> split(IntRandomizedBinarySearchTree<V> x, int k) {
        if (k < 0 || k > size(x)) {
            throw new IndexOutOfBoundsException(
                String.format("index %d is out of bounds for the length of %d", k, size(x))
            );
        }
        if (x == null) return new Pair<>(null, null);
        Pair<IntRandomizedBinarySearchTree<V>, IntRandomizedBinarySearchTree<V>> p;
        if (k <= size(x.l)) {
            p = split(x.l, k);
            x.l = p.snd;
            p.snd = x.update();
        } else {
            p = split(x.r, k - size(x.l) - 1);
            x.r = p.fst;
            p.fst = x.update();
        }
        return p;
    }
    static <V> IntRandomizedBinarySearchTree<V> insert(IntRandomizedBinarySearchTree<V> t, int k, int key, V val) {
        Pair<IntRandomizedBinarySearchTree<V>, IntRandomizedBinarySearchTree<V>> p = split(t, k);
        return merge(merge(p.fst, new IntRandomizedBinarySearchTree<>(key, val)), p.snd);
    }
    static <V> IntRandomizedBinarySearchTree<V> erase(IntRandomizedBinarySearchTree<V> t, int k) {
        Pair<IntRandomizedBinarySearchTree<V>, IntRandomizedBinarySearchTree<V>> p = split(t, k);
        IntRandomizedBinarySearchTree<V> l = p.fst;
        IntRandomizedBinarySearchTree<V> r = split(p.snd, 1).snd;
        return merge(l, r);
    }
    static <V> IntRandomizedBinarySearchTree<V> eraseRange(IntRandomizedBinarySearchTree<V> t, int from, int to) {
        Pair<IntRandomizedBinarySearchTree<V>, IntRandomizedBinarySearchTree<V>> p = split(t, from);
        IntRandomizedBinarySearchTree<V> l = p.fst;
        IntRandomizedBinarySearchTree<V> r = split(p.snd, to - from).snd;
        return merge(l, r);
    }
    static int size(IntRandomizedBinarySearchTree<?> nd) {return nd == null ? 0 : nd.size;}

    static <V> Iterator<IntEntry<V>> iterator(IntRandomizedBinarySearchTree<V> nd) {
        return nd == null ? IterUtil.emptyIterator() : nd.iterator();
    }

    static PrimitiveIterator.OfInt keyIterator(IntRandomizedBinarySearchTree<?> nd) {
        return nd == null ? IterUtil.emptyIntIterator() : nd.keyIterator();
    }

    private Iterator<IntEntry<V>> iterator() {
        Iterator<IntEntry<V>> it = List.<IntEntry<V>>of(this).iterator();
        if (l != null) it = IterUtil.concatIterators(l.iterator(), it);
        if (r != null) it = IterUtil.concatIterators(it, r.iterator());
        return it;
    }

    private PrimitiveIterator.OfInt keyIterator() {
        PrimitiveIterator.OfInt it = IterUtil.ofIntIterator(key);
        if (l != null) it = IterUtil.concatIntIterators(l.keyIterator(), it);
        if (r != null) it = IterUtil.concatIntIterators(it, r.keyIterator());
        return it;
    }
}