package lib.graph;

import java.util.Arrays;

public class GraphUtil {
    public static int[] getCycle(Digraph<? extends AbstractEdge> g) {
        int n = g.getV();
        int[] p = new int[n];
        int[] iter = new int[n];
        int[] stack = new int[n];
        int ptr = 0;
        for (int u = 0; u < n; u++) {
            if (iter[u] == g.deg(u)) continue;
            stack[p[u] = ptr++] = u;
            while (ptr > 0) {
                int x = stack[ptr - 1];
                if (iter[x] == g.deg(x)) {
                    --ptr;
                } else {
                    int y = g.getEdge(x, iter[x]++).to;
                    if (p[y] < ptr && stack[p[y]] == y) {
                        return Arrays.copyOfRange(stack, p[y], ptr);
                    }
                    stack[p[y] = ptr++] = y;
                }
            }
        }
        return null;
    }
    public static int[] getCycle(Graph<? extends AbstractEdge> g) {
        int n = g.getV();
        AbstractEdge[] par = new AbstractEdge[n];
        int[] p = new int[n];
        int[] iter = new int[n];
        int[] stack = new int[n];
        int ptr = 0;
        for (int u = 0; u < n; u++) {
            if (iter[u] == g.deg(u)) continue;
            stack[p[u] = ptr++] = u;
            while (ptr > 0) {
                int x = stack[ptr - 1];
                if (iter[x] == g.deg(x)) {
                    --ptr;
                } else {
                    AbstractEdge e = g.getEdge(x, iter[x]++);
                    if (e == par[x]) continue;
                    int y = e.to;
                    if (par[y] != null || y == u) {
                        return Arrays.copyOfRange(stack, p[y], ptr);
                    }
                    par[y] = e;
                    stack[p[y] = ptr++] = y;
                }
            }
        }
        return null;
    }
}
