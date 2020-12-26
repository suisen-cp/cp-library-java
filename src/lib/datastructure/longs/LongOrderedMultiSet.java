package lib.datastructure.longs;

import java.util.OptionalLong;

import lib.util.Random;
import lib.util.pair.Pair;

public class LongOrderedMultiSet {
    private static final Random rnd = new Random();
    Node root;
    public LongOrderedMultiSet() {}
    private LongOrderedMultiSet(Node root) {this.root = root;}
    public static LongOrderedMultiSet merge(LongOrderedMultiSet l, LongOrderedMultiSet r) {
        return l.mergeRight(r);
    }
    public LongOrderedMultiSet mergeLeft(LongOrderedMultiSet l) {
        return new LongOrderedMultiSet(Node.merge(l.root, root));
    }
    public LongOrderedMultiSet mergeRight(LongOrderedMultiSet r) {
        return new LongOrderedMultiSet(Node.merge(root, r.root));
    }
    public Pair<LongOrderedMultiSet, LongOrderedMultiSet> splitWithIndex(int k) {
        Pair<Node, Node> p = Node.splitWithIndex(root, k);
        LongOrderedMultiSet fst = new LongOrderedMultiSet(p.fst);
        LongOrderedMultiSet snd = new LongOrderedMultiSet(p.snd);
        //noinspection Convert2Diamond
        return new Pair<LongOrderedMultiSet, LongOrderedMultiSet>(fst, snd);
    }
    public Pair<LongOrderedMultiSet, LongOrderedMultiSet> splitWithElement(long e) {
        Pair<Node, Node> p = Node.splitWithElement(root, e);
        LongOrderedMultiSet fst = new LongOrderedMultiSet(p.fst);
        LongOrderedMultiSet snd = new LongOrderedMultiSet(p.snd);
        //noinspection Convert2Diamond
        return new Pair<LongOrderedMultiSet, LongOrderedMultiSet>(fst, snd);
    }
    public long kthElement(int k) {
        if (k < 0 || k >= size()) throw new IndexOutOfBoundsException();
        return Node.kthElement(root, k);
    }
    public long first() {
        return kthElement(0);
    }
    public long last() {
        return kthElement(size() - 1);
    }
    public long lower(long key) {
        return kthElement(Node.ltCount(root, key) - 1);
    }
    public long floor(long key) {
        return kthElement(Node.leqCount(root, key) - 1);
    }
    public long higher(long key) {
        return kthElement(Node.leqCount(root, key));
    }
    public long ceil(long key) {
        return kthElement(Node.ltCount(root, key));
    }
    public void add(long e) {
        root = Node.insert(root, e);
    }
    public void removeKthElement(int k) {
        root = Node.eraseWithIndex(root, k);
    }
    public void remove(long e) {
        if (!contains(e)) return;
        root = Node.eraseWithElement(root, e);
    }
    public boolean contains(long e) {
        return Node.contains(root, e);
    }
    public int size() {
        return Node.size(root);
    }
    public void clear() {
        root = null;
    }
    public OptionalLong safeFirst() {
        return size() > 0 ? OptionalLong.of(kthElement(0)) : OptionalLong.empty();
    }
    public OptionalLong safeLast() {
        return size() > 0 ? OptionalLong.of(kthElement(size() - 1)) : OptionalLong.empty();
    }
    public OptionalLong safeLower(long key) {
        int k = Node.ltCount(root, key) - 1;
        return k >= 0 ? OptionalLong.of(kthElement(k)) : OptionalLong.empty();
    }
    public OptionalLong safeFloor(long key) {
        int k = Node.leqCount(root, key) - 1;
        return k >= 0 ? OptionalLong.of(kthElement(k)) : OptionalLong.empty();
    }
    public OptionalLong safeHigher(long key) {
        int k = Node.leqCount(root, key);
        return k < size() ? OptionalLong.of(kthElement(k)) : OptionalLong.empty();
    }
    public OptionalLong safeCeil(long key) {
        int k = Node.ltCount(root, key);
        return k < size() ? OptionalLong.of(kthElement(k)) : OptionalLong.empty();
    }
    static final class Node {
        private final long key;
        private Node l, r;
        private int size;
        private Node(long key) {this.key = key; this.size = 1;}
        private Node update() {
            size = size(l) + size(r) + 1;
            return this;
        }
        static long kthElement(Node t, int k) {
            int c = size(t.l);
            if (k < c) return kthElement(t.l, k);
            if (k == c) return t.key;
            return kthElement(t.r, k - c - 1);
        }
        static int leqCount(Node t, long key) {
            if (t == null) return 0;
            if (key < t.key) return leqCount(t.l, key);
            return leqCount(t.r, key) + size(t.l) + 1;
        }
        static int ltCount(Node t, long key) {
            if (t == null) return 0;
            if (key <= t.key) return ltCount(t.l, key);
            return ltCount(t.r, key) + size(t.l) + 1;
        }
        static Node merge(Node l, Node r) {
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
        static Pair<Node, Node> splitWithIndex(Node x, int k) {
            if (k < 0 || k > size(x)) {
                throw new IndexOutOfBoundsException(
                    String.format("index %d is out of bounds for the length of %d", k, size(x))
                );
            }
            if (x == null) {
                //noinspection Convert2Diamond
                return new Pair<Node, Node>(null, null);
            } else if (k <= size(x.l)) {
                Pair<Node, Node> p = splitWithIndex(x.l, k);
                x.l = p.snd;
                p.snd = x.update();
                return p;
            } else {
                Pair<Node, Node> p = splitWithIndex(x.r, k - size(x.l) - 1);
                x.r = p.fst;
                p.fst = x.update();
                return p;
            }
        }
        static Pair<Node, Node> splitWithElement(Node x, long e) {
            if (x == null) {
                //noinspection Convert2Diamond
                return new Pair<Node, Node>(null, null);
            } else if (e <= x.key) {
                Pair<Node, Node> p = splitWithElement(x.l, e);
                x.l = p.snd;
                p.snd = x.update();
                return p;
            } else {
                Pair<Node, Node> p = splitWithElement(x.r, e);
                x.r = p.fst;
                p.fst = x.update();
                return p;
            }
        }
        static Node insert(Node t, long e) {
            Pair<Node, Node> p = splitWithElement(t, e);
            return Node.merge(Node.merge(p.fst, new Node(e)), p.snd);
        }
        static Node eraseWithIndex(Node t, int k) {
            Pair<Node, Node> p = splitWithIndex(t, k);
            Pair<Node, Node> q = splitWithIndex(p.snd, 1);
            return Node.merge(p.fst, q.snd);
        }
        static Node eraseWithElement(Node t, long e) {
            Pair<Node, Node> p = splitWithElement(t, e);
            Pair<Node, Node> q = splitWithIndex(p.snd, 1);
            return Node.merge(p.fst, q.snd);
        }
        static boolean contains(Node t, long e) {
            while (t != null) {
                if (t.key == e) return true;
                else if (t.key < e) t = t.r;
                else t = t.l;
            }
            return false;
        }
        static int size(Node nd) {
            return nd == null ? 0 : nd.size;
        }
    }
}