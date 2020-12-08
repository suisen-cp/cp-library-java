package lib.math;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class PolynomialSquareRoot {
    public static final int INT_SQRT_MAX = 46340;
    public static final long LONG_SQRT_MAX = 3037000499L;

    public static OptionalInt sqrt(int x) {
        int l = 0, r = INT_SQRT_MAX + 1;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            if (m * m <= x) {
                l = m;
            } else {
                r = m;
            }
        }
        return l * l == x ? OptionalInt.of(l) : OptionalInt.empty();
    }
    
    public static OptionalLong sqrt(long x) {
        long l = 0, r = LONG_SQRT_MAX + 1;
        while (r - l > 1) {
            long m = (l + r) >> 1;
            if (m * m <= x) {
                l = m;
            } else {
                r = m;
            }
        }
        return l * l == x ? OptionalLong.of(l) : OptionalLong.empty();
    }

    public static Optional<int[]> polySqrt(int[] f) {
        int lz = 0, tz = f.length - 1;
        while (lz < f.length && f[lz] == 0) lz++;
        if (lz == f.length) {
            return Optional.of(new int[]{0});
        }
        while (f[tz] == 0) tz--;
        if (((tz | lz) & 1) == 1) return Optional.empty();
        f = Arrays.copyOfRange(f, lz, tz + 1);
        final int m = (tz - lz) >> 1;
        int[] g = new int[m + 1];
        OptionalInt sqrt0 = sqrt(f[0]);
        if (sqrt0.isEmpty()) {
            return Optional.empty();
        }
        final int c0 = g[0] = sqrt0.getAsInt();
        final int d = c0 << 1;
        f[0] -= c0 * c0;
        for (int i = 1; i <= m; i++) {
            if (f[i] % d != 0) return Optional.empty();
            final int ci = g[i] = f[i] / d;
            for (int j = 0; j < i; j++) {
                f[i + j] -= (g[j] * ci) << 1;
            }
            f[i + i] -= ci * ci;
        }
        for (int c : f) {
            if (c != 0) return Optional.empty();
        }
        int[] sqrt = new int[(tz >> 1) + 1];
        System.arraycopy(g, 0, sqrt, lz >> 1, m + 1);
        return Optional.of(sqrt);
    }

    public static Optional<long[]> polySqrt(long[] f) {
        int lz = 0, tz = f.length - 1;
        while (lz < f.length && f[lz] == 0) lz++;
        if (lz == f.length) {
            return Optional.of(new long[]{0});
        }
        while (f[tz] == 0) tz--;
        if (((tz | lz) & 1) == 1) return Optional.empty();
        f = Arrays.copyOfRange(f, lz, tz + 1);
        final int m = (tz - lz) >> 1;
        long[] g = new long[m + 1];
        OptionalLong sqrt0 = sqrt(f[0]);
        if (sqrt0.isEmpty()) {
            return Optional.empty();
        }
        final long c0 = g[0] = sqrt0.getAsLong();
        final long d = c0 << 1;
        f[0] -= c0 * c0;
        for (int i = 1; i <= m; i++) {
            if (f[i] % d != 0) return Optional.empty();
            final long ci = g[i] = f[i] / d;
            for (int j = 0; j < i; j++) {
                f[i + j] -= (g[j] * ci) << 1;
            }
            f[i + i] -= ci * ci;
        }
        for (long c : f) {
            if (c != 0) return Optional.empty();
        }
        long[] sqrt = new long[(tz >> 1) + 1];
        System.arraycopy(g, 0, sqrt, lz >> 1, m + 1);
        return Optional.of(sqrt);
    }
}
