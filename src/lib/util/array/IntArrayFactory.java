package lib.util.array;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntArrayFactory {
    private IntArrayFactory() {}
    public static int[] filled(int n, int init) {
        int[] ret = new int[n];
        Arrays.fill(ret, init);
        return ret;
    }
    public static int[][] filled(int n, int m, int init) {
        int[][] ret = new int[n][m];
        for (int i = 0; i < n; i++) Arrays.fill(ret[i], init);
        return ret;
    }
    public static int[] setAll(int n, IntUnaryOperator f) {
        int[] a = new int[n];
        Arrays.setAll(a, f);
        return a;
    }
    public static int[][] setAll(int n, int m, IntBinaryOperator f) {
        int[][] a = new int[n][m];
        for (int i = 0; i < n; i++) {
            int _i = i; a[i] = setAll(m, j -> f.applyAsInt(_i, j));
        }
        return a;
    }
    public static int[] toArray(Collection<? extends Number> collection) {
        int n = collection.size();
        int[] ret = new int[n];
        java.util.Iterator<? extends Number> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) ret[i++] = it.next().intValue();
        return ret;
    }
    public static int[] unique(int[] a) {
        HashSet<Integer> set = new HashSet<>();
        for (int e : a) set.add(e);
        int[] b = new int[set.size()];
        int j = 0;
        for (int e : a) if (set.contains(e)) set.remove(b[j++] = e);
        return b;
    }
    public static int[][] transpose(int[][] a) {
        return setAll(a[0].length, a.length, (i, j) -> a[j][i]);
    }
    public static int[] map(int[] a, IntUnaryOperator f) {
        return setAll(a.length, i -> f.applyAsInt(a[i]));
    }
    public static int[] histogram(int[] a, int max) {
        int[] ret = new int[max + 1];
        for (int e : a) ret[e]++;
        return ret;
    }
    public static int[] filter(int[] a, IntPredicate p) {
        int m = 0;
        for (int e : a) if (p.test(e)) m++;
        int[] res = new int[m];
        int j = 0;
        for (int e : a) if (p.test(e)) res[j++] = e;
        return res;
    }
    public static int[] filterOfRange(int l, int r, IntPredicate p) {
        int m = 0;
        for (int i = l; i < r; i++) if (p.test(i)) m++;
        int[] res = new int[m];
        for (int i = l, j = 0; i < r; i++) if (p.test(i)) res[j++] = i;
        return res;
    }
    public static int[] filterOfRange(int n, IntPredicate p) {
        return filterOfRange(0, n, p);
    }
}