package lib.util.array;

import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * 1. DESTRUCTIVE methods for char arrays.
 * 2. methods that receives arrays and return some results (except for char arrays).
 */
public final class CharArrays {
    private CharArrays(){}
    public static void swap(char[] a, int u, int v) {
        char tmp = a[u]; a[u] = a[v]; a[v] = tmp;
    }
    public static void reverse(char[] a, int begin, int end) {
        while (end - begin > 1) swap(a, begin++, --end);
    }
    public static void reverse(char[] a) {
        reverse(a, 0, a.length);
    }
    public static void sortDescending(char[] a) {
        Arrays.sort(a);
        reverse(a);
    }
    public static int[] toIntArray(char[] s) {
        int n = s.length;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = s[i];
        }
        return a;
    }
    public static String join(char[] a, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
    public static String join(char[][] a, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
    public static String joinWithPrefixAndSuffix(char[] a, IntFunction<String> idxToPre, IntFunction<String> idxToSuf, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(idxToPre.apply(i)).append(a[i]).append(idxToSuf.apply(i));
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
}