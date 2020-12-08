package lib.tree;

public class TreeUtil {
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
    public static int[] path(Tree t, int u, int v) {
        int n = t.getV();
        int[] par = new int[n];
        java.util.Arrays.fill(par, -1);
        int[] iter = new int[n];
        int[] stack = new int[n];
        int ptr = 0;
        stack[ptr++] = u;
        while (ptr > 0) {
            int x = stack[ptr - 1];
            int[] e = t.getEdges(x);
            if (iter[x] == e.length) {
                --ptr;
            } else {
                int y = e[iter[x]++];
                if (y == par[x]) continue;
                stack[ptr++] = y;
                par[y] = x;
                if (y == v) break;
            }
        }
        return java.util.Arrays.copyOfRange(stack, 0, ptr);
    }
}