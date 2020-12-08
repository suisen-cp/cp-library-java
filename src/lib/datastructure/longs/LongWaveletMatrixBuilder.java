package lib.datastructure.longs;

import java.util.HashMap;

import lib.datastructure.BitVector;
import lib.datastructure.BitVectorBuilder;

public class LongWaveletMatrixBuilder {
    final int Length;
    final int BitLength;
    final BitVector[] Matrix;
    final int[] ZeroCount;
    final HashMap<Long, Integer> IndexMap;
    long[] Sorted;

    public LongWaveletMatrixBuilder(int length, int bitLength) {
        this.Length = length;
        this.BitLength = bitLength;
        this.Matrix = new BitVector[BitLength];
        this.ZeroCount = new int[BitLength];
        this.IndexMap = new HashMap<>();
        this.Sorted = new long[Length];
    }

    public void set(int index, long val) {
        if ((val >> BitLength) != 0) {
            throw new IllegalArgumentException();
        }
        Sorted[index] = val;
    }

    public LongWaveletMatrix build() {
        long[] work = new long[Length];
        for (int i = BitLength - 1; i >= 0; i--) {
            BitVectorBuilder builder = new BitVectorBuilder(Length);
            for (int j = 0; j < Length; j++) {
                boolean bit = ((Sorted[j] >> i) & 1) == 1;
                builder.set(j, bit);
                if (!bit) ZeroCount[i]++;
            }
            Matrix[i] = builder.build();
            long[] tmp = Sorted; Sorted = work; work = tmp;
            int zeroIndex = 0, oneIndex = ZeroCount[i];
            for (int j = 0; j < Length; j++) {
                boolean bit = ((work[j] >> i) & 1) == 1;
                if (bit) {
                    Sorted[oneIndex++] = work[j];
                } else {
                    Sorted[zeroIndex++] = work[j];
                }
            }
        }
        for (int i = 0; i < Length; i++) {
            if (i == 0 || Sorted[i] != Sorted[i - 1]) {
                IndexMap.put(Sorted[i], i);
            }
        }
        return new LongWaveletMatrix(Matrix, ZeroCount, IndexMap, Sorted);
    }

    public static LongWaveletMatrix fromArray(long[] a, int bitLength) {
        LongWaveletMatrixBuilder builder = new LongWaveletMatrixBuilder(a.length, bitLength);
        for (int i = 0; i < a.length; i++) builder.set(i, a[i]);
        return builder.build();
    }
}