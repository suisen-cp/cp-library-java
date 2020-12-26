package lib.persistent;

import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

public class PersistentLongSegmentTree {
    private final int n;
    private final Node root;
    private final LongBinaryOperator op;
    private final long e;
    public PersistentLongSegmentTree(long[] a, LongBinaryOperator op, long e) {
        this.n = a.length;
        this.op = op;
        this.e = e;
        this.root = build(a, 0, n);
    }
    public PersistentLongSegmentTree(int n, LongBinaryOperator op, long e) {
        this.n = n;
        this.op = op;
        this.e = e;
        this.root = buildConstant(0, n);
    }
    private PersistentLongSegmentTree(int n, LongBinaryOperator op, long e, Node root) {
        this.n = n;
        this.op = op;
        this.e = e;
        this.root = root;
    }
    private Node merge(Node l, Node r) {return new Node(l, r, op.applyAsLong(l.v, r.v));}
    private Node build(long[] a, int l, int r) {
        return r - l == 1 ? new Node(a[l]) : merge(build(a, l, (l + r) >> 1), build(a, (l + r) >> 1, r));
    }
    private Node buildConstant(int l, int r) {
        return r - l == 1 ? new Node(e) : new Node(buildConstant(l, (l + r) >> 1), buildConstant((l + r) >> 1, r), e);
    }
    public PersistentLongSegmentTree update(final int i, final long v) {
        return apply(i, x -> v);
    }
    public PersistentLongSegmentTree apply(final int i, final LongUnaryOperator f) {
        return new PersistentLongSegmentTree(n, op, e, apply(i, f, root, 0, n));
    }
    private Node apply(final int i, final LongUnaryOperator f, final Node nd, final int l, final int r) {
        if (l < i + 1 && i < r) {
            if (i <= l && r <= i + 1) {
                return new Node(f.applyAsLong(nd.v));
            } else {
                return merge(apply(i, f, nd.l, l, (l + r) >> 1), apply(i, f, nd.r, (l + r) >> 1, r));
            }
        }
        return nd;
    }
    public long query(final int l, final int r) {return query(l, r, root, 0, n);}
    private long query(final int ql, final int qr, final Node nd, final int tl, final int tr) {
        if (ql < tr && tl < qr) {
            if (ql <= tl && tr <= qr) {
                return nd.v;
            } else {
                final long lv = query(ql, qr, nd.l, tl, (tl + tr) >> 1);
                final long rv = query(ql, qr, nd.r, (tl + tr) >> 1, tr);
                return op.applyAsLong(lv, rv);
            }
        }
        return e;
    }
    private static final class Node {
        private final Node l, r;
        private final long v;
        private Node(long v){this.l = null; this.r = null; this.v = v;}
        private Node(Node l, Node r, long v) {this.l = l; this.r = r; this.v = v;}
    }
}