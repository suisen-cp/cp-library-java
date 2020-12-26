package lib.tree;

public class Centroid {
    public static Result getCentroids(Tree t) {
        int g1 = -1, g2 = -1;
        int n = t.getV();
        int h = n >> 1;
        int[] sub = new int[n];
        for (int u : t.postOrder()) {
            boolean ok = true;
            for (int v : t.getEdges(u)) {
                sub[u] += sub[v];
                ok &= sub[v] <= h;
            }
            if (n - ++sub[u] <= h && ok) {
                if (g1 < 0) {
                    g1 = u;
                } else {
                    g2 = u;
                }
            }
        }
        return new Result(sub, g2 < 0 ? new int[]{g1} : new int[]{g1, g2});
    }
    public static final class Result {
        final int[] sub;
        final int[] centroids;
        Result(int[] sub, int[] centroids) {
            this.sub = sub;
            this.centroids = centroids;
        }
        public int[] subtreeSize() {
            return sub;
        }
        public int[] centroids() {
            return centroids;
        }
    }
}
