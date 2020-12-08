package lib.datastructure.ints;

public class IntBinaryTrie {
    private static final class Node {
        int size;
        Node l, r;
    }
    private final int W;
    private final Node root;
    public IntBinaryTrie(int wordSize) {
        if (wordSize < 0 || wordSize >= Integer.SIZE) throw new AssertionError();
        this.W = wordSize;
        this.root = new Node();
    }
    public void add(int x) {
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
    public void remove(int x) {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        t.size--;
        for (int i = W - 1; i >= 0; i--) {
            t = ((x >> i) & 1) == 0 ? t.l : t.r;
            if (sizeOf(t) == 0) throw new AssertionError();
            t.size--;
        }
    }
    public boolean contains(int x) {
        Node t = root;
        if (t.size == 0) return false;
        for (int i = W - 1; i >= 0; i--) {
            t = ((x >> i) & 1) == 0 ? t.l : t.r;
            if (sizeOf(t) == 0) return false;
        }
        return true;
    }
    public int minXor(int xor) {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        int min = 0;
        for (int i = W - 1; i >= 0; i--) {
            if (((xor >>> i) & 1) == 0) {
                if (sizeOf(t.l) > 0) {
                    t = t.l;
                } else {
                    min |= 1 << i;
                    t = t.r;
                }
            } else {
                if (sizeOf(t.r) > 0) {
                    min |= 1 << i;
                    t = t.r;
                } else {
                    t = t.l;
                }
            }
        }
        return min;
    }
    public int max() {
        return minXor(~0);
    }
    public int min() {
        return minXor(0);
    }
    public int countLowerXor(int v, int xor) {
        Node t = root;
        int cnt = 0;
        for (int i = W - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return cnt;
            int b = (v >>> i) & 1;
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
    public int countLower(int v) {
        return countLowerXor(v, 0);
    }
    public int countHigher(int v) {
        return countLowerXor(v, ~0);
    }
    public int count(int v) {
        Node t = root;
        for (int i = W - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return 0;
            t = ((v >>> i) & 1) == 0 ? t.l : t.r;
        }
        return sizeOf(t);
    }
    public int kthElementXor(int k, int xor) {
        if (k < 0 || k >= size()) throw new AssertionError();
        Node t = root;
        int res = 0;
        for (int i = W - 1; i >= 0; i--) {
            if (((xor >>> i) & 1) == 0) {
                if (sizeOf(t.l) > k) {
                    t = t.l;
                } else {
                    res |= 1 << i;
                    k -= sizeOf(t.l);
                    t = t.r;
                }
            } else {
                if (sizeOf(t.r) > k) {
                    res |= 1 << i;
                    t = t.r;
                } else {
                    k -= sizeOf(t.r);
                    t = t.l;
                }
            }
        }
        return res;
    }
    public int kthElement(int k) {
        return kthElementXor(k, 0);
    }
    public int lowerElementXor(int v, int xor) {
        return kthElementXor(countLowerXor(v, xor) - 1, xor);
    }
    public int lowerElement(int v) {
        return lowerElementXor(v, 0);
    }
    public int higherElement(int v) {
        return lowerElementXor(v, ~0);
    }
    public int floorElement(int v) {
        return kthElement(size() - countHigher(v) - 1);
    }
    public int ceilElement(int v) {
        return kthElement(countLower(v));
    }
    public int size() {
        return root.size;
    }
    private int sizeOf(Node t) {
        return t == null ? 0 : t.size;
    }
}
