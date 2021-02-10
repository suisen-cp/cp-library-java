package lib.algorithm;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;

import lib.util.function.IntBiFunction;

public class SMAWK<T> {
    final Comparator<T> comparator;
    final IntBiFunction<T> gen;
    final int[] argmins;

    public SMAWK(boolean isMinQuery, int[] rows, int[] cols, IntBiFunction<T> gen, Comparator<T> comparator) {
        this.comparator = isMinQuery ? comparator : comparator.reversed();
        this.gen = gen;
        this.argmins = new int[rows.length];
        solve(rows, cols);
    }

    public SMAWK(boolean isMinQuery, int n, int m, IntBiFunction<T> gen, Comparator<T> comparator) {
        this(isMinQuery, idArray(n), idArray(m), gen, comparator);
    }

    public int[] get() {
        return argmins;
    }

    private static int[] idArray(int n) {
        int[] ids = new int[n];
        Arrays.setAll(ids, i -> i);
        return ids;
    }

    private static int[] oddIndices(int[] rows) {
        int n = rows.length >> 1;
        int[] odd = new int[n];
        for (int i = 0; i < n; i++) odd[i] = rows[i << 1 | 1];
        return odd;
    }

    private void solve(int[] rows, int[] cols) {
        cols = reduce(rows, cols);
        if (cols.length == 1) {
            int col = cols[0];
            for (int row : rows) argmins[row] = col;
            return;
        }
        solve(oddIndices(rows), cols);
        interpolate(rows, cols);
    }

    private static class Column {
        final int col, head;
        Column(int col, int head) {
            this.col = col;
            this.head = head;
        }
    }

    private int[] reduce(int[] rows, int[] cols) {
        ArrayDeque<Column> survivingColsStack = new ArrayDeque<>(cols.length);
        for (int col : cols) {
            while (survivingColsStack.size() > 0) {
                Column last = survivingColsStack.getLast();
                int lastCol = last.col;
                int i = last.head;
                int row = rows[i];
                if (comparator.compare(gen.apply(row, col), gen.apply(row, lastCol)) <= 0) {
                    survivingColsStack.removeLast();
                } else {
                    if (i + 1 < rows.length) {
                        survivingColsStack.addLast(new Column(col, i + 1));
                    }
                    break;
                }
            }
            if (survivingColsStack.size() == 0) {
                survivingColsStack.addLast(new Column(col, 0));
            }
        }
        int reducedColsNum = survivingColsStack.size();
        int[] reduced = new int[reducedColsNum];
        while (reducedColsNum > 0) {
            reduced[--reducedColsNum] = survivingColsStack.removeLast().col;
        }
        return reduced;
    }

    private void interpolate(int[] rows, int[] cols) {
        int h = rows.length;
        int maxCol = cols[cols.length - 1];
        for (int i = 0, colsIdx = 0; i < h; i += 2) {
            int left = cols[colsIdx];
            int right = i + 1 < h ? argmins[rows[i + 1]] : maxCol;
            int row = rows[i];
            T min = null;
            for (int col = left;; col = cols[++colsIdx]) {
                T val = gen.apply(row, col);
                if (min == null || comparator.compare(val, min) < 0) {
                    min = val;
                    argmins[row] = col;
                }
                if (col == right) break;
            }
        }
    }
}
