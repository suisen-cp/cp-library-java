package lib.tree;

public class WeightedForest extends Forest {
    final long[] cst;
    final long[][] adjCost;

    WeightedForest(int n, int[][] adj, int[][] groups, int[] ids, int[] idx, int[] root, long[][] adjCost) {
        super(n, adj, groups, ids, idx, root);
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
