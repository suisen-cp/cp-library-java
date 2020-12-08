package lib.math.number;

public final class ConvolutionAnyMod extends Convolution {
    private static final long MOD0 = 754974721;
    private static final long MOD1 = 167772161;
    private static final long MOD2 = 469762049;

    private static final ModArithmetic MA0 = new ModArithmetic(){
        public long getMod() {return MOD0;}
        public long mod(long a) {return (a %= MOD0) < 0 ? a + MOD0 : a;}
        public long add(long a, long b) {return (a += b) >= MOD0 ? a - MOD0 : a;}
        public long sub(long a, long b) {return (a -= b) < 0 ? a + MOD0 : a;}
        public long mul(long a, long b) {return (a * b) % MOD0;}
    };
    private static final ModArithmetic MA1 = new ModArithmetic(){
        public long getMod() {return MOD1;}
        public long mod(long a) {return (a %= MOD1) < 0 ? a + MOD1 : a;}
        public long add(long a, long b) {return (a += b) >= MOD1 ? a - MOD1 : a;}
        public long sub(long a, long b) {return (a -= b) < 0 ? a + MOD1 : a;}
        public long mul(long a, long b) {return (a * b) % MOD1;}
    };
    private static final ModArithmetic MA2 = new ModArithmetic(){
        public long getMod() {return MOD2;}
        public long mod(long a) {return (a %= MOD2) < 0 ? a + MOD2 : a;}
        public long add(long a, long b) {return (a += b) >= MOD2 ? a - MOD2 : a;}
        public long sub(long a, long b) {return (a -= b) < 0 ? a + MOD2 : a;}
        public long mul(long a, long b) {return (a * b) % MOD2;}
    };

    private static final ConvolutionNTTPrime CNV0 = new ConvolutionNTTPrime(MA0);
    private static final ConvolutionNTTPrime CNV1 = new ConvolutionNTTPrime(MA1);
    private static final ConvolutionNTTPrime CNV2 = new ConvolutionNTTPrime(MA2);

    public ConvolutionAnyMod(ModArithmetic MA) {super(MA);}

    public long[] convolution(long[] a, long[] b, int deg) {
        int n = Math.min(a.length, deg + 1);
        int m = Math.min(b.length, deg + 1);
        if (n <= 0 || m <= 0) return new long[]{};
        if (Math.max(n, m) <= THRESHOLD_NAIVE_CONVOLUTION) {
            return convolutionNaive(a, b, n, m);
        }
        long[] c0 = CNV0.convolution(a, b, deg);
        long[] c1 = CNV1.convolution(a, b, deg);
        long[] c2 = CNV2.convolution(a, b, deg);
        int k = Math.min(c0.length, deg + 1);
        long[] ret = new long[k];
        for (int i = 0; i < k; i++) {
            ret[i] = garner(c0[i], c1[i], c2[i]);
        }
        return ret;
    }

    private static final long INV1 = MA1.inv(MOD0);
    private static final long INV2 = MA2.inv(MA2.mul(MOD0, MOD1));

    private long garner(long c0, long c1, long c2) {
        long v = MA1.mul(MA1.sub(c1, c0), INV1);
        long u = MA2.mul(MA2.mod(c2 - c0 - MOD0 * v), INV2);
        return MA.mod(c0 + MA.mul(MOD0, v) + MA.mul(MOD0, MOD1, u));
    }
}
