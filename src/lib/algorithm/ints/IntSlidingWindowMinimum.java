package lib.algorithm.ints;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.IntUnaryOperator;

public class IntSlidingWindowMinimum {
    private static enum IndexPrioritizingMode { PRIORITIZE_MIN_INDEX, PRIORITIZE_MAX_INDEX; }
    public static final IndexPrioritizingMode PRIORITIZE_MIN_INDEX = IndexPrioritizingMode.PRIORITIZE_MIN_INDEX;
    public static final IndexPrioritizingMode PRIORITIZE_MAX_INDEX = IndexPrioritizingMode.PRIORITIZE_MAX_INDEX;

    private final IndexPrioritizingMode indexPrioritizingMode;
    private final int n;
    private final int[] a;
    private final int[] deque;
    private int head, tail;
    private int l, r;
    public IntSlidingWindowMinimum(int n, IntUnaryOperator generator, IndexPrioritizingMode indexPrioritizingMode) {
        this.n = n;
        this.a = new int[n];
        Arrays.setAll(a, generator);
        this.deque = new int[n];
        this.indexPrioritizingMode = indexPrioritizingMode;
    }
    public IntSlidingWindowMinimum(int[] a, IndexPrioritizingMode indexPrioritizingMode) {
        this(a.length, i -> a[i], indexPrioritizingMode);
    }
    public void proceedRight() {
        if (r == n) throw new NoSuchElementException("no more elements.");
        int v = a[r];
        switch (indexPrioritizingMode) {
            case PRIORITIZE_MIN_INDEX:
                while (head < tail && a[deque[tail - 1]] >  v) --tail;
                break;
            case PRIORITIZE_MAX_INDEX:
                while (head < tail && a[deque[tail - 1]] >= v) --tail;
                break;
        }
        deque[tail++] = r++;
    }
    public void proceedRight(int toIndex) {
        if (toIndex < r) throw new AssertionError("cannot proceed backward!");
        while (r < toIndex) proceedRight();
    }
    public void proceedLeft() {
        if (l == r) throw new NoSuchElementException("cannot remove an element from the empty window.");
        if (head < tail && deque[head] == l) head++;
        l++;
    }
    public void proceedLeft(int toIndex) {
        if (toIndex < l) throw new AssertionError("cannot proceed backward!");
        while (l < toIndex) proceedLeft();
    }
    public void proceed(int l, int r) { proceedRight(r); proceedLeft(l); }
    public int argmin() {
        if (l == r) throw new NoSuchElementException("empty window.");
        return deque[head];
    }
    public int min() { return a[argmin()]; }
}
