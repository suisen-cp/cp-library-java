package lib.math.number;

import lib.base.ArithmeticOperations;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalLong;

@SuppressWarnings("PointlessBitwiseExpression")
public final class ModPolynomialFactory {
    public final Convolution cnv;
    public final ModArithmetic ma;

    public ModPolynomialFactory(Convolution cnv) {
        this.cnv = cnv;
        this.ma = cnv.MA;
    }

    public ModPolynomial create(long[] c, int n) {
        return new ModPolynomial(_cut(_mod(c), n));
    }

    public ModPolynomial create(long[] c) {
        return new ModPolynomial(_normalize(_mod(c)));
    }

    public ModPolynomial interpolate(long[] xs, long[] ys) {
        final long[] ZERO = new long[]{0};
        final long[] ONE = new long[]{1};
        final int N = xs.length;
        int k = 1;
        while (k < N) k <<= 1;
        long[][] seg = new long[k << 1][];
        long[][] g = new long[k << 1][];
        for (int i = 0; i < N; i++) {
            seg[k + i] = new long[]{ma.mod(-xs[i]), 1};
        }
        for (int i = N; i < k; i++) {
            seg[k + i] = ONE;
        }
        for (int i = k - 1; i > 0; i--) {
            seg[i] = _mul(seg[i << 1 | 0], seg[i << 1 | 1]);
        }
        g[1] = _polyMod(_differentiate(seg[1]), seg[1]);
        for (int i = 2; i < k + N; i++) {
            g[i] = _polyMod(g[i >> 1], seg[i]);
        }
        for (int i = 0; i < N; i++) {
            g[k + i] = new long[]{ma.div(ma.mod(ys[i]), g[k + i][0])};
        }
        for (int i = N; i < k; i++) {
            g[k + i] = ZERO;
        }
        for (int i = k - 1; i > 0; i--) {
            g[i] = _add(_mul(g[i << 1 | 0], seg[i << 1 | 1]), _mul(g[i << 1 | 1], seg[i << 1 | 0]));
        }
        return new ModPolynomial(g[1]);
    }

    private long[] _mod(long[] f) {
        int deg = f.length - 1;
        long[] fmod = new long[deg + 1];
        for (int i = 0; i <= deg; i++) {
            fmod[i] = ma.mod(f[i]);
        }
        return fmod;
    }

    private long[] _normalize(long[] f) {
        int degF = f.length - 1;
        if (f[degF] != 0) return f;
        while (degF > 0 && f[degF] == 0) {
            degF--;
        }
        return Arrays.copyOf(f, degF + 1);
    }

    private long[] _cut(long[] f, int deg) {
        deg = Math.min(f.length - 1, deg);
        while (deg > 0 && f[deg] == 0) {
            deg--;
        }
        return Arrays.copyOf(f, deg + 1);
    }

    private long[] _add(long[] f, long[] g) {
        final int degF = f.length - 1, degG = g.length - 1;
        int deg = Math.max(degF, degG);
        long[] res = Arrays.copyOf(f, deg + 1);
        for (int i = 0; i <= degG; i++) {
            res[i] = ma.add(res[i], g[i]);
        }
        return _normalize(res);
    }

    private long[] _sub(long[] f, long[] g) {
        final int degF = f.length - 1, degG = g.length - 1;
        int deg = Math.max(degF, degG);
        long[] res = Arrays.copyOf(f, deg + 1);
        for (int i = 0; i <= degG; i++) {
            res[i] = ma.sub(res[i], g[i]);
        }
        return _normalize(res);
    }

    private long[] _mul(long[] f, long[] g) {
        return _normalize(cnv.convolution(f, g));
    }

    private long[] _mul(long[] f, long[] g, int deg) {
        long[] h = cnv.convolution(f, g, deg);
        int degH = h.length - 1;
        if (degH <= deg) {
            return _normalize(h);
        } else {
            return _cut(h, deg);
        }
    }

    private long[] _muli(long[] f, long a) {
        a = ma.mod(a);
        if (a == 0) return new long[]{0};
        int deg = f.length - 1;
        long[] res = new long[deg + 1];
        for (int i = 0; i <= deg; i++) {
            res[i] = ma.mul(a, f[i]);
        }
        return _normalize(res);
    }

    private long[] _div(long[] f, long[] g, int deg) {
        return _mul(f, _inv(g, deg), deg);
    }

    private long[] _inv(long[] f, int deg) {
        long[] inv = new long[]{ma.inv(f[0])};
        for (int k = 1; k <= deg;) {
            k <<= 1;
            inv = _sub(_muli(inv, 2), _mul(_mul(inv, inv, k), f, k));
        }
        return _cut(inv, deg);
    }

    private long[] _differentiate(long[] f) {
        int deg = f.length - 1;
        if (deg == 0) return new long[]{0};
        long[] diff = new long[deg];
        for (int i = 1; i <= deg; i++) {
            diff[i - 1] = ma.mul(f[i], i);
        }
        return _normalize(diff);
    }

    private long[] _integrate(long[] f) {
        int deg = f.length - 1;
        long[] intg = new long[deg + 2];
        long[] invs = ma.rangeInv(deg + 1);
        for (int i = 0; i <= deg; i++) {
            intg[i + 1] = ma.mul(f[i], invs[i + 1]);
        }
        return _normalize(intg);
    }
    
    private long[] _log(long[] f, int deg) {
        long[] difF = _differentiate(f);
        long[] invF = _inv(f, deg);
        return _integrate(_mul(difF, invF, deg - 1));
    }
    
    private long[] _exp(long[] f, int deg) {
        long[] g = new long[]{1};
        int k = 1;
        while (k <= deg) {
            k <<= 1;
            long[] tmp = _sub(_cut(f, k), _log(g, k));
            tmp[0] = ma.add(tmp[0], 1);
            g = _mul(g, tmp, k);
        }
        return _cut(g, deg);
    }

    private long[] _pow(long[] f, long k, int deg) {
        int tlz = 0;
        while (tlz < f.length && f[tlz] == 0) {
            tlz++;
        }
        if (tlz * k > deg) {
            return new long[]{0};
        }
        long[] g = Arrays.copyOfRange(f, tlz, f.length);
        long base = g[0];
        g = _muli(g, ma.inv(base));
        long[] h = _muli(_exp(_muli(_log(g, deg), k), deg), ma.pow(base, k));
        long[] c = new long[deg + 1];
        int zeros = (int) (tlz * k);
        System.arraycopy(h, 0, c, zeros, deg + 1 - zeros);
        return _cut(c, deg);
    }

    private Optional<long[]> _sqrt(long[] f, int deg) {
        int tlz = 0;
        while (tlz < f.length && f[tlz] == 0) {
            tlz++;
        }
        if (tlz == f.length) {
            return Optional.of(new long[]{0});
        }
        OptionalLong ops = ma.sqrt(f[tlz]);
        if ((tlz & 1) == 1 || ops.isEmpty()) {
            return Optional.empty();
        }
        long sq = ops.getAsLong();
        long[] g = Arrays.copyOfRange(f, tlz, f.length);
        g = _muli(_exp(_muli(_log(_muli(g, ma.inv(g[0])), deg), ma.inv(2)), deg), sq);
        long[] sqrt = new long[tlz / 2 + g.length];
        System.arraycopy(g, 0, sqrt, tlz / 2, g.length);
        return Optional.of(_cut(sqrt, deg));
    }

    /**
     * @return f(x + c)
     */
    private long[] _translate(long[] f, long c) {
        final int degF = f.length - 1;
        c = ma.mod(c);
        long[] pow = ma.rangePower(c, degF);
        long[] fac = ma.factorial(degF);
        long[] ifac = ma.factorialInv(degF);
        long[] expc = new long[degF + 1];
        long[] g = new long[degF + 1];
        for (int i = 0; i <= degF; i++) {
            g[degF - i] = ma.mul(f[i], fac[i]);
            expc[i] = ma.mul(pow[i], ifac[i]);
        }
        long[] prd = _mul(g, expc, degF);
        long[] h = new long[degF + 1];
        for (int i = 0; i <= degF; i++) {
            h[i] = ma.mul(ifac[i], prd[degF - i]);
        }
        return _normalize(h);
    }

    private long[] naivePolyDiv(long[] f, long[] g) {
        final int degF = f.length - 1, degG = g.length - 1, K = degF - degG;
        final long head = g[degG];
        long[] a = f.clone();
        long[] q = new long[K + 1];
        for (int i = K; i >= 0; i--) {
            long div = ma.div(a[degG + i], head);
            q[i] = div;
            for (int j = 0; j <= degG; j++) {
                a[i + j] = ma.sub(a[i + j], ma.mul(div, g[j]));
            }
        }
        return _normalize(q);
    }

    private static final int THRESHOLD_NAIVE_POLY_DIV = 256;

    private long[] _polyDiv(long[] f, long[] g) {
        final int degF = f.length - 1, degG = g.length - 1;
        if (degF < degG) {
            return new long[]{0};
        }
        if (degG == 0) {
            return _muli(f, ma.inv(g[0]));
        }
        if (degG <= THRESHOLD_NAIVE_POLY_DIV) {
            return naivePolyDiv(f, g);
        }
        int deg = degF - degG;
        long[] revF = new long[degF + 1];
        for (int i = 0; i <= degF; i++) {
            revF[i] = f[degF - i];
        }
        long[] revG = new long[degG + 1];
        for (int i = 0; i <= degG; i++) {
            revG[i] = g[degG - i];
        }
        long[] revH = cnv.convolution(revF, _inv(revG, deg));
        long[] res = new long[deg + 1];
        for (int i = 0; i <= deg; i++) {
            res[deg - i] = revH[i];
        }
        return _normalize(res);
    }

    private long[] _polyMod(long[] f, long[] g) {
        if (f.length < g.length) {
            return f.clone();
        }
        return _sub(f, _mul(_polyDiv(f, g), g));
    }

    private long[] _multipointEval(long[] f, long[] xs) {
        final long[] ONE = new long[]{1};
        int m = xs.length;
        int k = 1;
        while (k < m) k <<= 1;
        long[][] seg = new long[k << 1][];
        for (int i = 0; i < m; i++) {
            seg[k + i] = new long[]{ma.mod(-xs[i]), 1};
        }
        for (int i = m; i < k; i++) {
            seg[k + i] = ONE;
        }
        for (int i = k - 1; i > 0; i--) {
            seg[i] = _mul(seg[i << 1 | 0], seg[i << 1 | 1]);
        }
        seg[1] = _polyMod(f, seg[1]);
        for (int i = 2; i < k + m; i++) {
            seg[i] = _polyMod(seg[i >> 1], seg[i]);
        }
        long[] ys = new long[m];
        for (int i = 0; i < m; i++) {
            ys[i] = seg[k + i][0];
        }
        return ys;
    }

    public final class ModPolynomial implements ArithmeticOperations<ModPolynomial> {
        public final int DEG;
        private final long[] C;
        private ModPolynomial(long[] c) {
            this.C = c;
            this.DEG = c.length - 1;
        }
        public long apply(long x) {
            long ret = C[DEG];
            for (int i = DEG - 1; i >= 0; i--) ret = ma.mod(ret * x + C[i]);
            return ret;
        }
        public ModPolynomial expand(int deg) {
            return new ModPolynomial(Arrays.copyOf(C, deg + 1));
        }
        public ModPolynomial cut(int deg) {
            return new ModPolynomial(_cut(C, deg));
        }
        public ModPolynomial add(ModPolynomial f) {
            return new ModPolynomial(_add(C, f.C));
        }
        public ModPolynomial sub(ModPolynomial f) {
            return new ModPolynomial(_sub(C, f.C));
        }
        public ModPolynomial mul(ModPolynomial f) {
            return new ModPolynomial(_mul(C, f.C));
        }
        public ModPolynomial mul(ModPolynomial f, int deg) {
            return new ModPolynomial(_mul(C, f.C, deg));
        }
        public ModPolynomial muli(long a) {
            return new ModPolynomial(_muli(C, a));
        }
        public ModPolynomial div(ModPolynomial f) {
            return div(f, DEG);
        }
        public ModPolynomial div(ModPolynomial f, int deg) {
            return new ModPolynomial(_div(C, f.C, deg));
        }
        public ModPolynomial inv(int deg) {
            return new ModPolynomial(_inv(C, deg));
        }
        public ModPolynomial differentiate() {
            return new ModPolynomial(_differentiate(C));
        }
        public ModPolynomial integrate() {
            return new ModPolynomial(_integrate(C));
        }
        public ModPolynomial log(int deg) {
            return new ModPolynomial(_log(C, deg));
        }
        public ModPolynomial exp(int deg) {
            return new ModPolynomial(_exp(C, deg));
        }
        public ModPolynomial pow(long n, int deg) {
            return new ModPolynomial(_pow(C, n, deg));
        }
        public Optional<ModPolynomial> sqrt(int deg) {
            Optional<long[]> sqrt = _sqrt(C, deg);
            if (sqrt.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(new ModPolynomial(sqrt.get()));
            }
        }
        /**
         * @return f(x + c)
         */
        public ModPolynomial translate(long c) {
            return new ModPolynomial(_translate(C, c));
        }
        public ModPolynomial polyDiv(ModPolynomial f) {
            return new ModPolynomial(_polyDiv(C, f.C));
        }
        public ModPolynomial polyMod(ModPolynomial f) {
            return new ModPolynomial(_polyMod(C, f.C));
        }
        public long[] multipointEval(long[] xs) {
            return _multipointEval(C, xs);
        }
        public long getCoef(int deg) {
            return deg < C.length ? C[deg] : 0;
        }
        public long[] getCoefs(){
            return C;
        }
    }
}