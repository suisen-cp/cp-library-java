package lib.datastructure.longs;

import java.util.function.LongBinaryOperator;

public class LongDynSegTree {
    @FunctionalInterface
    public static interface KeyAndValConsumer {
        public void accept(long key, long val);
    }

    private final class Node {
        private int leaves = 1;
        private long val = e;
        private Node l, r;

        private Node left()  { return l == null ? l = new Node() : l; }
        private Node right() { return r == null ? r = new Node() : r; }

        private void update() {
            val = op.applyAsLong(value(l), value(r));
            leaves = leaves(l) + leaves(r);
        }

        private void forEach(KeyAndValConsumer con, long nl, long nr) {
            if (nr - nl == 1) {
                con.accept(nl, val);
            } else {
                long nm = nl + (nr - nl >> 1);
                if (l != null) l.forEach(con, nl, nm);
                if (r != null) r.forEach(con, nm, nr);
            }
        }

        private void forEachDesc(KeyAndValConsumer con, long nl, long nr) {
            if (nr - nl == 1) {
                con.accept(nl, val);
            } else {
                long nm = nl + (nr - nl >> 1);
                if (r != null) r.forEach(con, nm, nr);
                if (l != null) l.forEach(con, nl, nm);
            }
        }
    }

    private long value(Node node) { return node == null ? e : node.val; }
    private int leaves(Node node) { return node == null ? 0 : node.leaves; }

    private final LongBinaryOperator op;
    private final long e;
    private final long n;

    private Node root;
    private final Node[] stack = new Node[64];

    /**
     * Dynamic segment tree for the range [0, n)
     * @param n right bound of the range (<b>exclusive</b>)
     * @param binOp binary operator
     * @param identity identity element
     */
    public LongDynSegTree(long n, LongBinaryOperator binOp, long identity) {
        this.op = binOp;
        this.e = identity;
        long _n = 2;
        while (_n < n) _n <<= 1;
        this.n = _n;
        this.root = new Node();
        root.leaves = 0;
    }

    /**
     * If the specified key is not already associated with a value, associates it with the given value. Otherwise, replaces the associated value with the results of the given remapping function. This method may be of use when combining multiple mapped values for a key.
     * @implNote
     * The implementation is equivalent to performing the following steps for this segment tree, then returning the current value:
     * <pre>{@code
     *long oldVal = t.get(key);
     *long newVal = oldVal == identity ? val :
     *              remappingFunction.apply(oldValue, value);
     * }</pre>
     * @param key key with which the resulting value is to be associated
     * @param val the value to be merged with the existing value associated with the key or, if no existing value is associated with the key, to be associated with the key
     * @param remappingFunction the function to recompute a value if present
     */
    public long merge(long key, long val, LongBinaryOperator remappingFunction) {
        Node cur = root;
        long l = 0, r = n;
        int ptr = 0;
        while (r - l > 1) {
            stack[ptr++] = cur;
            long m = l + (r - l >> 1);
            if (key >= m) {
                l = m;
                cur = cur.right();
            } else {
                r = m;
                cur = cur.left();
            }
        }
        cur.val = remappingFunction.applyAsLong(cur.val, val);
        while (ptr > 0) stack[--ptr].update();
        return cur.val;
    }

    public void put(long key, long val) { merge(key, val, (oldVal, newVal) -> newVal); }

    public long get(long key) {
        Node cur = root;
        long l = 0, r = n;
        while (cur != null && r - l > 1) {
            long m = l + (r - l >> 1);
            if (key >= m) {
                l = m;
                cur = cur.r;
            } else {
                r = m;
                cur = cur.l;
            }
        }
        return value(cur);
    }

    private long prod(Node node, long nl, long nr, long ql, long qr) {
        if (node == null || qr <= nl || nr <= ql) return e;
        if (ql <= nl && nr <= qr) {
            return node.val;
        } else {
            long nm = nl + (nr - nl >> 1);
            long lprod = prod(node.l, nl, nm, ql, qr);
            long rprod = prod(node.r, nm, nr, ql, qr);
            return op.applyAsLong(lprod, rprod);
        }
    }

    public long prod(long l, long r) { return prod(root, 0, n, l, r); }
    public long prodAll() { return prod(0, n); }
    public long prodLeft (long r) { return prod(0, r); }
    public long prodRight(long l) { return prod(l, n); }

    public void forEach    (KeyAndValConsumer con) { root.forEach    (con, 0, n); }
    public void forEachDesc(KeyAndValConsumer con) { root.forEachDesc(con, 0, n); }

    public int size() { return root.leaves; }

    public static LongDynSegTree merge(LongDynSegTree t1, LongDynSegTree t2, LongBinaryOperator remappingFunction) {
        if (t1.size() < t2.size()) return merge(t2, t1, remappingFunction);
        t2.forEach((k, v) -> t1.merge(k, v, remappingFunction));
        return t1;
    }

    public static LongDynSegTree merge(LongDynSegTree t1, LongDynSegTree t2) { return merge(t1, t2, t1.op); }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        forEach((k, v) -> sb.append(k).append("=>").append(v).append(','));
        if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
        sb.append(']');
        return sb.toString();
    }
}
