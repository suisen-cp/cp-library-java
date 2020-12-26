package lib.datastructure;

public class BitVector {
    final byte[] Data;
    final int[] LargeBlocks;
    final byte[] SmallBlocks;
    BitVector(byte[] Data, int[] LargeBlocks, byte[] SmallBlocks) {
        this.Data = Data;
        this.LargeBlocks = LargeBlocks;
        this.SmallBlocks = SmallBlocks;
    }
    public int rank1(int n) {
        if (n == 0) return 0;
        int c = (n - 1) >> 8;
        int b = (n - 1) >> 3;
        int r = (n - 1 & 0b0111) + 1;
        int x = popcount((byte) (Data[b] & ((1 << r) - 1)));
        return LargeBlocks[c] + (0xff & SmallBlocks[b]) + x;
    }
    public int rank0(int n) {
        return n - rank1(n);
    }
    public int select1(int n) {
        if (n == 0) return 0;
        int max = Data.length << 3;
        if (rank1(max) < n) return -1;
        int l = 0, r = max;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (rank1(m) < n) l = m;
            else r = m;
        }
        return r;
    }
    public int select0(int n) {
        if (n == 0) return 0;
        int max = Data.length << 3;
        if (rank0(max) < n) return -1;
        int l = 0, r = max;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (rank0(m) < n) l = m;
            else r = m;
        }
        return r;
    }
    public int get(int n) {
        int q = n >> 3;
        int r = n & 0b0111;
        return (Data[q] >> r) & 1;
    }
    public boolean getAsBool(int n) {
        return get(n) == 1;
    }
    static int popcount(byte b) {
        int x = b & 0xff;
        x = (x & 0x55) + ((x & 0xaa) >>> 1);
        x = (x & 0x33) + ((x & 0xcc) >>> 2);
        x = (x & 0x0f) + ((x & 0xf0) >>> 4);
        return x;
    }
}