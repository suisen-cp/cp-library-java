package lib.datastructure.ints;

public class IntBinaryTrie2 {
    private final class Node {
        int size;
        Node[] ch = new Node[1 << LOG];
    }

    private final int DEP;
    private final int LOG;
    private final int MASK;
    private final Node root;

    public IntBinaryTrie2(int log) {
        this.LOG = log;
        this.DEP = (Integer.SIZE + LOG - 1) / LOG;
        this.MASK = (1 << LOG) - 1;
        this.root = new Node();
    }
    public void add(int x) {
        Node t = root;
        t.size++;
        for (int i = DEP - 1; i >= 0; i--) {
            int b = (x >>> i * LOG) & MASK;
            if (t.ch[b] == null) t.ch[b] = new Node();
            t = t.ch[b];
            t.size++;
        }
    }
    public void remove(int x) {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        t.size--;
        for (int i = DEP - 1; i >= 0; i--) {
            int b = (x >>> i * LOG) & MASK;
            t = t.ch[b];
            if (sizeOf(t) == 0) throw new AssertionError();
            t.size--;
        }
    }
    public boolean contains(int x) {
        Node t = root;
        if (t.size == 0) return false;
        for (int i = DEP - 1; i >= 0; i--) {
            int b = (x >>> i * LOG) & MASK;
            t = t.ch[b];
            if (sizeOf(t) == 0) return false;
        }
        return true;
    }
    public int min() {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        int min = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            for (int j = 0; j <= MASK; j++) {
                if (sizeOf(t.ch[j]) > 0) {
                    min |= j << i * LOG;
                    t = t.ch[j];
                    break;
                }
            }
        }
        return min;
    }
    public int max() {
        Node t = root;
        if (t.size == 0) throw new AssertionError();
        int max = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            for (int j = MASK; j >= 0; j--) {
                if (sizeOf(t.ch[j]) > 0) {
                    max |= j << i * LOG;
                    t = t.ch[j];
                    break;
                }
            }
        }
        return max;
    }
    public int countLower(int v) {
        Node t = root;
        int cnt = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return cnt;
            int b = (v >>> i * LOG) & MASK;
            for (int j = 0; j < b; j++) {
                cnt += sizeOf(t.ch[j]);
            }
            t = t.ch[b];
        }
        return cnt;
    }
    public int countFloor(int v) {
        Node t = root;
        int cnt = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return cnt;
            int b = (v >>> i * LOG) & MASK;
            for (int j = 0; j < b; j++) {
                cnt += sizeOf(t.ch[j]);
            }
            t = t.ch[b];
        }
        return cnt + sizeOf(t);
    }
    public int countHigher(int v) {
        Node t = root;
        int cnt = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return cnt;
            int b = (v >>> i * LOG) & MASK;
            for (int j = MASK; j > b; j--) {
                cnt += sizeOf(t.ch[j]);
            }
            t = t.ch[b];
        }
        return cnt;
    }
    public int countCeil(int v) {
        Node t = root;
        int cnt = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return cnt;
            int b = (v >>> i * LOG) & MASK;
            for (int j = MASK; j > b; j--) {
                cnt += sizeOf(t.ch[j]);
            }
            t = t.ch[b];
        }
        return cnt + sizeOf(t);
    }
    public int count(int v) {
        Node t = root;
        for (int i = DEP - 1; i >= 0; i--) {
            if (sizeOf(t) == 0) return 0;
            int b = (v >>> i * LOG) & MASK;
            t = t.ch[b];
        }
        return sizeOf(t);
    }
    public int kthElement(int k) {
        if (k < 0 || k >= size()) throw new AssertionError();
        Node t = root;
        int res = 0;
        for (int i = DEP - 1; i >= 0; i--) {
            for (int j = 0; j <= MASK; j++) {
                int siz = sizeOf(t.ch[j]);
                if (siz > k) {
                    res |= j << i * LOG;
                    t = t.ch[j];
                    break;
                } else {
                    k -= siz;
                }
            }
        }
        return res;
    }
    public int lowerElement(int v) {
        return kthElement(countLower(v) - 1);
    }
    public int higherElement(int v) {
        return kthElement(countFloor(v));
    }
    public int floorElement(int v) {
        return kthElement(countFloor(v) - 1);
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
