package lib.util.collections.longs;

import java.util.NoSuchElementException;
import java.util.OptionalLong;

import lib.util.function.LongComparator;

/**
 * @author https://atcoder.jp/users/suisen
 */
@SuppressWarnings("PointlessBitwiseExpression")
public class LongPriorityQueue {
    static final int DEFAULT_CAPACITY = 1 << 6;

    long[] que;

    final LongComparator comparator;

    final boolean descending;

    int size = 0;

    public LongPriorityQueue(int capacity, boolean descending) {
        int k = 1;
        while (k < capacity) k <<= 1;
        this.que = new long[k];
        this.comparator = null;
        this.descending = descending;
        this.size = 0;
    }

    public LongPriorityQueue(int capacity) {
        this(capacity, false);
    }

    public LongPriorityQueue(boolean descending) {
        this(DEFAULT_CAPACITY, descending);
    }

    public LongPriorityQueue() {
        this(DEFAULT_CAPACITY, false);
    }

    public LongPriorityQueue(int capacity, LongComparator comparator) {
        int k = 1;
        while (k < capacity) k <<= 1;
        this.que = new long[k];
        this.comparator = comparator;
        this.descending = false;
        this.size = 0;
    }

    public LongPriorityQueue(LongComparator comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public void add(long e) {
        if (++size == que.length) grow();
        if (comparator != null) {
            addUsingComparator(e);
        } else if (descending) {
            addDescending(e);
        } else {
            addAscending(e);
        }
    }

    void grow() {
        long[] newQue = new long[que.length << 1];
        System.arraycopy(que, 0, newQue, 0, que.length);
        que = newQue;
    }

    void addUsingComparator(long e) {
        int i = size;
        while (i > 1) {
            int p = i >> 1;
            if (comparator.compare(e, que[p]) >= 0) break;
            que[i] = que[i = p];
        }
        que[i] = e;
    }

    void addDescending(long e) {
        int i = size;
        while (i > 1) {
            int p = i >> 1;
            if (e <= que[p]) break;
            que[i] = que[i = p];
        }
        que[i] = e;
    }

    void addAscending(long e) {
        int i = size;
        while (i > 1) {
            int p = i >> 1;
            if (e >= que[p]) break;
            que[i] = que[i = p];
        }
        que[i] = e;
    }

    public OptionalLong poll() {
        if (size == 0) return OptionalLong.empty();
        if (comparator != null) {
            return OptionalLong.of(pollUsingComparator());
        } else if (descending) {
            return OptionalLong.of(pollDescending());
        } else {
            return OptionalLong.of(pollAscending());
        }
    }

    public long removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        if (comparator != null) {
            return pollUsingComparator();
        } else if (descending) {
            return pollDescending();
        } else {
            return pollAscending();
        }
    }

    long pollUsingComparator() {
        long ret = que[1];
        long e = que[size--];
        int i = 1;
        int h = size >> 1;
        while (i <= h) {
            int l = i << 1 | 0, r = i << 1 | 1;
            if (r <= size) {
                if (comparator.compare(que[l], que[r]) > 0) {
                    if (comparator.compare(e, que[r]) <= 0) break;
                    que[i] = que[i = r];
                } else {
                    if (comparator.compare(e, que[l]) <= 0) break;
                    que[i] = que[i = l];
                }
            } else {
                if (comparator.compare(e, que[l]) <= 0) break;
                que[i] = que[i = l];
            }
        }
        que[i] = e;
        return ret;
    }

    long pollDescending() {
        long ret = que[1];
        long e = que[size--];
        int i = 1;
        int h = size >> 1;
        while (i <= h) {
            int l = i << 1 | 0, r = i << 1 | 1;
            if (r <= size) {
                if (que[l] < que[r]) {
                    if (e >= que[r]) break;
                    que[i] = que[i = r];
                } else {
                    if (e >= que[l]) break;
                    que[i] = que[i = l];
                }
            } else {
                if (e >= que[l]) break;
                que[i] = que[i = l];
            }
        }
        que[i] = e;
        return ret;
    }

    long pollAscending() {
        long ret = que[1];
        long e = que[size--];
        int i = 1;
        int h = size >> 1;
        while (i <= h) {
            int l = i << 1 | 0, r = i << 1 | 1;
            if (r <= size) {
                if (que[l] > que[r]) {
                    if (e <= que[r]) break;
                    que[i] = que[i = r];
                } else {
                    if (e <= que[l]) break;
                    que[i] = que[i = l];
                }
            } else {
                if (e <= que[l]) break;
                que[i] = que[i = l];
            }
        }
        que[i] = e;
        return ret;
    }

    public OptionalLong peek() {
        return size == 0 ? OptionalLong.empty() : OptionalLong.of(que[1]);
    }

    public long getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return que[1];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }

    @Override
    public String toString() {
        return toString(1, 0);
    }

    private String toString(int k, int space) {
        String s = "";
        if ((k << 1 | 1) <= size) s += toString(k << 1 | 1, space + 3) + "\n";
        s += " ".repeat(space) + que[k];
        if ((k << 1 | 0) <= size) s += "\n" + toString(k << 1 | 0, space + 3);
        return s;
    }
}