package lib.algorithm.ints;

import lib.datastructure.ints.IntOrderedMultiSet;

public class IntRemovablePrioritySum {
    private final boolean isDescending;
    private final IntOrderedMultiSet topk;
    private final IntOrderedMultiSet other;
    private int k;
    private int sumK = 0;
    public IntRemovablePrioritySum(int k, boolean descending) {
        this.isDescending = descending;
        this.topk = new IntOrderedMultiSet();
        this.other = new IntOrderedMultiSet();
        this.k = k;
    }
    public IntRemovablePrioritySum(int k) {
        this(k, false);
    }
    public int  getK()      {return k;}
    public void setK(int k) {this.k = k;}
    public void incrK()     {this.k++;}
    public void decrK()     {this.k--;}
    public void addK(int v) {this.k += v;}
    public void subK(int v) {this.k -= v;}
    public void add(int v) {
        if (topk.size() == 0) {
            other.add(v);
        } else {
            if (isDescending) {
                if (topk.kthElement(0) <= v) {
                    topk.add(v);
                    sumK += v;
                } else {
                    other.add(v);
                }
            } else {
                if (topk.kthElement(topk.size() - 1) >= v) {
                    topk.add(v);
                    sumK += v;
                } else {
                    other.add(v);
                }
            }
        }
    }
    public int sum() {
        int size = topk.size();
        if (size > k) {
            int d = size - k;
            if (isDescending) {
                while (d --> 0) {
                    int v = topk.kthElement(0);
                    topk.remove(v);
                    sumK -= v;
                    other.add(v);
                }
            } else {
                while (d --> 0) {
                    int v = topk.kthElement(topk.size() - 1);
                    topk.remove(v);
                    sumK -= v;
                    other.add(v);
                }
            }
        } else if (size < k){
            int d = k - size;
            if (isDescending) {
                while (d --> 0 && other.size() > 0) {
                    int v = other.kthElement(other.size() - 1);
                    other.remove(v);
                    sumK += v;
                    topk.add(v);
                }
            } else {
                while (d --> 0 && other.size() > 0) {
                    int v = other.kthElement(0);
                    other.remove(v);
                    sumK += v;
                    topk.add(v);
                }
            }
        }
        return sumK;
    }
    public void remove(int v) {
        if (other.count(v) > 0) {
            other.remove(v);
        } else if (topk.count(v) > 0) {
            topk.remove(v);
            sumK -= v;
        }
    }
    public int size() {
        return topk.size() + other.size();
    }
}
