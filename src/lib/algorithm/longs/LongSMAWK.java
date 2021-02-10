package lib.algorithm.longs;

import java.util.ArrayDeque;
import java.util.Arrays;

import lib.util.function.IntToLongBiFunction;

/**
 * @see http://web.cs.unlv.edu/larmore/Courses/CSC477/monge.pdf
 */
public class LongSMAWK {
    final IntToLongBiFunction gen;
    final int[] argmins;

    public LongSMAWK(boolean isMinQuery, int[] rows, int[] cols, IntToLongBiFunction gen) {
        this.gen = isMinQuery ? gen : (i, j) -> ~gen.apply(i, j);
        this.argmins = new int[rows.length];
        solve(rows, cols);
    }

    public LongSMAWK(boolean isMinQuery, int n, int m, IntToLongBiFunction gen) {
        this(isMinQuery, idArray(n), idArray(m), gen);
    }

    public int[] getArgs() {
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
                if (gen.apply(row, col) <= gen.apply(row, lastCol)) {
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
            long min = Long.MAX_VALUE;
            for (int col = left;; col = cols[++colsIdx]) {
                long val = gen.apply(row, col);
                if (val < min) {
                    min = val;
                    argmins[row] = col;
                }
                if (col == right) break;
            }
        }
    }

    private static class SMAWKTest {
        private static final int[][] TEST_DATA = {
            {  25,  21,  13, 10, 20, 13, 19, 35, 37, 41, 58, 66, 82, 99, 124, 133, 156, 178 },
            {  42,  35,  26, 20, 29, 21, 25, 37, 36, 39, 56, 64, 76, 91, 116, 125, 146, 164 },
            {  57,  48,  35, 28, 33, 24, 28, 40, 37, 37, 54, 61, 72, 83, 107, 113, 131, 146 },
            {  78,  65,  51, 42, 44, 35, 38, 48, 42, 42, 55, 61, 70, 80, 100, 106, 120, 135 },
            {  90,  76,  58, 48, 49, 39, 42, 48, 39, 35, 47, 51, 56, 63,  80,  86,  97, 110 },
            { 103,  85,  67, 56, 55, 44, 44, 49, 39, 33, 41, 44, 49, 56,  71,  75,  84,  96 },
            { 123, 105,  86, 75, 73, 59, 57, 62, 51, 44, 50, 52, 55, 59,  72,  74,  80,  92 },
            { 142, 123, 100, 86, 82, 65, 61, 62, 50, 43, 47, 45, 46, 46,  58,  59,  65,  73 },
            { 151, 130, 104, 88, 80, 59, 52, 49, 37, 29, 29, 24, 23, 20,  28,  25,  31,  39 },
        };
        private static final int N = TEST_DATA.length;
        private static final int M = TEST_DATA[0].length;
        private static final IntToLongBiFunction Gen = (i, j) -> TEST_DATA[i][j];

        private static final int[] REDUCED_EXPECTED = { 3, 5, 6, 9, 10, 11, 12, 13, 15 };

        private static final int[] EXPECTED = { 3, 3, 5, 5, 9, 9, 9, 9, 13 };

        public static void main(String[] args) {
            // reduceTest();
            test();
        }

        private static void reduceTest() {
            LongSMAWK smawk = new LongSMAWK(false, N, M, Gen);
            int[] reduced = smawk.reduce(idArray(N), idArray(M));
            if (Arrays.equals(REDUCED_EXPECTED, reduced)) {
                System.out.println("Test passed.");
            } else {
                System.err.println("Test failed:");
                System.err.println("    expected: " + Arrays.toString(REDUCED_EXPECTED));
                System.err.println("      actual: " + Arrays.toString(reduced));
                throw new AssertionError();
            }
        }

        private static void test() {
            int[] answer = new LongSMAWK(false, N, M, Gen).argmins;
            if (Arrays.equals(EXPECTED, answer)) {
                System.out.println("Test passed.");
            } else {
                System.err.println("Test failed:");
                System.err.println("    expected: " + Arrays.toString(EXPECTED));
                System.err.println("      actual: " + Arrays.toString(answer));
                throw new AssertionError();
            }
        }
    }
}
