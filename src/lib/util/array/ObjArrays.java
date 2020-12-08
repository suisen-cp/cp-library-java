package lib.util.array;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class ObjArrays {
    private ObjArrays(){}
    public static <T> void swap(T[] a, int u, int v) {
        T tmp = a[u]; a[u] = a[v]; a[v] = tmp;
    }
    public static <T> void reverse(T[] a, int l, int r) {
        while (r - l > 1) swap(a, l++, --r);
    }
    public static <T> void reverse(T[] a) {
        reverse(a, 0, a.length);
    }
    public static <T> T fold(T[] a, BinaryOperator<T> op) {
        T ret = a[0];
        for (int i = 1; i < a.length; i++) ret = op.apply(ret, a[i]);
        return ret;
    }
    public static <T extends Comparable<T>> T max(T[] a, BinaryOperator<T> op) {
        return fold(a, (o1, o2) -> o1.compareTo(o2) > 0 ? o1 : o2);
    }
    public static <T> T max(T[] a, BinaryOperator<T> op, Comparator<T> comparator) {
        return fold(a, (o1, o2) -> comparator.compare(o1, o2) > 0 ? o1 : o2);
    }
    public static <T extends Comparable<T>> T min(T[] a, BinaryOperator<T> op) {
        return fold(a, (o1, o2) -> o1.compareTo(o2) < 0 ? o1 : o2);
    }
    public static <T> T min(T[] a, BinaryOperator<T> op, Comparator<T> comparator) {
        return fold(a, (o1, o2) -> comparator.compare(o1, o2) < 0 ? o1 : o2);
    }
    public static <T extends Comparable<T>> int argmax(T[] a) {
        T max = a[0];
        int argmax = 0;
        for (int i = 0; i < a.length; i++) if (max.compareTo(a[i]) < 0) max = a[argmax = i];
        return argmax;
    }
    public static <T> int argmax(T[] a, Comparator<T> comparator) {
        T max = a[0];
        int argmax = 0;
        for (int i = 0; i < a.length; i++) if (comparator.compare(max, a[i]) < 0) max = a[argmax = i];
        return argmax;
    }
    public static <T extends Comparable<T>> int argmin(T[] a) {
        T min = a[0];
        int argmin = 0;
        for (int i = 0; i < a.length; i++) if (min.compareTo(a[i]) > 0) min = a[argmin = i];
        return argmin;
    }
    public static <T> int argmin(T[] a, Comparator<T> comparator) {
        T min = a[0];
        int argmin = 0;
        for (int i = 0; i < a.length; i++) if (comparator.compare(min, a[i]) > 0) min = a[argmin = i];
        return argmin;
    }
    public static <T> void map(T[] a, UnaryOperator<T> op) {
        Arrays.setAll(a, i -> op.apply(a[i]));
    }
    public static <T> void permute(T[] a, int[] p) {
        int n = p.length;
        boolean[] settled = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = i; !settled[j]; j = p[j]) {
                if (p[j] == i) {
                    settled[j] = true;
                    break;
                }
                swap(a, j, p[j]);
                settled[j] = true;
            }
        }
    }
}