package lib.algorithm;

import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

import lib.util.Longs;
import lib.util.function.IntBiPredicate;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Doubling {
    public final int[][] doubling;
    private final int height;
    private final int n;
    public Doubling(final int[] a, final long maxStep) {
        this.n = a.length;
        this.height = Longs.ceilExponent(maxStep) + 2;
        this.doubling = new int[height][n];
        build(a);
    }
    public int getHeight() {return height;}
    public int step(int i, long step) {
        int h = height - 1;
        while (step > 0) {
            if ((step & (1L << h)) != 0) {
                i = doubling[h][i];
                step ^= 1L << h;
            }
            h--;
        }
        return i;
    }
    public int stepWhile(int i, final IntPredicate p) {
        int h = height - 1;
        while (h >= 0) {
            if (p.test(doubling[h][i])) i = doubling[h][i];
            h--;
        }
        return i;
    }
    public int stepUntil(final int i, final IntPredicate p) {return p.test(i) ? i : doubling[0][stepWhile(i, p.negate())];}
    public int[] biStep(final int u, final int v, final int exponent) {
        final int us = doubling[exponent][u];
        final int vs = doubling[exponent][v];
        return new int[]{us, vs};
    }
    private long biStep(final long uv, final int exponent) {
        final int u = (int) (uv >>> 32);
        final int v = (int) (uv & 0xffff_ffffL);
        return (long) doubling[exponent][u] << 32 | doubling[exponent][v];
    }
    public int[] biStepWhile(final int u, final int v, final IntBiPredicate p) {
        final long ret = biStepWhile((long) u << 32 | v, p.toLongPredicate());
        return new int[]{(int) (ret >> 32), (int) (ret & 0xffff_ffffL)};
    }
    private long biStepWhile(long uv, final LongPredicate p) {
        int h = height - 1;
        while (h >= 0) {
            final long step = biStep(uv, h);
            if (p.test(step)) {
                uv = step;
            }
            h--;
        }
        return uv;
    }
    public int[] biStepUntil(final int u, final int v, final IntBiPredicate p) {
        final long ret = biStepUntil((long) u << 32 | v, p.toLongPredicate());
        return new int[]{(int) (ret >> 32), (int) (ret & 0xffff_ffffL)};
    }
    private long biStepUntil(final long uv, final LongPredicate p) {return p.test(uv) ? uv : biStep(biStepWhile(uv, p.negate()), 0);}
    public int[] parallelStep(final int[] a, final int exponent) {
        final int[] ret = a.clone();
        for (int i = 0; i < a.length; i++) ret[i] = doubling[exponent][a[i]];
        return ret;
    }
    private void build(final int[] a) {
        doubling[0] = a;
        for (int h = 1; h < height; h++) for (int i = 0; i < n; i++) doubling[h][i] = doubling[h - 1][doubling[h - 1][i]];
    }
}