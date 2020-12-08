import java.util.ArrayDeque;
import java.util.HashSet;

class BreadthFirstSearch {
    public static <T> HashSet<T> reachableNodes(Digraph<T> g, T src) {
        HashSet<T> reachable = new HashSet<>();
        reachable.add(src);
        ArrayDeque<T> dq = new ArrayDeque<>();
        dq.add(src);
        while (dq.size() > 0) {
            T u = dq.pollFirst();
            for (T v : g.getEdges(u)) {
                if (!reachable.contains(v)) {
                    reachable.add(v);
                    dq.addLast(v);
                }
            }
        }
        return reachable;
    }
}
