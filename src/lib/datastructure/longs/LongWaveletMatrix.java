package lib.datastructure.longs;

import java.util.HashMap;

import lib.datastructure.BitVector;

public class LongWaveletMatrix {
    BitVector[] Matrix;
    int[] ZeroCount;
    HashMap<Long, Integer> IndexMap;
    long[] Sorted;

    LongWaveletMatrix(BitVector[] Matrix, int[] ZeroCount, HashMap<Long, Integer> IndexMap, long[] Sorted) {
        this.Matrix = Matrix;
        this.ZeroCount = ZeroCount;
        this.IndexMap = IndexMap;
        this.Sorted = Sorted;
    }

    public long get(int index) {
        long res = 0;
        for (int i = Matrix.length - 1; i >= 0; i--) {
            long bit = Matrix[i].get(index);
            res |= bit << i;
            if (bit == 0) {
                index = Matrix[i].rank0(index);
            } else {
                index = ZeroCount[i] + Matrix[i].rank1(index);
            }
        }
        return res;
    }

    public int rank(long val, int toIndex) {
        if (!IndexMap.containsKey(val)) return 0;
        int ptr = toIndex;
        for (int i = Matrix.length - 1; i >= 0; i--) {
            long bit = (val >> i) & 1;
            if (bit == 0) {
                ptr = Matrix[i].rank0(ptr);
            } else {
                ptr = ZeroCount[i] + Matrix[i].rank1(ptr);
            }
        }
        return ptr - IndexMap.get(val);
    }

    public int select(long val, int k) {
        if (!IndexMap.containsKey(val)) return -1;
        int ptr = IndexMap.get(val) + k;
        if (Sorted[ptr - 1] != val) return -1;
        for (int i = 0; i < Matrix.length; i++) {
            long bit = (val >> i) & 1;
            if (bit == 0) {
                ptr = Matrix[i].select0(ptr);
            } else {
                ptr = Matrix[i].select1(ptr - ZeroCount[i]);
            }
        }
        return ptr;
    }

    public long quantile(int fromIndex, int toIndex, int k) {
        for (int i = Matrix.length - 1; i >= 0; i--) {
            int leftOnes = Matrix[i].rank1(fromIndex);
            int rightOnes = Matrix[i].rank1(toIndex);
            int zeros = (toIndex - fromIndex) - (rightOnes - leftOnes);
            if (k < zeros) {
                fromIndex = fromIndex - leftOnes;
                toIndex = toIndex - rightOnes;
            } else {
                fromIndex = ZeroCount[i] + leftOnes;
                toIndex = ZeroCount[i] + rightOnes;
                k -= zeros;
            }
        }
        return Sorted[fromIndex];
    }

    public int rankLessThan(long val, int fromIndex, int toIndex) {
        int cnt = 0;
        for (int i = Matrix.length - 1; i >= 0; i--) {
            long bit = (val >> i) & 1;
            int leftOnes = Matrix[i].rank1(fromIndex);
            int rightOnes = Matrix[i].rank1(toIndex);
            int zeros = (toIndex - fromIndex) - (rightOnes - leftOnes);
            if (bit == 0) {
                fromIndex -= leftOnes;
                toIndex -= rightOnes;
            } else {
                fromIndex = ZeroCount[i] + leftOnes;
                toIndex = ZeroCount[i] + rightOnes;
                cnt += zeros;
            }
        }
        return cnt;
    }

    public int rangeFreq(long fromVal, long toVal, int fromIndex, int toIndex) {
        return rankLessThan(toVal, fromIndex, toIndex) - rankLessThan(fromVal, fromIndex, toIndex);
    }
}