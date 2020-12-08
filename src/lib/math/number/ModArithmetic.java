package lib.math.number;

import java.util.OptionalLong;

/**
 * @author https://atcoder.jp/users/suisen
 */
public abstract class ModArithmetic {
    public abstract long getMod();
    public abstract long mod(long a);
    public abstract long add(long a, long b);
    public abstract long sub(long a, long b);
    public abstract long mul(long a, long b);
    public final long div(long a, long b) {return mul(a, inv(b));}
    public final long inv(long a) {
        a = mod(a);
        long b = getMod();
        long u = 1, v = 0;
        while (b >= 1) {
            long t = a / b;
            a -= t * b;
            long tmp1 = a; a = b; b = tmp1;
            u -= t * v;
            long tmp2 = u; u = v; v = tmp2;
        }
        if (a != 1) throw new ArithmeticException("divide by zero");
        return mod(u);
    }
    public final long fma(long a, long b, long c) {return add(mul(a, b), c);}
    public final long pow(long a, long b) {
        long pow = 1;
        for (a = mod(a); b > 0; b >>= 1, a = mul(a, a)) {
            if ((b & 1) == 1) pow = mul(pow, a);
        }
        return pow;
    }

    public final long add(long a, long b, long c) {return mod(a + b + c);}
    public final long add(long a, long b, long c, long d) {return mod(a + b + c + d);}
    public final long add(long a, long b, long c, long d, long e) {return mod(a + b + c + d + e);}
    public final long add(long a, long b, long c, long d, long e, long f) {return mod(a + b + c + d + e + f);}
    public final long add(long a, long b, long c, long d, long e, long f, long g) {return mod(a + b + c + d + e + f + g);}
    public final long add(long a, long b, long c, long d, long e, long f, long g, long h) {return mod(a + b + c + d + e + f + g + h);}
    public final long add(long... xs) {long s = 0; for (long x : xs) s += x; return mod(s);}
    public final long mul(long a, long b, long c) {return mul(a, mul(b, c));}
    public final long mul(long a, long b, long c, long d) {return mul(a, mul(b, mul(c, d)));}
    public final long mul(long a, long b, long c, long d, long e) {return mul(a, mul(b, mul(c, mul(d, e))));}
    public final long mul(long a, long b, long c, long d, long e, long f) {return mul(a, mul(b, mul(c, mul(d, mul(e, f)))));}
    public final long mul(long a, long b, long c, long d, long e, long f, long g) {return mul(a, mul(b, mul(c, mul(d, mul(e, mul(f, g))))));}
    public final long mul(long a, long b, long c, long d, long e, long f, long g, long h) {return mul(a, mul(b, mul(c, mul(d, mul(e, mul(f, mul(g, h)))))));}
    public final long mul(long... xs) {long s = 1; for (long x : xs) s = mul(s, x); return s;}
    /**
     * @return pair(g, x) s.t. g = gcd(a, b), xa = g (mod b), 0 <= x < b/g
     */
    public final long[] gcdInv(long a) {
        final long m = getMod();
        a = mod(a);
        if (a == 0) return new long[]{m, 0};
        long s = m, t = a;
        long m0 = 0, m1 = 1;
        while (t > 0) {
            long u = s / t;
            s -= t * u;
            m0 -= m1 * u;
            long tmp;
            tmp = s; s = t; t = tmp;
            tmp = m0; m0 = m1; m1 = tmp;
        }
        if (m0 < 0) m0 += m / s;
        return new long[]{s, m0};
    }
    public final OptionalLong sqrt(long a) {
        a = mod(a);
        if (a == 0) return OptionalLong.of(0);
        if (a == 1) return OptionalLong.of(1);
        long p = getMod();
        if (pow(a, (p - 1) >> 1) != 1) {
            return OptionalLong.empty();
        }
        if ((p & 3) == 3) {
            return OptionalLong.of(pow(a, (p + 1) >> 2));
        }
        if ((p & 7) == 5) {
            if (pow(a, (p - 1) >> 2) == 1) {
                return OptionalLong.of(pow(a, (p + 3) >> 3));
            } else {
                return OptionalLong.of(mul(pow(2, (p - 1) >> 2), pow(a, (p + 3) >> 3)));
            }
        }
        long S = 0, Q = p - 1;
        while ((Q & 1) == 0) {
            ++S;
            Q >>= 1;
        }
        long z = 1;
        while (pow(z, (p - 1) >> 1) != p - 1) ++z;
        long c = pow(z, Q), R = pow(a, (Q + 1) / 2), t = pow(a, Q), M = S;
        while (t != 1) {
            long cur = t;
            int i;
            for (i = 1; i < M; i++) {
                cur = mul(cur, cur);
                if (cur == 1) break;
            }
            long b = pow(c, 1L << (M - i - 1));
            c = mul(b, b); R = mul(R, b); t = mul(t, b, b); M = i;
        }
        return OptionalLong.of(R);
    }

    public final int primitiveRoot() {
        final int m = Math.toIntExact(getMod());
        if (m == 2) return 1;
        if (m == 167772161) return 3;
        if (m == 469762049) return 3;
        if (m == 754974721) return 11;
        if (m == 998244353) return 3;
        int[] divs = new int[20];
        divs[0] = 2;
        int cnt = 1;
        int x = (m - 1) / 2;
        while (x % 2 == 0) x /= 2;
        for (int i = 3; (long) i * i <= x; i += 2) {
            if (x % i == 0) {
                divs[cnt++] = i;
                while (x % i == 0) x /= i;
            }
        }
        if (x > 1) {
            divs[cnt++] = x;
        }
        for (int g = 2; ; g++) {
            boolean ok = true;
            for (int i = 0; i < cnt; i++) {
                if (pow(g, (m - 1) / divs[i]) == 1) {
                    ok = false;
                    break;
                }
            }
            if (ok) return g;
        }
    }

    /** array operations */

    public final long[] rangeInv(int n) {
        final long MOD = getMod();
        long[] invs = new long[n + 1];
        if (n == 0) return invs;
        invs[1] = 1;
        for (int i = 2; i <= n; i++) {
            invs[i] = mul(MOD - MOD / i, invs[(int) (MOD % i)]);
        }
        return invs;
    }
    public final long[] arrayInv(long[] a) {
        int n = a.length;
        long[] l = new long[n + 1];
        long[] r = new long[n + 1];
        l[0] = r[n] = 1;
        for (int i = 0; i < n; i++) l[i + 1] = mul(l[i], a[i    ]);
        for (int i = n; i > 0; i--) r[i - 1] = mul(r[i], a[i - 1]);
        long invAll = inv(l[n]);
        long[] invs = new long[n];
        for (int i = 0; i < n; i++) {
            invs[i] = mul(l[i], r[i + 1], invAll);
        }
        return invs;
    }
    public final long[] factorial(int n) {
        long[] ret = new long[n + 1];
        ret[0] = 1;
        for (int i = 1; i <= n; i++) ret[i] = mul(ret[i - 1], i);
        return ret;
    }
    public final long[] factorialInv(int n) {
        long facN = 1;
        for (int i = 2; i <= n; i++) facN = mul(facN, i);
        long[] invs = new long[n + 1];
        invs[n] = inv(facN);
        for (int i = n; i > 0; i--) invs[i - 1] = mul(invs[i], i);
        return invs;
    }
    public final long[] rangePower(long a, int n) {
        a = mod(a);
        long[] pows = new long[n + 1];
        pows[0] = 1;
        for (int i = 1; i <= n; i++) pows[i] = mul(pows[i - 1], a);
        return pows;
    }
    public final long[] rangePowerInv(long a, int n) {
        a = mod(a);
        long[] invs = new long[n + 1];
        invs[n] = inv(pow(a, n));
        for (int i = n; i > 0; i--) invs[i - 1] = mul(invs[i], a);
        return invs;
    }

    /** combinatric operations */

    public final long[][] combTable(int n) {
        long[][] comb = new long[n + 1][];
        for (int i = 0; i <= n; i++) {
            comb[i] = new long[i + 1];
            comb[i][0] = comb[i][i] = 1;
            for (int j = 1; j < i; j++) {
                comb[i][j] = add(comb[i - 1][j - 1], comb[i - 1][j]);
            }
        }
        return comb;
    }
    public final long naiveComb(long n, long r) {
        if (r < 0 || r > n) return 0;
        r = Math.min(r, n - r);
        long num = 1, den = 1;
        for (int i = 0; i < r; i++) {
            num = mul(num, n - i);
            den = mul(den, i + 1);
        }
        return div(num, den);
    }
    public final long naivePerm(long n, long r) {
        if (r < 0 || r > n) return 0;
        long res = 1;
        for (long i = 0; i < r; i++) res = mul(res, n - i);
        return res;
    }
}