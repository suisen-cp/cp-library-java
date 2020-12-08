package lib.algorithm;

import lib.algorithm.ints.IntCompress;
import lib.algorithm.longs.LongCompress;
import lib.datastructure.ints.IntBinaryIndexedTree;

import java.util.Arrays;

public final class InversionNumber {
    public static long solve(int[] a) {
        return solveCompressed(new IntCompress(a, false).compressed());
    }
    public static long solve(long[] a) {
        return solveCompressed(new LongCompress(a, false).compressed());
    }
    public static long solveCompressed(int[] a) {
        if (a.length == 0) return 0;
        long res = 0;
        int max = Arrays.stream(a).max().getAsInt();
        IntBinaryIndexedTree bit = new IntBinaryIndexedTree(max + 1);
        for (final int j : a) {
            res += bit.sum(j + 1, max + 1);
            bit.add(j, 1);
        }
        return res;
    }
}
