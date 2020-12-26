package lib.util;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class BitUtil {
    private static final int[] INT_MASK = {
        0x0000_0000,
        0x0000_0001, 0x0000_0003, 0x0000_0007, 0x0000_000f,
        0x0000_001f, 0x0000_003f, 0x0000_007f, 0x0000_00ff,
        0x0000_01ff, 0x0000_03ff, 0x0000_07ff, 0x0000_0fff,
        0x0000_1fff, 0x0000_3fff, 0x0000_7fff, 0x0000_ffff,
        0x0001_ffff, 0x0003_ffff, 0x0007_ffff, 0x000f_ffff,
        0x001f_ffff, 0x003f_ffff, 0x007f_ffff, 0x00ff_ffff,
        0x01ff_ffff, 0x03ff_ffff, 0x07ff_ffff, 0x0fff_ffff,
        0x1fff_ffff, 0x3fff_ffff, 0x7fff_ffff, 0xffff_ffff
    };
    private static final long[] LONG_MASK = {
            0x0000_0000_0000_0000L,
            0x0000_0000_0000_0001L, 0x0000_0000_0000_0003L, 0x0000_0000_0000_0007L, 0x0000_0000_0000_000fL,
            0x0000_0000_0000_001fL, 0x0000_0000_0000_003fL, 0x0000_0000_0000_007fL, 0x0000_0000_0000_00ffL,
            0x0000_0000_0000_01ffL, 0x0000_0000_0000_03ffL, 0x0000_0000_0000_07ffL, 0x0000_0000_0000_0fffL,
            0x0000_0000_0000_1fffL, 0x0000_0000_0000_3fffL, 0x0000_0000_0000_7fffL, 0x0000_0000_0000_ffffL,
            0x0000_0000_0001_ffffL, 0x0000_0000_0003_ffffL, 0x0000_0000_0007_ffffL, 0x0000_0000_000f_ffffL,
            0x0000_0000_001f_ffffL, 0x0000_0000_003f_ffffL, 0x0000_0000_007f_ffffL, 0x0000_0000_00ff_ffffL,
            0x0000_0000_01ff_ffffL, 0x0000_0000_03ff_ffffL, 0x0000_0000_07ff_ffffL, 0x0000_0000_0fff_ffffL,
            0x0000_0000_1fff_ffffL, 0x0000_0000_3fff_ffffL, 0x0000_0000_7fff_ffffL, 0x0000_0000_ffff_ffffL,
            0x0000_0001_ffff_ffffL, 0x0000_0003_ffff_ffffL, 0x0000_0007_ffff_ffffL, 0x0000_000f_ffff_ffffL,
            0x0000_001f_ffff_ffffL, 0x0000_003f_ffff_ffffL, 0x0000_007f_ffff_ffffL, 0x0000_00ff_ffff_ffffL,
            0x0000_01ff_ffff_ffffL, 0x0000_03ff_ffff_ffffL, 0x0000_07ff_ffff_ffffL, 0x0000_0fff_ffff_ffffL,
            0x0000_1fff_ffff_ffffL, 0x0000_3fff_ffff_ffffL, 0x0000_7fff_ffff_ffffL, 0x0000_ffff_ffff_ffffL,
            0x0001_ffff_ffff_ffffL, 0x0003_ffff_ffff_ffffL, 0x0007_ffff_ffff_ffffL, 0x000f_ffff_ffff_ffffL,
            0x001f_ffff_ffff_ffffL, 0x003f_ffff_ffff_ffffL, 0x007f_ffff_ffff_ffffL, 0x00ff_ffff_ffff_ffffL,
            0x01ff_ffff_ffff_ffffL, 0x03ff_ffff_ffff_ffffL, 0x07ff_ffff_ffff_ffffL, 0x0fff_ffff_ffff_ffffL,
            0x1fff_ffff_ffff_ffffL, 0x3fff_ffff_ffff_ffffL, 0x7fff_ffff_ffff_ffffL, 0xffff_ffff_ffff_ffffL
    };
    private BitUtil(){}
    public static boolean test (long n, int bit) {return (n & (1L << bit)) != 0;}
    public static boolean test (int  n, int bit) {return (n & (1  << bit)) != 0;}

    public static int     get  (long n, int bit) {return (n & (1L << bit)) == 0 ? 1 : 0;}
    public static int     get  (int  n, int bit) {return (n & (1  << bit)) == 0 ? 1 : 0;}

    public static long    set  (long n, int bit)             {return n | (1L << bit);}
    public static int     set  (int  n, int bit)             {return n | (1  << bit);}
    public static long    set  (long n, int from, int to)    {return n | (LONG_MASK[to - from] << from);}
    public static int     set  (int  n, int from, int to)    {return n | (INT_MASK [to - from] << from);}
    public static long    set  (long n, int bit,  boolean b) {return b ? n | (1L << bit) : n & ~(1L << bit);}
    public static int     set  (int  n, int bit,  boolean b) {return b ? n | (1  << bit) : n & ~(1  << bit);}
    public static long    set  (long n, int from, int to, boolean b) {return b ? n | (LONG_MASK[to - from] << from) : n & ~(LONG_MASK[to - from] << from);}
    public static int     set  (int  n, int from, int to, boolean b) {return b ? n | (INT_MASK [to - from] << from) : n & ~(INT_MASK [to - from] << from);}

    public static long    clear(long n, int bit)          {return n & ~(1L << bit);}
    public static int     clear(int  n, int bit)          {return n & ~(1  << bit);}
    public static long    clear(long n, int from, int to) {return n & ~(LONG_MASK[to - from] << from);}
    public static int     clear(int  n, int from, int to) {return n & ~(INT_MASK [to - from] << from);}

    public static long    flip (long n, int bit)          {return n ^ (1L << bit);}
    public static int     flip (int  n, int bit)          {return n ^ (1  << bit);}
    public static long    flip (long n, int from, int to) {return n ^ (LONG_MASK[to - from] << from);}
    public static int     flip (int  n, int from, int to) {return n ^ (INT_MASK [to - from] << from);}

    public static long    mask (long n, int bit)          {return n & (1L << bit);}
    public static int     mask (int  n, int bit)          {return n & (1  << bit);}
    public static long    mask (long n, int from, int to) {return n & (LONG_MASK[to - from] << from);}
    public static int     mask (int  n, int from, int to) {return n & (INT_MASK [to - from] << from);}

    public static int     bsr  (long n) {return 63 - Long   .numberOfLeadingZeros(n);}
    public static int     bsr  (int  n) {return 31 - Integer.numberOfLeadingZeros(n);}

    public static int[] toArray(int length, int bits) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = (bits >> i) & 1;
        }
        return res;
    }

    public static int[] toArray(int length, long bits) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = (int) ((bits >> i) & 1);
        }
        return res;
    }

    public static int[][] bits(int length) {
        if (length >= 32) throw new AssertionError();
        int n = length;
        int[][] bits = new int[1 << n][];
        bits[0] = new int[0];
        for (int i = 1; i < 1 << n; i++) {
            int bsr = bsr(i);
            int[] pre = bits[i ^ (1 << bsr)];
            bits[i] = java.util.Arrays.copyOf(pre, pre.length + 1);
            bits[i][pre.length] = bsr;
        }
        return bits;
    }
}