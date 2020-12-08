package lib.tree;
/**
 * @author https://atcoder.jp/users/suisen
 */
public class Tree {
    final int n;
    final int root;
    final int[][] adj;
    final int[] par;
    final int[] pre;
    final int[] pst;
    Tree(int n, int root, int[][] adj) {
        this.n = n;
        this.adj = adj;
        this.root = root;
        this.par = new int[n];
        this.pre = new int[n];
        this.pst = new int[n];
        build();
    }
    private void build() {
        int preOrd = 0, pstOrd = 0;
        java.util.Arrays.fill(par, -1);
        int[] stack = new int[n << 1];
        int ptr = 0;
        stack[ptr++] = ~root;
        stack[ptr++] =  root;
        while (ptr > 0) {
            int u = stack[--ptr];
            if (u >= 0) {
                pre[preOrd++] = u;
                for (int v : adj[u]) {
                    if (v == par[u]) continue;
                    par[v] = u;
                    stack[ptr++] = ~v;
                    stack[ptr++] =  v;
                }
            } else {
                pst[pstOrd++] = ~u;
            }
        }
    }
    public int getV() {
        return n;
    }
    public int getRoot() {
        return root;
    }
    public int[] getEdges(int u) {
        return adj[u];
    }
    public int[] parent() {
        return par;
    }
    public int[] preOrder() {
        return pre;
    }
    public int[] postOrder() {
        return pst;
    }
}