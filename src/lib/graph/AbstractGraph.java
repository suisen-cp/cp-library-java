package lib.graph;

public abstract class AbstractGraph<Edg extends AbstractEdge> {
    final int n;
    final java.util.ArrayList<Edg> edges;
    final java.util.ArrayList<java.util.ArrayList<Edg>> adj;
    public AbstractGraph(int n) {
        this.n = n;
        this.edges = new java.util.ArrayList<>(n);
        this.adj = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new java.util.ArrayList<>());
        }
    }
    public abstract void addEdge(Edg edge);
    public Edg getEdge(int u, int i) {
        return adj.get(u).get(i);
    }
    public java.util.ArrayList<Edg> getEdges(int u) {
        return adj.get(u);
    }
    public java.util.ArrayList<Edg> getEdges() {
        return edges;
    }
    public int deg(int u) {
        return adj.get(u).size();
    }
    public int getV() {
        return n;
    }
    public int getE() {
        return edges.size();
    }
}