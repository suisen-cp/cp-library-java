package lib.util.itertools;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class Permutation implements Iterable<int[]> {
    private final int[] p;
    private final boolean isAscending;
    private Permutation(int[] p, boolean isAscending) {
        this.p = p;
        this.isAscending = isAscending;
    }
    @Override
    public Iterator<int[]> iterator() {
        final int[] q = p.clone();
        if (isAscending) {
            //noinspection Convert2Diamond
            return new Iterator<int[]>() {
                boolean flg = false;
                public boolean hasNext() {return flg ? nextPermutation(q) != null : (flg = true);}
                public int[] next() {return q;}
            };
        } else {
            //noinspection Convert2Diamond
            return new Iterator<int[]>() {
                boolean flg = false;
                public boolean hasNext() {return flg ? predPermutation(q) != null : (flg = true);}
                public int[] next() {return q;}
            };
        }
    }
    public static Permutation of(int[] p) {
        return ofAscending(p);
    }
    public static Permutation of(int n) {
        return ofAscending(n);
    }
    public static Permutation ofOneIndexed(int n) {
        return ofAscendingOneIndexed(n);
    }
    public static Permutation ofAscending(int[] p) {
        return new Permutation(p.clone(), true);
    }
    public static Permutation ofAscending(int n) {
        return new Permutation(ascendingPermutation(n), true);
    }
    public static Permutation ofAscendingOneIndexed(int n) {
        return new Permutation(ascendingPermutationOneIndexed(n), true);
    }
    public static Permutation ofDescending(int[] p) {
        return new Permutation(p.clone(), false);
    }
    public static Permutation ofDescending(int n) {
        return new Permutation(descendingPermutation(n), false);
    }
    public static Permutation ofDescendingOneIndexed(int n) {
        return new Permutation(descendingPermutationOneIndexed(n), false);
    }
    public static int[] ascendingPermutation(int n) {
        int[] p = new int[n];
        Arrays.setAll(p, i -> i);
        return p;
    }
    public static int[] ascendingPermutationOneIndexed(int n) {
        int[] p = new int[n];
        Arrays.setAll(p, i -> i + 1);
        return p;
    }
    public static int[] descendingPermutation(int n) {
        int[] p = new int[n];
        Arrays.setAll(p, i -> n - i - 1);
        return p;
    }
    public static int[] descendingPermutationOneIndexed(int n) {
        int[] p = new int[n];
        Arrays.setAll(p, i -> n - i);
        return p;
    }
    public static int[] nextPermutation(int[] p) {
        int n = p.length;
        for (int i = n - 1; i > 0; i--) {
            if (p[i] <= p[i - 1]) continue;
            int l = i, r = n;
            while (r - l > 1) {
                int m = (l + r) >> 1;
                if (p[m] > p[i - 1]) l = m;
                else r = m;
            }
            swap(p, l, i - 1);
            reverse(p, i, n);
            return p;
        }
        return null;
    }
    public static int[] predPermutation(int[] p) {
        int n = p.length;
        for (int i = n - 1; i > 0; i--) {
            if (p[i] >= p[i - 1]) continue;
            int l = i, r = n;
            while (r - l > 1) {
                int m = (l + r) >> 1;
                if (p[m] < p[i - 1]) l = m;
                else r = m;
            }
            swap(p, l, i - 1);
            reverse(p, i, n);
            return p;
        }
        return null;
    }
    private static void reverse(int[] p, int l, int r) {
        while (l < r) swap(p, l++, --r);
    }
    private static void swap(int[] p, int i, int j) {
        int tmp = p[i]; p[i] = p[j]; p[j] = tmp;
    }
}