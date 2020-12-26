package lib.datastructure.longs;

public final class LongRangeAddRangeSum {
    final LongBinaryIndexedTree p;
    final LongBinaryIndexedTree q;
    public LongRangeAddRangeSum(int n) {
        this.p = new LongBinaryIndexedTree(n);
        this.q = new LongBinaryIndexedTree(n);
    }
    public void add(int l, int r, long v) {
        p.add(l, -v * l);
        p.add(r,  v * r);
        q.add(l,  v);
        q.add(r, -v);
    }
    public long sum(int l, int r) {
        long sumr = p.sum(r) + q.sum(r) * r;
        long suml = p.sum(l) + q.sum(l) * l;
        return sumr - suml;
    }
}
