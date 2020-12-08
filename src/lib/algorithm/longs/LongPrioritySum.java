package lib.algorithm.longs;

import lib.util.collections.longs.LongPriorityQueue;

public final class LongPrioritySum {
    private final boolean isDescending;
    private final LongPriorityQueue topk;
    private final LongPriorityQueue other;
    private int k;
    private long sumK = 0;
    public LongPrioritySum(int k, boolean descending) {
        this.isDescending = descending;
        this.topk = new LongPriorityQueue(!descending);
        this.other = new LongPriorityQueue(descending);
        this.k = k;
    }
    public LongPrioritySum(int k) {
        this(k, false);
    }
    public int  getK()      {return k;}
    public void setK(int k) {this.k = k;}
    public void incrK()     {this.k++;}
    public void decrK()     {this.k--;}
    public void addK(int v) {this.k += v;}
    public void subK(int v) {this.k -= v;}
    public void add(long v) {
        if (topk.size() == 0 || topk.getFirst() < v ^ isDescending) {
            other.add(v);
        } else {
            topk.add(v);
            sumK += v;
        }
    }
    public long sum() {
        int size = topk.size();
        if (size > k) {
            int d = size - k;
            while (d --> 0) {
                long v = topk.removeFirst();
                sumK -= v;
                other.add(v);
            }
        } else if (size < k){
            int d = k - size;
            while (d --> 0 && other.size() > 0) {
                long v = other.removeFirst();
                sumK += v;
                topk.add(v);
            }
        }
        return sumK;
    }
    public int size() {
        return topk.size() + other.size();
    }
}
