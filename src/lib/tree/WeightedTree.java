package lib.tree;
/**
 * @author https://atcoder.jp/users/suisen
 */
public class WeightedTree extends Tree {
    final long[] cst;
    final long[][] adjCost;
    WeightedTree(int n, int root, int[][] adj, long[][] adjCost) {
        super(n, root, adj);
        this.cst = new long[n];
        this.adjCost = adjCost;
        for (int u = 0; u < n; u++) {
            int k = adj[u].length;
            for (int i = 0; i < k; i++) {
                int v = adj[u][i];
                long c = adjCost[u][i];
                if (v == par[u]) {
                    cst[u] = c;
                } else {
                    cst[v] = c;
                }
            }
        }
    }
    public long[] getWeights() {
        return cst;
    }
    public long getWeight(int u, int i) {
        return adjCost[u][i];
    }
}