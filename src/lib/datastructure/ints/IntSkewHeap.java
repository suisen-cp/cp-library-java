package lib.datastructure.ints;

public class IntSkewHeap {
    private Heap root;
    public void meld(Heap other) {root = meld(root, other);}
    private Heap meld(Heap a, Heap b) {
        if (a == null) return b;
        if (b == null) return a;
        if (a.val > b.val) return meld(b, a);
        a.r = meld(a.r, b);
        Heap t = a.l; a.l = a.r; a.r = t;
        a.recalcSize();
        return a;
    }
    public void push(int idx, int val) {root = meld(root, new Heap(idx, val));}
    public Heap top() {return root;}
    public Heap pop() {
        Heap top = root;
        root = meld(root.l, root.r);
        return top;
    }
    public boolean isEmpty() {return root == null;}
    public int size() {return root == null ? 0 : root.size;}
    public static class Heap {
        private int size = 1;
        private Heap l, r;
        public final int idx, val;
        private Heap(int idx, int val) {this.idx = idx; this.val = val;}
        private void recalcSize() {size = (l == null ? 0 : l.size) + (r == null ? 0 : r.size) + 1;}
    }
}