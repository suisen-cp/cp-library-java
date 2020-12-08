package lib.string;

import lib.util.array.CharArrays;
import lib.util.collections.ints.IntArrayList;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class KMP {
    private final int n;
    private final int[] s;
    public final int[] table;
    public KMP(int[] s) {
        this.n = s.length;
        this.s = s.clone();
        this.table = buildTable(s);
    }
    public KMP(char[] s) {
        this(CharArrays.toIntArray(s));
    }
    public KMP(String s) {
        this(s.toCharArray());
    }
    /**
     * @return array {@code a} s.t. a[i]=max{j<i|S[0:j]==S[i-j:i]}
     */
    public static int[] buildTable(int[] s) {
        int n = s.length;
        int[] a = new int[n + 1];
        a[0] = -1;
        int j = -1;
        for (int i = 0; i < n; i++) {
            while (j >= 0 && s[i] != s[j]) j = a[j];
            a[i + 1] = ++j;
        }
        return a;
    }
    /**
     * @return array {@code a} s.t. a[i]=max{j<i|S[0:j]==S[i-j:i]}
     */
    public static int[] buildTable(char[] s) {
        return buildTable(CharArrays.toIntArray(s));
    }
    /**
     * @return array {@code a} s.t. a[i]=max{j<i|S[0:j]==S[i-j:i]}
     */
    public static int[] buildTable(String s) {
        return buildTable(s.toCharArray());
    }
    public IntArrayList matching(int[] t) {
        IntArrayList res = new IntArrayList();
        int m = 0, i = 0;
        while (m + i < n) {
            if (t[i] == s[m + i]) {
                if (++i == t.length) {
                    res.add(m);
                    m = m + i - table[i];
                    i = table[i];
                }
            } else {
                m = m + i - table[i];
                if (i > 0) i = table[i];
            }
        }
        return res;
    }
    public IntArrayList matching(char[] t) {
        return matching(CharArrays.toIntArray(t));
    }
    public IntArrayList matching(String t) {
        return matching(t.toCharArray());
    }
}