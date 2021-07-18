package lib.datastructure.longs;

import java.lang.reflect.Array;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.LongBinaryOperator;

import lib.util.function.ObjLongToLongFunction;
import lib.util.function.ObjLongToObjFunction;

public class LongObjDynLazySegTree<F> {
    private final class Node {
        private int leaves = 1;
        private F laz = e1;
        private long val = e0;
        private final long nl, nr;
        private Node l, r;

        private Node(long nl, long nr) { this.nl = nl; this.nr = nr; }

        private Node left()  { return l == null ? l = new Node(nl, nl + (nr - nl >> 1)) : l; }
        private Node right() { return r == null ? r = new Node(nl + (nr - nl >> 1), nr) : r; }

        private void update() {
            val = op.applyAsLong(value(l), value(r));
            leaves = leaves(l) + leaves(r);
        }

        private void compose(F _laz) {
            laz = (laz == e1 ? _laz : composition.apply(_laz, laz));
        }

        private void propagate() {
            if (laz == e1) return;
            if (nr - nl > 1) {
                left ().compose(laz);
                right().compose(laz);
            }
            val = mapping.apply(prodComposition.apply(laz, nr - nl), val);
            laz = e1;
        }

        private void forEach(Consumer<Node> con) {
            if (l == null && r == null) {
                if (nr - nl == 1) propagate();
                con.accept(this);
                return;
            }
            propagate();
            l.forEach(con);
            r.forEach(con);
        }
    }

    private long value(Node node) {
        if (node == null) return e0;
        node.propagate();
        return node.val;
    }
    private static int leaves(LongObjDynLazySegTree<?>.Node node) { return node == null ? 0 : node.leaves; }

    private final LongBinaryOperator op;
    private final long e0;
    private final ObjLongToLongFunction<F> mapping;
    private final BinaryOperator<F> composition;
    private final F e1;
    private final ObjLongToObjFunction<F, F> prodComposition;
    private final long n;

    private Node root;
    @SuppressWarnings("unchecked")
    private final Node[] stack = (Node[]) Array.newInstance(new Node(0, 0).getClass(), 64);

    public LongObjDynLazySegTree(
        long n,
        LongBinaryOperator binOp, long e0,
        ObjLongToLongFunction<F> mapping,
        BinaryOperator<F> composition, F e1,
        ObjLongToObjFunction<F, F> prodComposition
    ) {
        this.op = binOp;
        this.e0 = e0;
        this.mapping = mapping;
        this.composition = composition;
        this.e1 = e1;
        this.prodComposition = prodComposition;
        long _n = 2;
        while (_n < n) _n <<= 1;
        this.n = _n;
        this.root = new Node(0L, _n);
    }

    public void put(long key, long val) { merge(key, val, (oldVal, newVal) -> newVal); }

    public long merge(long key, long val, LongBinaryOperator remappingFunction) {
        Node cur = root;
        long l = 0, r = n;
        int ptr = 0;
        while (r - l > 1) {
            cur.propagate();
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
        cur.val = remappingFunction.applyAsLong(value(cur), val);
        while (ptr > 0) stack[--ptr].update();
        return cur.val;
    }

    private void apply(Node node, F laz, long ql, long qr) {
        long nl = node.nl, nr = node.nr;
        if (qr <= nl || nr <= ql) return;
        if (ql <= nl && nr <= qr) {
            node.compose(laz);
        } else {
            node.propagate();
            apply(node.left(),  laz, ql, qr);
            apply(node.right(), laz, ql, qr);
            node.update();
        }
    }

    public void apply(long ql, long qr, F f) { apply(root, f, ql, qr); }
    public void applyLeft(long qr, F f) { apply(0, qr, f); }
    public void applyRight(long ql, F f) { apply(ql, n, f); }
    public void applyAll(F f) { apply(0, n, f); }

    public long get(long key) {
        Node cur = root;
        long l = 0, r = n;
        while (cur != null && r - l > 1) {
            cur.propagate();
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

    private long prod(Node node, long ql, long qr) {
        if (node == null) return e0;
        long nl = node.nl, nr = node.nr;
        if (qr <= nl || nr <= ql) return e0;
        if (ql <= nl && nr <= qr) {
            return value(node);
        } else {
            node.propagate();
            long lprod = prod(node.l, ql, qr);
            long rprod = prod(node.r, ql, qr);
            return op.applyAsLong(lprod, rprod);
        }
    }

    public long prod(long l, long r) { return prod(root, l, r); }
    public long prodAll() { return prod(0, n); }
    public long prodLeft (long r) { return prod(0, r); }
    public long prodRight(long l) { return prod(l, n); }

    public static int comparelongize(LongObjDynLazySegTree<?> t1, LongObjDynLazySegTree<?> t2) { return leaves(t1.root) - leaves(t2.root); }

    private void forEach(Consumer<Node> con) { root.forEach(con); }

    public static <F> LongObjDynLazySegTree<F> merge(LongObjDynLazySegTree<F> t1, LongObjDynLazySegTree<F> t2) {
        if (comparelongize(t1, t2) < 0) return merge(t2, t1);
        t2.forEach(node -> {
            long nl = node.nl, nr = node.nr;
            if (nr - nl == 1) {
                t1.merge(nl, t2.value(node), t1.op);
            } else {
                t1.apply(nl, nr, node.laz);
            }
        });
        return t1;
    }
}
