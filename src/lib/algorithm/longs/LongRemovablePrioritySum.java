package lib.algorithm.longs;

import lib.datastructure.longs.LongOrderedMultiSet;

public class LongRemovablePrioritySum {
    private final boolean isDescending;
    private final LongOrderedMultiSet topk;
    private final LongOrderedMultiSet other;
    private int k;
    private long sumK = 0;
    public LongRemovablePrioritySum(int k, boolean descending) {
        this.isDescending = descending;
        this.topk = new LongOrderedMultiSet();
        this.other = new LongOrderedMultiSet();
        this.k = k;
    }
    public LongRemovablePrioritySum(int k) {
        this(k, false);
    }
    public int  getK()      {return k;}
    public void setK(int k) {this.k = k;}
    public void incrK()     {this.k++;}
    public void decrK()     {this.k--;}
    public void addK(int v) {this.k += v;}
    public void subK(int v) {this.k -= v;}
    public void add(long v) {
        if (topk.size() == 0) {
            other.add(v);
        } else {
            if (isDescending) {
                if (topk.first() <= v) {
                    topk.add(v);
                    sumK += v;
                } else {
                    other.add(v);
                }
            } else {
                if (topk.last() >= v) {
                    topk.add(v);
                    sumK += v;
                } else {
                    other.add(v);
                }
            }
        }
    }
    public long sum() {
        int size = topk.size();
        if (size > k) {
            int d = size - k;
            if (isDescending) {
                while (d --> 0) {
                    long v = topk.kthElement(0);
                    topk.remove(v);
                    sumK -= v;
                    other.add(v);
                }
            } else {
                while (d --> 0) {
                    long v = topk.kthElement(topk.size() - 1);
                    topk.remove(v);
                    sumK -= v;
                    other.add(v);
                }
            }
        } else if (size < k){
            int d = k - size;
            if (isDescending) {
                while (d --> 0 && other.size() > 0) {
                    long v = other.kthElement(other.size() - 1);
                    other.remove(v);
                    sumK += v;
                    topk.add(v);
                }
            } else {
                while (d --> 0 && other.size() > 0) {
                    long v = other.kthElement(0);
                    other.remove(v);
                    sumK += v;
                    topk.add(v);
                }
            }
        }
        return sumK;
    }
    public void remove(long v) {
        if (other.contains(v)) {
            other.remove(v);
        } else if (topk.contains(v)) {
            topk.remove(v);
            sumK -= v;
        }
    }
    public int size() {
        return topk.size() + other.size();
    }
}
