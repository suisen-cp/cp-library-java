package lib.tree.dp;

import lib.tree.Tree;

public class Rerooting {
    @FunctionalInterface
    public interface AddChild {
        long add(long dpSum, long dp, int child, int parent);
    }
    @FunctionalInterface
    public interface SubtreeRootInfoAppender {
        long add(long dpSum, int subtreeRoot, int parentOfSubtreeRoot);
    }
    @FunctionalInterface
    public interface MergeChildren {
        long merge(long dpSumL, long dpSumR);
    }
    @FunctionalInterface
    public interface RootInfoAppender {
        long add(long dpSum, int root);
    }

    private final Tree t;
    private final int n;
    private final int[] par;
    private final long[] subTreeDP;
    private final long[] childrenDP;
    private final long[] rerooting;
    
    public Rerooting(Tree t, long e, AddChild addChild, SubtreeRootInfoAppender addSubtreeRoot, MergeChildren mergeChildren, RootInfoAppender addRoot) {
        this.t = t;
        this.n = t.getV();
        this.par = t.parent();
        this.subTreeDP = new long[n];
        this.childrenDP = new long[n];
        this.rerooting = new long[n];
        dfs(e, addChild, addSubtreeRoot);
        bfs(e, addChild, addSubtreeRoot, mergeChildren, addRoot);
    }
    private void dfs(long e, AddChild adCh, SubtreeRootInfoAppender adSubRt) {
        for (int u : t.postOrder()) {
            childrenDP[u] = e;
            for (int v : t.getEdges(u)) {
                if (v == par[u]) continue;
                childrenDP[u] = adCh.add(childrenDP[u], subTreeDP[v], v, u);
            }
            subTreeDP[u] = adSubRt.add(childrenDP[u], u, par[u]);
        }
    }
    private void bfs(long e, AddChild adCh, SubtreeRootInfoAppender adSubRt, MergeChildren mgCh, RootInfoAppender adRt) {
        long[] parDP = new long[n];
        rerooting[t.getRoot()] = subTreeDP[t.getRoot()];
        for (int u : t.preOrder()) {
            int[] adj = t.getEdges(u);
            int l = adj.length;
            long sumR = e;
            for (int i = l - 1; i >= 0; i--) {
                int v = adj[i];
                if (v == par[u]) {
                    sumR = adCh.add(sumR, parDP[u], v, u);
                    continue;
                }
                sumR = adCh.add(rerooting[v] = sumR, subTreeDP[v], v, u);
            }
            long sumL = e;
            for (int i = 0; i < l; i++) {
                int v = adj[i];
                if (v == par[u]) {
                    sumL = adCh.add(sumL, parDP[u], v, u);
                    continue;
                }
                sumR = rerooting[v];
                parDP[v] = adSubRt.add(mgCh.merge(sumL, sumR), u, v);
                rerooting[v] = adRt.add(adCh.add(childrenDP[v], parDP[v], u, v), v);
                sumL = adCh.add(sumL, subTreeDP[v], v, u);
            }
        }
    }
    public long[] dp() {
        return rerooting;
    }
}