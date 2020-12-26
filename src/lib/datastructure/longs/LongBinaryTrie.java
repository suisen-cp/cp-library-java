package lib.datastructure.longs;

public class LongBinaryTrie {
    private static final class Node {
        int size;
        Node l, r;
    }
    private final int W;
    private final Node root;
    public LongBinaryTrie(int wordSize) {
        if (wordSize < 0 || wordSize >= Long.SIZE) throw new AssertionError();
        this.W = wordSize;
        this.root = new Node();
    }
    public void add(long x) {
        Node t = root;
        t.size++;
        for (int i = W - 1; i >= 0; i--) {
            if (((x >> i) & 1) == 0) {
                if (t.l == null) t.l = new Node();
                t = t.l;
            } else {
                if (t.r == null) t.r = new Node();
                t = t.r;
            }
            t.size++;
        }
    }
    public void remove(long x) {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        t.size--;
        for (int i = W - 1; i >= 0; i--) {
            t = ((x >> i) & 1) == 0 ? t.l : t.r;
            if (sizeOf(t) == 0) throw new AssertionError();
            t.size--;
        }
    }
    public boolean contains(long x) {
        Node t = root;
        if (t.size == 0) return false;
        for (int i = W - 1; i >= 0; i--) {
            t = ((x >> i) & 1) == 0 ? t.l : t.r;
            if (sizeOf(t) == 0) return false;
        }
        return true;
    }
    public long minXor(long xor) {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        long min = 0;
        for (int i = W - 1; i >= 0; i--) {
            if (((xor >>> i) & 1) == 0) {
                if (sizeOf(t.l) > 0) {
                    t = t.l;
                } else {
                    min |= 1L << i;
                    t = t.r;
                }
            } else {
                if (sizeOf(t.r) > 0) {
                    min |= 1L << i;
                    t = t.r;
                } else {
                    t = t.l;
                }
            }
        }
        return min;
    }
    public long max() {
        return minXor(~0L);
    }
    public long min() {
        return minXor(0L);
    }
    public int countLowerXor(long v, long xor) {
        Node t = root;
        int cnt = 0;
        for (int i = W - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return cnt;
            int b = (int) ((v >>> i) & 1);
            if (((xor >>> i) & 1) == 0) {
                if (b == 1) {
                    cnt += sizeOf(t.l);
                    t = t.r;
                } else {
                    t = t.l;
                }
            } else {
                if (b == 0) {
                    cnt += sizeOf(t.r);
                    t = t.l;
                } else {
                    t = t.r;
                }
            }
        }
        return cnt;
    }
    public int countLower(long v) {
        return countLowerXor(v, 0L);
    }
    public int countHigher(long v) {
        return countLowerXor(v, ~0L);
    }
    public int count(long v) {
        Node t = root;
        for (int i = W - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return 0;
            t = ((v >>> i) & 1) == 0 ? t.l : t.r;
        }
        return sizeOf(t);
    }
    public long kthElementXor(int k, long xor) {
        if (k < 0 || k >= size()) throw new AssertionError();
        Node t = root;
        long res = 0;
        for (int i = W - 1; i >= 0; i--) {
            if (((xor >>> i) & 1) == 0) {
                if (sizeOf(t.l) > k) {
                    t = t.l;
                } else {
                    res |= 1L << i;
                    k -= sizeOf(t.l);
                    t = t.r;
                }
            } else {
                if (sizeOf(t.r) > k) {
                    res |= 1L << i;
                    t = t.r;
                } else {
                    k -= sizeOf(t.r);
                    t = t.l;
                }
            }
        }
        return res;
    }
    public long kthElement(int k) {
        return kthElementXor(k, 0);
    }
    public long lowerElementXor(long v, long xor) {
        return kthElementXor(countLowerXor(v, xor) - 1, xor);
    }
    public long lowerElement(long v) {
        return lowerElementXor(v, 0L);
    }
    public long higherElement(long v) {
        return lowerElementXor(v, ~0L);
    }
    public long floorElement(long v) {
        return kthElement(size() - countHigher(v) - 1);
    }
    public long ceilElement(long v) {
        return kthElement(countLower(v));
    }
    public int size() {
        return root.size;
    }
    private int sizeOf(Node t) {
        return t == null ? 0 : t.size;
    }
}
