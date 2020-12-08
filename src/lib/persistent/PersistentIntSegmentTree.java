package lib.persistent;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class PersistentIntSegmentTree {
    private final int n;
    private final Node root;
    private final IntBinaryOperator op;
    private final int e;
    public PersistentIntSegmentTree(int[] a, IntBinaryOperator op, int e) {
        this.n = a.length;
        this.op = op;
        this.e = e;
        this.root = build(a, 0, n);
    }
    public PersistentIntSegmentTree(int n, IntBinaryOperator op, int e) {
        this.n = n;
        this.op = op;
        this.e = e;
        this.root = buildConstant(0, n);
    }
    private PersistentIntSegmentTree(int n, IntBinaryOperator op, int e, Node root) {
        this.n = n;
        this.op = op;
        this.e = e;
        this.root = root;
    }
    private Node merge(Node l, Node r) {return new Node(l, r, op.applyAsInt(l.v, r.v));}
    private Node build(int[] a, int l, int r) {
        return r - l == 1 ? new Node(a[l]) : merge(build(a, l, (l + r) >> 1), build(a, (l + r) >> 1, r));
    }
    private Node buildConstant(int l, int r) {
        return r - l == 1 ? new Node(e) : new Node(buildConstant(l, (l + r) >> 1), buildConstant((l + r) >> 1, r), e);
    }
    public PersistentIntSegmentTree update(final int i, final int v) {
        return apply(i, x -> v);
    }
    public PersistentIntSegmentTree apply(final int i, final IntUnaryOperator f) {
        return new PersistentIntSegmentTree(n, op, e, apply(i, f, root, 0, n));
    }
    private Node apply(final int i, final IntUnaryOperator f, final Node nd, final int l, final int r) {
        if (l < i + 1 && i < r) {
            if (i <= l && r <= i + 1) {
                return new Node(f.applyAsInt(nd.v));
            } else {
                return merge(apply(i, f, nd.l, l, (l + r) >> 1), apply(i, f, nd.r, (l + r) >> 1, r));
            }
        }
        return nd;
    }
    public int query(final int l, final int r) {return query(l, r, root, 0, n);}
    private int query(final int ql, final int qr, final Node nd, final int tl, final int tr) {
        if (ql < tr && tl < qr) {
            if (ql <= tl && tr <= qr) {
                return nd.v;
            } else {
                final int lv = query(ql, qr, nd.l, tl, (tl + tr) >> 1);
                final int rv = query(ql, qr, nd.r, (tl + tr) >> 1, tr);
                return op.applyAsInt(lv, rv);
            }
        }
        return e;
    }
    private static final class Node {
        private final Node l, r;
        private final int v;
        private Node(int v){this.l = null; this.r = null; this.v = v;}
        private Node(Node l, Node r, int v) {this.l = l; this.r = r; this.v = v;}
    }
}