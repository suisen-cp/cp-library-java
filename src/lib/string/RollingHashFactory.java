package lib.string;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class RollingHashFactory {
    private static final long MOD = (1L << 61) - 1;
    private static final long MASK_31 = (1L << 31) - 1;
    private static final long MASK_30 = (1L << 30) - 1;
    private static final long MIN = 6000;
    private static final long MAX = MOD - 1;
    private final long base1;
    private final long base2;
    private final long[] pow1;
    private final long[] pow2;
    public RollingHashFactory(int maxlen) {
        this.pow1 = new long[maxlen + 1];
        this.pow2 = new long[maxlen + 1];
        this.base1 = generateBase(MIN, MAX);
        this.base2 = generateBase(MIN, MAX);
        pow1[0] = pow2[0] = 1;
        for (int i = 1; i <= maxlen; i++) {
            pow1[i] = mod(mul(pow1[i - 1], base1));
            pow2[i] = mod(mul(pow2[i - 1], base2));
        }
    }
    public RollingHash create(char[] s) {
        return new RollingHash(s);
    }
    public RollingHash create(String s) {
        return new RollingHash(s.toCharArray());
    }
    public final class RollingHash {
        private final long[] hash1;
        private final long[] hash2;
        private RollingHash(char[] s) {
            this.hash1 = new long[s.length + 1];
            this.hash2 = new long[s.length + 1];
            for (int i = 0; i < s.length; i++) {
                hash1[i + 1] = mod(mul(hash1[i], base1) + s[i] + 1);
                hash2[i + 1] = mod(mul(hash2[i], base2) + s[i] + 1);
            }
        }
        public long hash1(int l, int r) {
            long ret = hash1[r] - mod(mul(hash1[l], pow1[r - l]));
            return ret < 0 ? ret + MOD : ret;
        }
        public long hash2(int l, int r) {
            long ret = hash2[r] - mod(mul(hash2[l], pow2[r - l]));
            return ret < 0 ? ret + MOD : ret;
        }
    }
    public boolean isEqualSubstring(RollingHash rh1, int l1, int r1, RollingHash rh2, int l2, int r2) {
        long h11 = rh1.hash1(l1, r1);
        long h12 = rh1.hash2(l1, r1);
        long h21 = rh2.hash1(l2, r2);
        long h22 = rh2.hash2(l2, r2);
        return h11 == h21 && h12 == h22;
    }
    private static long mul(long a, long b) {
        long ah = a >> 31, al = a & MASK_31;
        long bh = b >> 31, bl = b & MASK_31;
        long m = ah * bl + al * bh;
        long mu = m >> 30, md = m & MASK_30;
        return ((ah * bh) << 1) + mu + (md << 31) + al * bl;
    }
    private static long mod(long l) {
        l = (l & MOD) + (l >> 61);
        while (l > MOD) l -= MOD;
        return l;
    }
    private static long generateBase(@SuppressWarnings("SameParameterValue") long lowerBound, @SuppressWarnings("SameParameterValue") long upperBound) {
        java.util.Random r = new java.util.Random(System.nanoTime());
        long ret;
        do {
            ret = r.nextLong();
        } while (ret >= upperBound || ret < lowerBound);
        return ret;
    }
}
