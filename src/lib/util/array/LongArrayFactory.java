package lib.util.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.IntToLongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import lib.util.function.IntToLongBiFunction;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * (NON-DESTRUCTIVE) methods that returns long array.
 */
public final class LongArrayFactory {
    private LongArrayFactory() {}
    public static long[] filled(int n, long init) {
        long[] ret = new long[n];
        Arrays.fill(ret, init);
        return ret;
    }
    public static long[][] filled(int n, int m, long init) {
        long[][] ret = new long[n][m];
        for (int i = 0; i < n; i++) Arrays.fill(ret[i], init);
        return ret;
    }
    public static long[] setAll(int n, IntToLongFunction f) {
        long[] a = new long[n];
        Arrays.setAll(a, f);
        return a;
    }
    public static long[][] setAll(int n, int m, IntToLongBiFunction f) {
        long[][] a = new long[n][m];
        for (int i = 0; i < n; i++) {
            IntToLongFunction g = f.curry(i);
            a[i] = setAll(m, g);
        }
        return a;
    }
    public static long[] toArray(Collection<? extends Number> collection) {
        int n = collection.size();
        long[] ret = new long[n];
        java.util.Iterator<? extends Number> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) ret[i++] = it.next().longValue();
        return ret;
    }
    public static long[] unique(long[] a) {
        HashSet<Long> set = new HashSet<>();
        for (long e : a) set.add(e);
        long[] b = new long[set.size()];
        int j = 0;
        for (long e : a) if (set.contains(e)) set.remove(b[j++] = e);
        return b;
    }
    public static long[][] transpose(long[][] a) {
        return setAll(a[0].length, a.length, (i, j) -> a[j][i]);
    }
    public static long[] map(long[] a, LongUnaryOperator f) {
        return setAll(a.length, i -> f.applyAsLong(a[i]));
    }
    public static long[] filter(long[] a, LongPredicate p) {
        int m = 0;
        for (long e : a) if (p.test(e)) m++;
        long[] res = new long[m];
        int j = 0;
        for (long e : a) if (p.test(e)) res[j++] = e;
        return res;
    }
}