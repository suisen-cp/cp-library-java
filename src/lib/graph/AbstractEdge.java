package lib.graph;

public abstract class AbstractEdge implements Comparable<AbstractEdge> {
    public final int from, to;
    public final long cost;
    public AbstractEdge(int from, int to, long cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }
    public AbstractEdge(int from, int to) {
        this(from, to, 1L);
    }
    public abstract AbstractEdge reverse();
    public int compareTo(AbstractEdge o) {
        return Long.compare(cost, o.cost);
    }
}
