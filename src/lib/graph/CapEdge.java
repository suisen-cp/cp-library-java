package lib.graph;

public class CapEdge extends AbstractEdge {
    public long cap;
    public int rev;
    public CapEdge(int from, int to, long cap, long cost) {
        super(from, to, cost);
        this.cap = cap;
    }
    public CapEdge(int from, int to, long cap) {
        this(from, to, cap, 1);
    }
    @Override
    public final AbstractEdge reverse() {
        throw new UnsupportedOperationException();
    }
}
