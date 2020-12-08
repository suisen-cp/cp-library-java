package lib.graph;

import java.util.Arrays;

import lib.base.Const;
import lib.util.collections.ints.IntArrayList;

/**
 * SSSP (Single Sourse Shortest Path) problem solver, implementing Dijkstra's
 * algorithm.
 * 
 * @author https://atcoder.jp/users/suisen
 */
public final class Dijkstra2 {
    /**
     * UNREACHABLE means there does not exist any path from s to t.
     */
    public static final long UNREACHABLE = Const.LINF;

    private final int n;
    private final int s;
    private final long[] dist;
    private final int[] prev;

    /**
     * Constructor. Class Dijkstra is not instantiable.
     */
    public Dijkstra2(long[][] g, int s) {
        this.n = g.length;
        this.s = s;
        this.dist = new long[n];
        this.prev = new int[n];
        solve(g);
    }


    /**
     * ************ costs must be non-negative integers *************** 
     * * Solving SSSP (Single Sourse Shortest Path) problem in O(V^2).*
     * ****************************************************************
     * 
     * @param edge edge[i][j] contains the cost of edge i -> j.
     */
    private void solve(long[][] edge) {
        Arrays.fill(dist, UNREACHABLE);
        dist[s] = 0;
        boolean[] settled = new boolean[n];
        while (true) {
            int u = -1;
            long min = UNREACHABLE;
            for (int i = 0; i < n; i++)
                if (dist[i] < min && !settled[i]) {
                    u = i;
                    min = dist[i];
                }
            if (u < 0)
                break;
            settled[u] = true;
            for (int v = 0; v < n; v++) {
                if (edge[u][v] >= 0 && dist[u] + edge[u][v] < dist[v]) {
                    dist[v] = dist[u] + edge[u][v];
                    prev[v] = u;
                }
            }
        }
    }

    /**
     * return if there exists a path from s to t.
     * 
     * @param t target vertex.
     * @return if there exists a path from s to t then true else false.
     */
    public boolean reachable(final int t) {
        return dist[t] != UNREACHABLE;
    }

    /**
     * return if there does NOT exist a path from s to t.
     * 
     * @param t target vertex.
     * @return if there does NOT exist a path from s to t then true else false.
     */
    public boolean unreachable(final int t) {
        return dist[t] == UNREACHABLE;
    }

    /**
     * return the minimum cost from s to t. if there does not exist any path,
     * returns {@code Dijkstra.UNREACHABLE = Const.LONG}.
     * 
     * @param t target vertex.
     * @return if there exists paths from s to t then the minimum cost of them else
     *         {@code Dijkstra.UNREACHABLE = Const.LONG}.
     */
    public long getDistance(int t) {
        return dist[t];
    }

    /**
     * return the minimum costs from s to 0, 1, ..., N-1.
     * 
     * @return the array that contains the minimum costs from s. if there does not
     *         exist paths from s to t then
     *         {@code dist[t] = Dijkstra.UNREACHABLE = Const.LONG}.
     */
    public long[] getDistances() {
        return dist;
    }

    /**
     * return one of the shortest paths from s to t. Let l be the size of the array
     * {@code path}, then {@code path[0] = s, ..., path[l - 1] = t}.
     * 
     * @param t target vertex.
     * @return one of the shortest paths from s to t.
     */
    public int[] path(final int t) {
        final IntArrayList rev = new IntArrayList();
        for (int v = t; v != s; v = prev[v]) rev.add(v);
        rev.add(s);
        rev.reverse();
        return rev.toArray();
    }
}