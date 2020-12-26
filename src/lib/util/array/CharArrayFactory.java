package lib.util.array;

import java.util.Collection;

import lib.util.Chars;

public class CharArrayFactory {
    private CharArrayFactory(){}
    public static char[] toArray(final Collection<Character> collection) {
        final int n = collection.size();
        final char[] ret = new char[n];
        final Object[] coll = collection.toArray();
        for (int i = 0; i < n; i++) ret[i] = (Character) coll[i];
        return ret;
    }
    public static char[][] transpose(final char[][] a) {
        final int n = a.length;
        final int m = a[0].length;
        final char[][] ret = new char[m][n];
        for (int j = 0; j < m; j++) for (int i = 0; i < n; i++) ret[j][i] = a[i][j];
        return ret;
    }
    public static int[] asciiCount(final char[] s) {
        final int[] cnt = new int[Chars.ASCII_SIZE];
        final int n = s.length;
        for (final char c : s) cnt[c]++;
        return cnt;
    }
    public static char[] concat(final char[]... a) {
        final StringBuilder sb = new StringBuilder();
        for (final char[] s : a) sb.append(s);
        return sb.toString().toCharArray();
    }
}