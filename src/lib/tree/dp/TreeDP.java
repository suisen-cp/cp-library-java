package lib.tree.dp;

import lib.tree.Tree;
import lib.tree.dp.function.IntChildrenMerger;
import lib.tree.dp.function.IntTransitionChildrenToParent;
import lib.tree.dp.function.IntTransitionParentToChild;
import lib.tree.dp.function.LongChildrenMerger;
import lib.tree.dp.function.LongTransitionChildrenToParent;
import lib.tree.dp.function.LongTransitionParentToChild;

public class TreeDP {
    public static int[] subtreeSize(Tree t) {
        int n = t.getV();
        int[] sub = new int[n];
        for (int u : t.postOrder()) {
            for (int v : t.getEdges(u)) {
                sub[u] += sub[v];
            }
            sub[u] += 1;
        }
        return sub;
    }
    public static int[] depth(Tree t) {
        int n = t.getV();
        int[] par = t.parent();
        int[] dep = new int[n];
        for (int u : t.preOrder()) {
            if (u == t.getRoot()) continue;
            dep[u] = dep[par[u]] + 1;
        }
        return dep;
    }
    public static int[] preOrderDP(Tree t, int rootVal, IntTransitionParentToChild trans) {
        int[] par = t.parent();
        int[] dp = new int[t.getV()];
        for (int u : t.preOrder()) dp[u] = u == t.getRoot() ? rootVal : trans.transition(par[u], dp[par[u]], u);
        return dp;
    }
    public static long[] preOrderDP(Tree t, long rootVal, LongTransitionParentToChild trans) {
        int[] par = t.parent();
        long[] dp = new long[t.getV()];
        for (int u : t.preOrder()) dp[u] = u == t.getRoot() ? rootVal : trans.transition(par[u], dp[par[u]], u);
        return dp;
    }
    public static int[] postOrderDP(Tree t, IntTransitionChildrenToParent collector, int mergerId, IntChildrenMerger childrenMerger) {
        int[] par = t.parent();
        int[] dp = new int[t.getV()];
        for (int u : t.postOrder()) {
            int merging = mergerId;
            for (int v : t.getEdges(u)) {
                if (v == par[u]) continue;
                dp[u] = childrenMerger.merge(merging, dp[v]);
            }
            dp[u] = collector.transition(merging, u);
        }
        return dp;
    }
    public static long[] postOrderDP(Tree t, LongTransitionChildrenToParent collector, long mergerId, LongChildrenMerger childrenMerger) {
        int[] par = t.parent();
        long[] dp = new long[t.getV()];
        for (int u : t.postOrder()) {
            long merging = mergerId;
            for (int v : t.getEdges(u)) {
                if (v == par[u]) continue;
                dp[u] = childrenMerger.merge(merging, dp[v]);
            }
            dp[u] = collector.transition(merging, u);
        }
        return dp;
    }
}
