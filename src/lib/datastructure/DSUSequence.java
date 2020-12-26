package lib.datastructure;

import java.util.ArrayList;

public class DSUSequence<QueryInfoType> {
    public static class Queries<QueryInfoType> {
        private final int n;
        private final int m;
        private final int[] seq;
        private final int[] inv;
        private final ArrayList<Query<QueryInfoType>> queries;
        Queries(int[] seq, int[] inv, ArrayList<Query<QueryInfoType>> queries) {
            this.n = seq.length;
            this.m = queries.size();
            this.seq = seq;
            this.inv = inv;
            this.queries = queries;
        }

        public int queryNum() {
            return m;
        }

        public ArrayList<Query<QueryInfoType>> getQueries() {
            return queries;
        }

        public Query<QueryInfoType> get(int qi) {
            return queries.get(qi);
        }

        public int[] getSequence() {
            return seq;
        }

        public int[] getSequenceInv() {
            return inv;
        }

        public Events events() {
            int[] cntl = new int[n];
            int[] cntr = new int[n];
            for (int qi = 0; qi < m; qi++) {
                Query<QueryInfoType> query = get(qi);
                int l = query.l, r = query.r;
                cntl[l]++;
                cntr[r]++;
            }
            int[][] beg = new int[n][];
            int[][] end = new int[n][];
            for (int i = 0; i < n; i++) {
                beg[i] = new int[cntl[i]];
                end[i] = new int[cntr[i]];
            }
            for (int qi = 0; qi < m; qi++) {
                Query<QueryInfoType> query = get(qi);
                int l = query.l, r = query.r;
                beg[l][--cntl[l]] = qi;
                end[r][--cntr[r]] = qi;
            }
            return new Events(beg, end);
        }
    }

    public static class Events {
        private final int[][] beg;
        private final int[][] end;
        Events(int[][] beg, int[][] end) {
            this.beg = beg;
            this.end = end;
        }
        public int[] getQueryIndicesLeft(int left) {
            return beg[left];
        }
        public int[] getQueryIndicesRightClosed(int right) {
            return end[right];
        }
        public int[] getQueryIndicesRightOpen(int right) {
            if (right == 0) return new int[]{};
            return end[right - 1];
        }
    }

    public static class Query<QueryInfoType> {
        int l, r;
        final QueryInfoType info;
        Query(int l, int r, QueryInfoType info) {
            this.l = l;
            this.r = r;
            this.info = info;
        }
        public int getLeft() {
            return l;
        }
        public int getRightClosed() {
            return r;
        }
        public int getRightOpen() {
            return r + 1;
        }
        public QueryInfoType getQueryInfo() {
            return info;
        }
    }

    private final int n;
    private final int[] dat;
    private final int[] nxt;
    private final ArrayList<Query<QueryInfoType>> qs = new ArrayList<>();

    public DSUSequence(int n) {
        this.n = n;
        this.dat = new int[n];
        this.nxt = new int[n];
        java.util.Arrays.fill(dat, -1);
        java.util.Arrays.setAll(nxt, i -> i);
    }

    public int root(int x) {
        return dat[x] < 0 ? x : (dat[x] = root(dat[x]));
    }

    public boolean same(int x, int y) {
        return root(x) == root(y);
    }

    public int union(int x, int y) {
        if ((x = root(x)) == (y = root(y))) return x;
        if (-dat[x] < -dat[y]) {int tmp = x; x = y; y = tmp;}
        dat[x] += dat[y];
        int tmp = nxt[x]; nxt[x] = nxt[y]; nxt[y] = tmp;
        return dat[y] = x;
    }

    public int size(int x) {
        return -dat[root(x)];
    }

    public int[] group(int x) {
        int size = size(x);
        int[] group = new int[size];
        for (int i = 0; i < size; i++) {
            group[i] = x;
            x = nxt[x];
        }
        return group;
    }

    public int[][] groups() {
        int groupNum = 0;
        for (int i = 0; i < n; i++) {
            if (dat[i] < 0) {
                groupNum++;
            }
        }
        int[][] groups = new int[groupNum][];
        for (int i = 0, cmp = 0; i < n; i++) {
            if (dat[i] < 0) {
                groups[cmp++] = group(i);
            }
        }
        return groups;
    }

    public void addQuery(int x, QueryInfoType info) {
        int rv = root(x);
        int lv = nxt[rv];
        //noinspection Convert2Diamond
        qs.add(new Query<QueryInfoType>(lv, rv, info));
    }

    public Queries<QueryInfoType> getQueries() {
        int[] inv = new int[n];
        int[] seq = new int[n];
        for (int i = 0, idx = 0; i < n; i++) {
            if (dat[i] < 0) {
                for (int v = nxt[i];; v = nxt[v]) {
                    seq[inv[v] = idx++] = v;
                    if (v == i) break;
                }
            }
        }
        for (Query<QueryInfoType> q : qs) {
            q.l = inv[q.l];
            q.r = inv[q.r];
        }
        //noinspection Convert2Diamond
        return new Queries<QueryInfoType>(seq, inv, qs);
    }
}
