package lib.util.collections.longs;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalLong;

import lib.util.function.LongComparator;

public final class LongPriorityDeque {
    static final int DEFAULT_CAPACITY = 1 << 6;

    long[] que;

    final LongComparator comparator;

    int size = 0;

    public LongPriorityDeque(int capacity) {
        int k = 1;
        while (k < capacity) k <<= 1;
        this.que = new long[k];
        this.comparator = null;
    }

    public LongPriorityDeque() {
        this(DEFAULT_CAPACITY);
    }

    public LongPriorityDeque(int capacity, LongComparator comparator) {
        throw new UnsupportedOperationException("Sorry, not implemented yet.");
        // int k = 1;
        // while (k < capacity) k <<= 1;
        // this.que = new long[k];
        // this.comparator = comparator;
    }

    public LongPriorityDeque(LongComparator comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    private int left(int p) { return p << 1 | p & 1; }

    private int right(int p) { return (p + 1) << 1 | p & 1; }

    private int parent(int x) {
        int y = x >> 1;
        return y - ((x ^ y) & 1);
    }

    private boolean isLeaf(int x) { return left(x) > size; }

    private int capacity() { return que.length; }

    private void ensureCapacity(int requiringCapacity) {
        if (requiringCapacity > capacity()) {
            que = Arrays.copyOf(que, Math.max(requiringCapacity, capacity() << 1));
        }
    }

    private void makeDescendingHeapBottomUp(int bottom, long v) {
        int i = bottom;
        while (i > 2) {
            int p = parent(i);
            if (que[p] >= v) break;
            que[i] = que[i = p];
        }
        que[i] = v;
    }

    private void makeAscendingHeapBottomUp(int bottom, long v) {
        int i = bottom;
        while (i > 2) {
            int p = parent(i);
            if (que[p] <= v) break;
            que[i] = que[i = p];
        }
        que[i] = v;
    }

    public void add(long v) {
        int i = ++size;
        ensureCapacity(size + 1);
        if (i == 1) { que[i] = v; return; }
        if ((i & 1) == 0) {
            if (v < que[i - 1]) {
                que[i] = que[i = i - 1];
                makeAscendingHeapBottomUp(i, v);
            } else {
                makeDescendingHeapBottomUp(i, v);
            }
        } else {
            int p = parent(i);
            if (que[p + 1] < v) {
                que[i] = que[i = p + 1];
                makeDescendingHeapBottomUp(i, v);
            } else {
                makeAscendingHeapBottomUp(i, v);
            }
        }
    }

    private int makeDescendingHeapTopDown(long v) {
        int i = 2;
        while (!isLeaf(i)) {
            int l = left(i), r = right(i);
            if (r <= size) {
                if (que[l] < que[r]) {
                    if (v >= que[r]) break;
                    que[i] = que[i = r];
                } else {
                    if (v >= que[l]) break;
                    que[i] = que[i = l];
                }
            } else {
                if (v >= que[l]) break;
                que[i] = que[i = l];
            }
        }
        que[i] = v;
        return i;
    }

    private int makeAscendingHeapTopDown(long v) {
        int i = 1;
        while (!isLeaf(i)) {
            int l = left(i), r = right(i);
            if (r <= size) {
                if (que[l] > que[r]) {
                    if (v <= que[r]) break;
                    que[i] = que[i = r];
                } else {
                    if (v <= que[l]) break;
                    que[i] = que[i = l];
                }
            } else {
                if (v <= que[l]) break;
                que[i] = que[i = l];
            }
        }
        que[i] = v;
        return i;
    }

    public long removeMin() {
        if (size == 0) throw new NoSuchElementException();
        long min = que[1];
        int i = makeAscendingHeapTopDown(que[size--]);
        if (isLeaf(i) && i + 1 <= size && que[i] > que[i + 1]) {
            long v = que[i];
            que[i] = que[i + 1];
            makeDescendingHeapBottomUp(i + 1, v);
        }
        return min;
    }

    public long removeMax() {
        if (size <= 1) return removeMin();
        long max = que[2];
        int i = makeDescendingHeapTopDown(que[size--]);
        if (isLeaf(i) && que[i - 1] > que[i]) {
            long v = que[i];
            que[i] = que[i - 1];
            makeAscendingHeapBottomUp(i - 1, v);
        }
        return max;
    }

    public long getMin() {
        if (size == 0) throw new NoSuchElementException();
        return que[1];
    }

    public long getMax() {
        return size <= 1 ? getMin() : que[2];
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void clear() { size = 0; }

    public OptionalLong pollMin() { return size > 0 ? OptionalLong.of(removeMin()) : OptionalLong.empty(); }

    public OptionalLong pollMax() { return size > 0 ? OptionalLong.of(removeMax()) : OptionalLong.empty(); }

    public OptionalLong peekMin() { return size > 0 ? OptionalLong.of(getMin()) : OptionalLong.empty(); }

    public OptionalLong peekMax() { return size > 0 ? OptionalLong.of(getMax()) : OptionalLong.empty(); }
}
