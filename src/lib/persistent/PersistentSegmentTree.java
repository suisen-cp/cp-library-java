package lib.persistent;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class PersistentSegmentTree<T> {
    private final int n;
    private final Node<T> root;
    private final BinaryOperator<T> op;
    private final T e;
    public PersistentSegmentTree(T[] a, BinaryOperator<T> op, T e) {
        this.n = a.length;
        this.op = op;
        this.e = e;
        this.root = build(a, 0, n);
    }
    public PersistentSegmentTree(int n, BinaryOperator<T> op, T e) {
        this.n = n;
        this.op = op;
        this.e = e;
        this.root = buildConstant(0, n);
    }
    private PersistentSegmentTree(int n, BinaryOperator<T> op, T e, Node<T> root) {
        this.n = n;
        this.op = op;
        this.e = e;
        this.root = root;
    }
    private Node<T> merge(Node<T> l, Node<T> r) {//noinspection Convert2Diamond
        return new Node<T>(l, r, op.apply(l.v, r.v));}
    private Node<T> build(T[] a, int l, int r) {
        //noinspection Convert2Diamond
        return r - l == 1 ? new Node<T>(a[l]) : merge(build(a, l, (l + r) >> 1), build(a, (l + r) >> 1, r));
    }
    private Node<T> buildConstant(int l, int r) {
        //noinspection Convert2Diamond,Convert2Diamond
        return r - l == 1 ? new Node<T>(e) : new Node<T>(buildConstant(l, (l + r) >> 1), buildConstant((l + r) >> 1, r), e);
    }
    public PersistentSegmentTree<T> update(final int i, final T v) {
        return apply(i, x -> v);
    }
    public PersistentSegmentTree<T> apply(final int i, final UnaryOperator<T> f) {
        //noinspection Convert2Diamond
        return new PersistentSegmentTree<T>(n, op, e, apply(i, f, root, 0, n));
    }
    private Node<T> apply(final int i, final UnaryOperator<T> f, final Node<T> nd, final int l, final int r) {
        if (l < i + 1 && i < r) {
            if (i <= l && r <= i + 1) {
                //noinspection Convert2Diamond
                return new Node<T>(f.apply(nd.v));
            } else {
                return merge(apply(i, f, nd.l, l, (l + r) >> 1), apply(i, f, nd.r, (l + r) >> 1, r));
            }
        }
        return nd;
    }
    public T query(final int l, final int r) {return query(l, r, root, 0, n);}
    private T query(final int ql, final int qr, final Node<T> nd, final int tl, final int tr) {
        if (ql < tr && tl < qr) {
            if (ql <= tl && tr <= qr) {
                return nd.v;
            } else {
                final T lv = query(ql, qr, nd.l, tl, (tl + tr) >> 1);
                final T rv = query(ql, qr, nd.r, (tl + tr) >> 1, tr);
                return op.apply(lv, rv);
            }
        }
        return e;
    }
    private static final class Node<T> {
        private final Node<T> l, r;
        private final T v;
        private Node(T v){this.l = null; this.r = null; this.v = v;}
        private Node(Node<T> l, Node<T> r, T v) {this.l = l; this.r = r; this.v = v;}
    }
}