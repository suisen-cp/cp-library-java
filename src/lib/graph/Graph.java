package lib.graph;
/**
 * @author https://atcoder.jp/users/suisen
 */
public class Graph<Edg extends AbstractEdge> extends AbstractGraph<Edg> {
    public Graph(int n) {
        super(n);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void addEdge(Edg edge) {
        Edg rev = (Edg) edge.reverse();
        adj.get(edge.from).add(edge);
        adj.get(edge.to).add(rev);
        edges.add(edge);
    }
}