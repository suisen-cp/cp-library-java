package lib.graph;
/**
 * @author https://atcoder.jp/users/suisen
 */
public class Digraph<Edg extends AbstractEdge> extends AbstractGraph<Edg> {
    public Digraph(int n) {
        super(n);
    }
    @Override
    public void addEdge(Edg edge) {
        adj.get(edge.from).add(edge);
        edges.add(edge);
    }
}