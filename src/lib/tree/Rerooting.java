package lib.tree;

public class Rerooting {
    @FunctionalInterface
    public interface AddChild {
        long add(long dpSum, long dp, int child, int parent);
    }
    @FunctionalInterface
    public interface AddSubtreeRoot {
        long add(long dpSum, int root, int parent);
    }
    @FunctionalInterface
    public interface MergeChildren {
        long merge(long dpSumL, long dpSumR);
    }
    @FunctionalInterface
    public interface AddRoot {
        long add(long dpSum, int root);
    }

    private final Tree t;
    private final int n;
    private final long[] subTreeDP;
    private final long[] childrenDP;
    private final long[] rerooting;
    
    public Rerooting(Tree t, long e, AddChild addChild, AddSubtreeRoot addSubtreeRoot, MergeChildren mergeChildren, AddRoot addRoot) {
        this.t = t;
        this.n = t.n;
        this.subTreeDP = new long[n];
        this.childrenDP = new long[n];
        this.rerooting = new long[n];
        dfs(e, addChild, addSubtreeRoot);
        bfs(e, addChild, addSubtreeRoot, mergeChildren, addRoot);
    }
    private void dfs(long e, AddChild adCh, AddSubtreeRoot adSubRt) {
        for (int u : t.pst) {
            childrenDP[u] = e;
            for (int v : t.adj[u]) {
                if (v == t.par[u]) continue;
                childrenDP[u] = adCh.add(childrenDP[u], subTreeDP[v], v, u);
            }
            subTreeDP[u] = adSubRt.add(childrenDP[u], u, t.par[u]);
        }
    }
    private void bfs(long e, AddChild adCh, AddSubtreeRoot adSubRt, MergeChildren mgCh, AddRoot adRt) {
        long[] parDP = new long[n];
        rerooting[t.root] = subTreeDP[t.root];
        for (int u : t.pre) {
            int l = t.adj[u].length;
            long sumR = e;
            for (int i = l - 1; i >= 0; i--) {
                int v = t.adj[u][i];
                if (v == t.par[u]) {
                    sumR = adCh.add(sumR, parDP[u], v, u);
                    continue;
                }
                sumR = adCh.add(rerooting[v] = sumR, subTreeDP[v], v, u);
            }
            long sumL = e;
            for (int i = 0; i < l; i++) {
                int v = t.adj[u][i];
                if (v == t.par[u]) {
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