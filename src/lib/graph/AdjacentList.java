package lib.graph;

import lib.util.IterableIterator;

import java.util.Iterator;

public class AdjacentList<T extends AbstractEdge> {
    public final boolean directed;
    public final int V;
    public final int E;
    private final int[] latest;
    private final int[] prev;
    private final T[] edge;
    int edgeCount;

    @SuppressWarnings("unchecked")
    public AdjacentList(int n, int m, boolean directed) {
        this.directed = directed;
        this.V = n;
        this.E = directed ? m : m << 1;
        this.latest = new int[V + 1];
        this.prev = new int[E + 1];
        this.edge = (T[]) new AbstractEdge[E + 1];
    }

    @SuppressWarnings("unchecked")
    public void addEdge(T edge) {
        addDirectedEdge(edge);
        if (!directed) {
            addDirectedEdge((T) edge.reverse());
        }
    }

    private void addDirectedEdge(T e) {
        edgeCount++;
        prev[edgeCount] = latest[e.from];
        latest[e.from] = edgeCount;
        edge[edgeCount] = e;
    }

    public IterableIterator<T> getEdges(int u) {
        return new EdgeIterator(u);
    }

    // public void dfs(int s, Consumer<T> pre, Consumer<T> pst) {
    //     boolean[] vis = new boolean[V];
    //     int[] stack = new int[V << 1];
    //     int ptr = 0;
    //     stack[ptr++] = ~s;
    //     stack[ptr++] =  s;
    //     while (ptr > 0) {
    //         int u = stack[--ptr];
    //         if (u >= 0) {

    //         } else {

    //         }
    //     }
    // }

    private final class EdgeIterator implements IterableIterator<T> {
        int edgeId;
        EdgeIterator(int u) {
            this.edgeId = latest[u];
        }
        @Override
        public boolean hasNext() {
            return edgeId > 0;
        }
        @Override
        public T next() {
            T ret = edge[edgeId];
            edgeId = prev[edgeId];
            return ret;
        }
        @Override
        public Iterator<T> iterator() {
            return this;
        }
    }
}
