package lib.tree;

import java.util.Arrays;

public class Forest {
    final int n;
    final int groupNum;
    final int[][] adj;
    final int[] ids;
    final int[] idx;
    final int[] par;
    final int[][] groups;
    final int[] roots;
    final int[][] pre;
    final int[][] pst;
    Forest(int n, int[][] adj, int[][] groups, int[] ids, int[] idx, int[] root) {
        this.n = n;
        this.groupNum = groups.length;
        this.adj = adj;
        this.ids = ids;
        this.idx = idx;
        this.par = new int[n];
        this.groups = groups;
        this.roots = new int[groupNum];
        this.pre = new int[groupNum][];
        this.pst = new int[groupNum][];
        build(root);
    }
    private void build(int[] root) {
        int[] stack = new int[n << 1];
        Arrays.fill(par, -1);
        for (int i = 0; i < groupNum; i++) {
            int size = groups[i].length;
            int head = groups[i][0];
            roots[i] = root[head] < 0 ? head : root[head];
            int[] pre_i = pre[i] = new int[size];
            int[] pst_i = pst[i] = new int[size];
            int preOrd = 0, pstOrd = 0;
            int ptr = 0;
            stack[ptr++] = ~roots[i];
            stack[ptr++] =  roots[i];
            while (ptr > 0) {
                int u = stack[--ptr];
                if (u >= 0) {
                    pre_i[preOrd++] = u;
                    for (int v : adj[u]) {
                        if (v == par[u]) continue;
                        par[v] = u;
                        stack[ptr++] = ~v;
                        stack[ptr++] =  v;
                    }
                } else {
                    pst_i[pstOrd++] = ~u;
                }
            }
        }
    }
    public final int getV() {
        return n;
    }
    public final int groupNum() {
        return groupNum;
    }
    public final int[] edges(int u) {
        return adj[u];
    }
    public final int parent(int u) {
        return par[u];
    }
    public final int[] parent() {
        return par;
    }
    public final int[] ids() {
        return ids;
    }
    public final int[] indices() {
        return idx;
    }
    public final int id(int u) {
        return ids[u];
    }
    public final int index(int u) {
        return idx[u];
    }
    public final int[] group(int id) {
        return groups[id];
    }
    public final int get(int id, int index) {
        return groups[id][index];
    }
    public final int root(int id) {
        return roots[id];
    }
    public final int size(int id) {
        return groups[id].length;
    }
    public final int[] preOrder(int id) {
        return pre[id];
    }
    public final int[] postOrder(int id) {
        return pst[id];
    }
    public final Tree[] toTrees() {
        Tree[] ts = new Tree[groupNum];
        for (int id = 0; id < groupNum; id++) {
            int root = root(id);
            TreeBuilder tb = new TreeBuilder(size(id), idx[root]);
            for (int u : group(id)) {
                if (u != root) {
                    tb.addEdge(idx[u], idx[par[u]]);
                }
            }
            ts[id] = tb.build();
        }
        return ts;
    }
}
