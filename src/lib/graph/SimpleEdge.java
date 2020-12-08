package lib.graph;

public final class SimpleEdge extends AbstractEdge {
    public SimpleEdge(int from, int to, long cost) {
        super(from, to, cost);
    }
    public SimpleEdge(int from, int to) {
        super(from, to);
    }
    @Override
    public SimpleEdge reverse() {
        return new SimpleEdge(to, from, cost);
    }
}
