package lib.util.collections.ints;

import java.util.NoSuchElementException;
import java.util.OptionalInt;

import lib.util.function.IntComparator;

/**
 * @author https://atcoder.jp/users/suisen
 */
@SuppressWarnings("PointlessBitwiseExpression")
public class IntPriorityQueue {
    static final int DEFAULT_CAPACITY = 1 << 6;

    int[] que;

    final IntComparator comparator;

    final boolean descending;

    int size = 0;

    public IntPriorityQueue(int capacity, boolean descending) {
        int k = 1;
        while (k < capacity) k <<= 1;
        this.que = new int[k];
        this.comparator = null;
        this.descending = descending;
        this.size = 0;
    }

    public IntPriorityQueue(int capacity) {
        this(capacity, false);
    }

    public IntPriorityQueue(boolean descending) {
        this(DEFAULT_CAPACITY, descending);
    }

    public IntPriorityQueue() {
        this(DEFAULT_CAPACITY, false);
    }

    public IntPriorityQueue(int capacity, IntComparator comparator) {
        int k = 1;
        while (k < capacity) k <<= 1;
        this.que = new int[k];
        this.comparator = comparator;
        this.descending = false;
        this.size = 0;
    }

    public IntPriorityQueue(IntComparator comparator) {
        this(DEFAULT_CAPACITY, comparator);
    }

    public void add(int e) {
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
        int[] newQue = new int[que.length << 1];
        System.arraycopy(que, 0, newQue, 0, que.length);
        que = newQue;
    }

    void addUsingComparator(int e) {
        int i = size;
        while (i > 1) {
            int p = i >> 1;
            if (comparator.compare(e, que[p]) >= 0) break;
            que[i] = que[i = p];
        }
        que[i] = e;
    }

    void addDescending(int e) {
        int i = size;
        while (i > 1) {
            int p = i >> 1;
            if (e <= que[p]) break;
            que[i] = que[i = p];
        }
        que[i] = e;
    }

    void addAscending(int e) {
        int i = size;
        while (i > 1) {
            int p = i >> 1;
            if (e >= que[p]) break;
            que[i] = que[i = p];
        }
        que[i] = e;
    }

    public OptionalInt poll() {
        if (size == 0) return OptionalInt.empty();
        if (comparator != null) {
            return OptionalInt.of(pollUsingComparator());
        } else if (descending) {
            return OptionalInt.of(pollDescending());
        } else {
            return OptionalInt.of(pollAscending());
        }
    }

    public int removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        if (comparator != null) {
            return pollUsingComparator();
        } else if (descending) {
            return pollDescending();
        } else {
            return pollAscending();
        }
    }

    int pollUsingComparator() {
        int ret = que[1];
        int e = que[size--];
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

    int pollDescending() {
        int ret = que[1];
        int e = que[size--];
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

    int pollAscending() {
        int ret = que[1];
        int e = que[size--];
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

    public OptionalInt peek() {
        return size == 0 ? OptionalInt.empty() : OptionalInt.of(que[1]);
    }

    public int getFirst() {
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