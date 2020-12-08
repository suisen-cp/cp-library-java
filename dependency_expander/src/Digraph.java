import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class Digraph<T> {
    final HashMap<T, ArrayList<T>> nodes;
    public Digraph() {
        this.nodes = new HashMap<>();
    }
    public Set<T> getNodes() {
        return nodes.keySet();
    }
    public void addEdge(T u, T v) {
        nodes.compute(u, (ky, vl) -> {
            ArrayList<T> l = vl == null ? new ArrayList<>() : vl;
            l.add(v);
            return l;
        });
    }
    public ArrayList<T> getEdges(T u) {
        return nodes.getOrDefault(u, new ArrayList<>(0));
    }
}
