package lib.graph;

public final class EdgeWithID extends AbstractEdge {
    public final int id;
    public EdgeWithID(int from, int to, long cost, int id) {
        super(from, to, cost);
        this.id = id;
    }
    public EdgeWithID(int from, int to, int id) {
        this(from, to, 1, id);
    }
    @Override
    public EdgeWithID reverse() {
        return new EdgeWithID(to, from, cost, id);
    }
}
