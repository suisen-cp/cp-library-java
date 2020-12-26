package lib.string;

import lib.util.array.CharArrays;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Z-algorithm.
 * 
 * S[i:j] := substring of s from i-th character to (j-1)-th character.
 * 
 * z[i] := the longest length of prefix of S s.t. S[0:z[i]] == S[i:i+z[i]].
 */
public final class Zalgorithm {
    private final int[] z;
    public Zalgorithm(int[] s) {
        this.z = build(s);
    }
    public Zalgorithm(char[] s) {
        this(CharArrays.toIntArray(s));
    }
    public Zalgorithm(String s) {
        this(s.toCharArray());
    }
    public static int[] build(int[] s) {
        int n = s.length;
        int[] z = new int[n];
        int c = 0;
        for (int i = 1; i < n; i++){
            if (i + z[i - c] < c + z[c]) {
                z[i] = z[i - c];
            } else {
                int j = Math.max(0, c + z[c] - i);
                while (i + j < n && s[j] == s[i + j]) j++;
                z[c = i] = j;
            }
        }
        z[0] = n;
        return z;
    }
    public int[] get() {
        return z;
    }
}