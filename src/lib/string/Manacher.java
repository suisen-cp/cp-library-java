package lib.string;

import lib.util.array.CharArrays;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Manacher's Algorithm.
 * 
 * S[i] := i-th character of the String S.
 * 
 * r[i] := the longest length of parindrome that has S[i] as the center.
 */
@SuppressWarnings("PointlessBitwiseExpression")
public final class Manacher {
    private final int n;
    private final int m;
    private final int[] s;
    private final int[] r;
    public Manacher(int[] s) {
        this.n = s.length;
        this.m = 2 * n - 1;
        this.s = s.clone();
        this.r = build();
    }
    public Manacher(char[] s) {
        this(CharArrays.toIntArray(s));
    }
    public Manacher(String s) {
        this(s.toCharArray());
    }
    private int[] build() {
        int[] t = new int[m];
        for (int i = 0; i < m; i++) {
            t[i] = (i & 1) == 0 ? s[i >> 1] : Integer.MAX_VALUE;
        }
        int[] r = new int[m];
        int i = 0, j = 0;
        while (i < m) {
            while (i - j >= 0 && i + j < m && t[i - j] == t[i + j]) {
                ++j;
            }
            r[i] = j;
            int k = 1;
            while (i - k >= 0 && k + r[i - k] < j) {
                r[i + k] = r[i - k];
                ++k;
            }
            i += k; j -= k;
        }
        return r;
    }
    public long countParindrome() {
        long cnt = 0;
        for (int i = 0 ; i < m; i++) {
            cnt += (i & 1) == 0 ? (r[i] + 1) >> 1 : r[i] >> 1;
        }
        return cnt;
    }
    public int radiusOfOddParindrome(int i) {
        return r[i << 1 | 1];
    }
    public int radiusOfEvenParindrome(int i) {
        return r[i << 1 | 0];
    }
}