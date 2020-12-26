package lib.string;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Matching Strings T1, T2, ..., Tq with S.
 * 
 * Preprocess: O(|S|)
 *     1. Build SA: O(|S|)
 *     2. Build LCP Array: O(|S|)
 *     3. Build RmQ SegTree of LCP Array: O(|S|)
 * 
 * Query: O(|T| + log|S|)
 *     1. Binary search on Suffix Array (Calculates LCP(suf[l], suf[m]) and LCP(suf[l], T)): O(|T| + log|S|)
 *        - Calculates LCP(S[l:], S[m:]) using SegTree: each O(1), total O(log|S|)
 *        - LCP(suf[l], T) is increasing: total O(|T|)
 */
@SuppressWarnings("PointlessBitwiseExpression")
public final class Matching {
    private final int n;     // the length of S.
    private final char[] s;  // String S.
    private final int[] sa;  // suffix array of S.
    private final int[] seg; // 1-indexed SegmentTree.
    private final int btm;   // the size of the lowest level of the segTree.

    public Matching(char[] s) {
        this.n = s.length;
        this.s = s;
        this.sa = SuffixArray.suffixArray(s);
        int k = 1;
        while (k < n) k <<= 1;
        this.btm = k;
        this.seg = new int[k << 1];
        System.arraycopy(SuffixArray.lcpArray(s, sa), 0, seg, btm, n);
        for (int i = btm + n; i < seg.length; i++) seg[i] = Integer.MAX_VALUE;
        for (int i = btm - 1; i > 0; i--) seg[i] = Math.min(seg[i << 1 | 0], seg[i << 1 | 1]);
    }

    /**
     * Search T in S and return the place of it if exists, otherwise negative value.
     * 
     * @param t matching string.
     * @return  the place of T in S if exists. othewise negative value.
     */
    public int query(char[] t) {
        int l = 0, r = btm;
        int ltlcp = (int) (lcpAndCmp(t, 0, 0) >>> 32);
        int k = 1;
        while (r - l > 1) {
            int m = (l + r) >> 1;
            int lmlcp = seg[k << 1];
            if (lmlcp == Integer.MAX_VALUE || ltlcp > lmlcp) {
                r = m;
            } else if (ltlcp < lmlcp) {
                l = m;
            } else {
                long p = lcpAndCmp(t, m, ltlcp);
                int lcp = (int) (p >>> 32);
                int cmp = (int) (p & 0xffff_ffffL);
                if (cmp >= 0) {
                    l = m;
                    ltlcp = lcp;
                } else {
                    r = m;
                }
            }
            k = r == m ? k << 1 : (k << 1) | 1;
        }
        return ltlcp == t.length ? l : -1;
    }

    /**
     * @param t      maching string.
     * @param index  index of suffix array.
     * @param offset the length we can skip checking the equality of T and suf[index].
     * @return upper 32 bits holds the length of lcp(T, suf[index]), lower 32 bits
     *         holds the result of comparation between T and suf[m].
     */
    private long lcpAndCmp(char[] t, int index, int offset) {
        if (index >= n) return (long) offset << 32 | 0xffff_ffffL;
        int ti = offset;
        int si = sa[index] + offset;
        while (ti < t.length && si < n) {
            if (t[ti] == s[si]) {
                ti++; si++;
            } else {
                return (long) ti << 32 | (t[ti] - s[si]);
            }
        }
        return (long) ti << 32 | 0;
    }
}