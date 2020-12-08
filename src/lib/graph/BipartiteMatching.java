package lib.graph;

import lib.util.pair.IntPair;

public class BipartiteMatching {
    private final int l;
    private final Digraph<CapEdge> g;
    private final int s, t;

    public BipartiteMatching(int l, int r) {
        this.l = l;
        this.s = l + r;
        this.t = s + 1;
        this.g = new Digraph<>(l + r + 2);
        for (int i = 0; i < l; i++) {
            g.addEdge(new CapEdge(s, i, 1));
        }
        for (int i = 0; i < r; i++) {
            g.addEdge(new CapEdge(l + i, t, 1));
        }
    }

    public void addEdge(int li, int ri) {
        g.addEdge(new CapEdge(li, l + ri, 1));
    }

    public IntPair[] solve() {
        MaxFlow mf = new MaxFlow(g);
        int k = (int) mf.maxFlow(s, t);
        IntPair[] pairs = new IntPair[k];
        int idx = 0;
        for (CapEdge e : g.getEdges()) {
            if (mf.getFlow(e) == 0 || e.from == s || e.to == t) continue;
            pairs[idx++] = new IntPair(e.from, e.to - l);
        }
        return pairs;
    }
}
