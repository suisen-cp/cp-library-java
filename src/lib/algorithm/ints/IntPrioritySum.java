package lib.algorithm.ints;

import lib.util.collections.ints.IntPriorityQueue;

public final class IntPrioritySum {
    private final boolean isDescending;
    private final IntPriorityQueue topk;
    private final IntPriorityQueue other;
    private int k;
    private int sumK = 0;
    public IntPrioritySum(int k, boolean descending) {
        this.isDescending = descending;
        this.topk = new IntPriorityQueue(!descending);
        this.other = new IntPriorityQueue(descending);
        this.k = k;
    }
    public IntPrioritySum(int k) {
        this(k, false);
    }
    public int  getK()      {return k;}
    public void setK(int k) {this.k = k;}
    public void incrK()     {this.k++;}
    public void decrK()     {this.k--;}
    public void addK(int v) {this.k += v;}
    public void subK(int v) {this.k -= v;}
    public void add(int v) {
        if (topk.size() == 0 || topk.getFirst() < v ^ isDescending) {
            other.add(v);
        } else {
            topk.add(v);
            sumK += v;
        }
    }
    public int sum() {
        int size = topk.size();
        if (size > k) {
            int d = size - k;
            while (d --> 0) {
                int v = topk.removeFirst();
                sumK -= v;
                other.add(v);
            }
        } else if (size < k){
            int d = k - size;
            while (d --> 0 && other.size() > 0) {
                int v = other.removeFirst();
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

