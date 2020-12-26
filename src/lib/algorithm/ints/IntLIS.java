package lib.algorithm.ints;

import java.util.Arrays;

public class IntLIS {
    private final int n;
    private final boolean strictlyIncreasing;
    private final int[] prev;
    private final int[] dp;
    private final int max;
    public IntLIS(int[] a, boolean strictlyIncreasing) {
        this.n = a.length;
        this.strictlyIncreasing = strictlyIncreasing;
        this.prev = new int[a.length];
        this.dp = new int[a.length + 1];
        this.max = build(a);
    }
    public int max() {
        return max;
    }
    public int[] restore() {
        boolean[] seen = new boolean[n];
        int[] seq = new int[max];
        if (max == 0) return seq;
        for (int i = n - 1; i >= 0; i--) {
            if (seen[i]) continue;
            int idx = max;
            for (int j = i; j >= 0; j = prev[j]) {
                seq[--idx] = j;
                seen[j] = true;
            }
            if (idx == 0) return seq;
        }
        throw new AssertionError();
    }
    public int[] previous() {
        return prev.clone();
    }
    private int build(int[] a) {
        Arrays.fill(dp, n);
        dp[0] = -1;
        int max = 0;
        for (int i = 0; i < n; i++) {
            int l = 0, r = max + 1;
            if (strictlyIncreasing) {
                while (r - l > 1) {
                    int m = (l + r) >> 1;
                    if (a[dp[m]] < a[i]) {
                        l = m;
                    } else {
                        r = m;
                    }
                }
            } else {
                while (r - l > 1) {
                    int m = (l + r) >> 1;
                    if (a[dp[m]] <= a[i]) {
                        l = m;
                    } else {
                        r = m;
                    }
                }
            }
            dp[r] = i;
            prev[i] = dp[l];
            max = Math.max(max, r);
        }
        return max;
    }
}
