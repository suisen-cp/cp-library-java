package lib.datastructure.ints;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntBinaryIndexedTree {
    private final int n;
    private final int[] dat;

    public IntBinaryIndexedTree(int n){
        this.n = n;
        this.dat = new int[n];
    }

    public IntBinaryIndexedTree(int[] dat) {
        this(dat.length);
        build(dat);
    }

    public int get(int p) {
        return sum(p + 1) - sum(p);
    }

    public void set(int p, int v) {
        add(p, v - get(p));
    }

    public void add(int p, int x){
        p++;
        while (p <= n) {
            dat[p - 1] += x;
            p += -p & p;
        }
    }
    public int sum(int l, int r){
        return sum(r) - sum(l);
    }

    private int sum(int r){
        int s = 0;
        while (r > 0) {
            s += dat[r - 1];
            r -= -r & r;
        }
        return s;
    }

    private void build(int[] dat) {
        System.arraycopy(dat, 0, this.dat, 0, n);
        for (int i = 1; i <= n; i++) {
            int p = i + (-i & i);
            if (p <= n) {
                this.dat[p - 1] += this.dat[i - 1];
            }
        }
    }

    @Override
    public String toString() {
        int msb = 31 - Integer.numberOfLeadingZeros(n);
        char[][] c = new char[msb + 1][1 << (2 * msb + 1)];
        int[] idx = new int[msb + 1];
        for (int i = 1; i <= n; i++) {
            int j = Integer.numberOfTrailingZeros(i);
            char[] ic = Integer.toString(dat[i]).toCharArray();
            c[j][idx[j]] = '['; c[j][idx[j] + (1 << (j + 2)) - 1] = ']';
            int st = idx[j] + (1 << (j + 1)) - ic.length / 2 - 1;
            System.arraycopy(ic, 0, c[j], st + 0, ic.length);
            idx[j] += 1 << (j + 3);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = msb; i >= 0; i--) sb.append(c[i]).append('\n');
        return sb.toString();
    }
}
