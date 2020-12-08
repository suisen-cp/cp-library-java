package lib.datastructure.ints;

import java.util.Arrays;
import java.util.HashMap;

import lib.datastructure.BitVector;
import lib.datastructure.BitVectorBuilder;
import lib.util.pair.IntPair3;

public class IntWaveletMatrix {
    int BitLength;
    int MaxValue;
    BitVector[] Matrix;
    int[] ZeroCount;
    HashMap<Integer, Integer> IndexMap;
    int[] Sorted;

    private IntWaveletMatrix() {}

    IntWaveletMatrix(BitVector[] Matrix, int[] ZeroCount, HashMap<Integer, Integer> IndexMap, int[] Sorted) {
        this.BitLength = Matrix.length;
        this.MaxValue = (1 << this.BitLength) - 1;
        this.Matrix = Matrix;
        this.ZeroCount = ZeroCount;
        this.IndexMap = IndexMap;
        this.Sorted = Sorted;
    }

    public int get(int index) {
        int res = 0;
        for (int i = BitLength - 1; i >= 0; i--) {
            int bit = Matrix[i].get(index);
            res |= bit << i;
            if (bit == 0) {
                index = Matrix[i].rank0(index);
            } else {
                index = ZeroCount[i] + Matrix[i].rank1(index);
            }
        }
        return res;
    }

    public int rank(int val, int toIndex) {
        if (!IndexMap.containsKey(val)) return 0;
        int ptr = toIndex;
        for (int i = BitLength - 1; i >= 0; i--) {
            int bit = (val >> i) & 1;
            if (bit == 0) {
                ptr = Matrix[i].rank0(ptr);
            } else {
                ptr = ZeroCount[i] + Matrix[i].rank1(ptr);
            }
        }
        return ptr - IndexMap.get(val);
    }

    public int rank(int val, int fromIndex, int toIndex) {
        return rank(val, toIndex) - rank(val, fromIndex);
    }

    public int select(int val, int k) {
        if (!IndexMap.containsKey(val)) return -1;
        int ptr = IndexMap.get(val) + k;
        if (Sorted[ptr - 1] != val) return -1;
        for (int i = 0; i < BitLength; i++) {
            int bit = (val >> i) & 1;
            if (bit == 0) {
                ptr = Matrix[i].select0(ptr);
            } else {
                ptr = Matrix[i].select1(ptr - ZeroCount[i]);
            }
        }
        return ptr;
    }

    public int quantile(int fromIndex, int toIndex, int k) {
        for (int i = BitLength - 1; i >= 0; i--) {
            int leftOnes = Matrix[i].rank1(fromIndex);
            int rightOnes = Matrix[i].rank1(toIndex);
            int zeros = (toIndex - fromIndex) - (rightOnes - leftOnes);
            if (k < zeros) {
                fromIndex -= leftOnes;
                toIndex -= rightOnes;
            } else {
                fromIndex = ZeroCount[i] + leftOnes;
                toIndex = ZeroCount[i] + rightOnes;
                k -= zeros;
            }
        }
        return Sorted[fromIndex];
    }

    public int rankLessThan(int val, int fromIndex, int toIndex) {
        int cnt = 0;
        for (int i = BitLength - 1; i >= 0; i--) {
            int bit = (val >> i) & 1;
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

    public IntPair3 rankAll(int val, int fromIndex, int toIndex) {
        int allCnt = toIndex - fromIndex;
        int ltCnt = 0;
        int gtCnt = 0;
        for (int i = BitLength - 1; i >= 0; i--) {
            int bit = (val >> i) & 1;
            int leftOnes = Matrix[i].rank1(fromIndex);
            int rightOnes = Matrix[i].rank1(toIndex);
            int ones = rightOnes - leftOnes;
            int zeros = (toIndex - fromIndex) - ones;
            if (bit == 0) {
                gtCnt += ones;
                fromIndex -= leftOnes;
                toIndex -= rightOnes;
            } else {
                ltCnt += zeros;
                fromIndex = ZeroCount[i] + leftOnes;
                toIndex = ZeroCount[i] + rightOnes;
            }
        }
        return new IntPair3(ltCnt, allCnt - ltCnt - gtCnt, gtCnt);
    }

    public int rangeFreq(int fromVal, int toVal, int fromIndex, int toIndex) {
        return rankLessThan(toVal, fromIndex, toIndex) - rankLessThan(fromVal, fromIndex, toIndex);
    }

    public int nextValue(int val, int fromIndex, int toIndex) {
        boolean eq = true;
        int eqFromIndex = fromIndex;
        int eqToIndex = toIndex;
        int gtFromIndex = -1;
        int gtToIndex = -1;
        int eqval = 0;
        int res = -1;
        for (int i = BitLength - 1; i >= 0; i--) {
            int bit = (val >> i) & 1;
            if (res >= 0) {
                int gtLeftOnes = Matrix[i].rank1(gtFromIndex);
                int gtRightOnes = Matrix[i].rank1(gtToIndex);
                int gtOnes = gtRightOnes - gtLeftOnes;
                int gtZeros = (gtToIndex - gtFromIndex) - gtOnes;
                if (gtZeros != 0) {
                    gtFromIndex -= gtLeftOnes;
                    gtToIndex -= gtRightOnes;
                } else {
                    gtFromIndex = ZeroCount[i] + gtLeftOnes;
                    gtToIndex = ZeroCount[i] + gtRightOnes;
                    res |= 1 << i;
                }
            }
            if (eq) {
                int eqLeftOnes = Matrix[i].rank1(eqFromIndex);
                int eqRightOnes = Matrix[i].rank1(eqToIndex);
                int eqOnes = eqRightOnes - eqLeftOnes;
                int eqZeros = (eqToIndex - eqFromIndex) - eqOnes;
                if (bit == 0) {
                    if (eqOnes != 0) {
                        gtFromIndex = ZeroCount[i] + eqLeftOnes;
                        gtToIndex = ZeroCount[i] + eqRightOnes;
                        res = eqval | 1 << i;
                    }
                    if (eqZeros != 0) {
                        eqFromIndex -= eqLeftOnes;
                        eqToIndex -= eqRightOnes;
                    } else {
                        eq = false;
                    }
                } else {
                    if (eqOnes != 0) {
                        eqFromIndex = ZeroCount[i] + eqLeftOnes;
                        eqToIndex = ZeroCount[i] + eqRightOnes;
                        eqval |= 1 << i;
                    } else {
                        eq = false;
                    }
                }
            }
        }
        if (eq) return val;
        if (res >= 0) return res;
        return -1;
    }

    public int prevValue(int val, int fromIndex, int toIndex) {
        boolean eq = true;
        int eqFromIndex = fromIndex;
        int eqToIndex = toIndex;
        int ltFromIndex = -1;
        int ltToIndex = -1;
        int eqval = 0;
        int res = -1;
        for (int i = BitLength - 1; i >= 0; i--) {
            int bit = (val >> i) & 1;
            if (res >= 0) {
                int ltLeftOnes = Matrix[i].rank1(ltFromIndex);
                int ltRightOnes = Matrix[i].rank1(ltToIndex);
                int ltOnes = ltRightOnes - ltLeftOnes;
                if (ltOnes != 0) {
                    ltFromIndex = ZeroCount[i] + ltLeftOnes;
                    ltToIndex = ZeroCount[i] + ltRightOnes;
                    res |= 1 << i;
                } else {
                    ltFromIndex -= ltLeftOnes;
                    ltToIndex -= ltRightOnes;
                }
            }
            if (eq) {
                int eqLeftOnes = Matrix[i].rank1(eqFromIndex);
                int eqRightOnes = Matrix[i].rank1(eqToIndex);
                int eqOnes = eqRightOnes - eqLeftOnes;
                int eqZeros = (eqToIndex - eqFromIndex) - eqOnes;
                if (bit == 0) {
                    if (eqZeros != 0) {
                        eqFromIndex -= eqLeftOnes;
                        eqToIndex -= eqRightOnes;
                    } else {
                        eq = false;
                    }
                } else {
                    if (eqZeros != 0) {
                        ltFromIndex = eqFromIndex - eqLeftOnes;
                        ltToIndex = eqToIndex - eqRightOnes;
                        res = eqval;
                    }
                    if (eqOnes != 0) {
                        eqFromIndex = ZeroCount[i] + eqLeftOnes;
                        eqToIndex = ZeroCount[i] + eqRightOnes;
                        eqval |= 1 << i;
                    } else {
                        eq = false;
                    }
                }
            }
        }
        if (res >= 0) return res;
        return -1;
    }
    
    public int rangeMax(int fromIndex, int toIndex) {
        if (rank(MaxValue, fromIndex, toIndex) > 0) return MaxValue;
        return prevValue(MaxValue, fromIndex, toIndex);
    }

    public int rangeMin(int fromIndex, int toIndex) {
        return nextValue(0, fromIndex, toIndex);
    }

    public static void main(String[] args) {
        int n = 12;
        int[] a = {5,4,5,5,2,1,5,6,1,3,5,0};
        IntWaveletMatrix m = new IntWaveletMatrix();
        m.BitLength = 3;
        m.MaxValue = 7;
        m.Matrix = new BitVector[3];
        m.Matrix[0] = BitVectorBuilder.fromBinaryString("110101111010");
        m.Matrix[1] = BitVectorBuilder.fromBinaryString("100100000010");
        m.Matrix[2] = BitVectorBuilder.fromBinaryString("111100110010");
        m.ZeroCount = new int[]{4,9,5};
        m.IndexMap = new HashMap<>();
        m.IndexMap.put(0, 0); m.IndexMap.put(1, 4);
        m.IndexMap.put(2, 2); m.IndexMap.put(3, 11);
        m.IndexMap.put(4, 1); m.IndexMap.put(5, 6);
        m.IndexMap.put(6, 3);
        m.Sorted = new int[]{0,4,2,6,1,1,5,5,5,5,5,3};
        int[] rankAll = {1,2,1,1,1,5,1};

        GetTest: {
            for (int i = 0; i < n; i++) {
                int get = m.get(i);
                if (get != a[i]) {
                    System.out.printf("Error in get(i = %d)\n", i);
                }
            }
        }
        RankTest: {
            for (int v = 0; v <= 7; v++) {
                for (int i = 0; i <= n; i++) {
                    int rank = m.rank(v, i);
                    int cnt = 0;
                    for (int j = 0; j < i; j++) if (a[j] == v) cnt++;
                    if (rank != cnt) {
                        System.out.printf("Error in rank(v = %d, i = %d)\n", v, i);
                    }
                }
            }
        }
        SelectTest: {
            for (int v = 0; v <= 6; v++) {
                for (int k = 1; k <= rankAll[v]; k++) {
                    int select = m.select(v, k);
                    int rank = m.rank(v, select);
                    if (k != rank) {
                        System.out.printf("Error in select(v = %d, k = %d)\n", v, k);
                        System.out.printf("\texpected: %d\n", k);
                        System.out.printf("\tactual  : %d\n", rank);
                    }
                }
            }
        }
        QuantileTest: {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    for (int k = 0; k < j - i; k++) {
                        int quantile = m.quantile(i, j, k);
                        int[] cop = Arrays.copyOfRange(a, i, j);
                        Arrays.sort(cop);
                        int cor = cop[k];
                        if (quantile != cor) {
                            System.out.printf("Error in quantile(i = %d, j = %d, k = %d)\n", i, j, k);
                            System.out.printf("\texpected: %d\n", cor);
                            System.out.printf("\tactual  : %d\n", quantile);
                        }
                    }
                }
            }
        }
        RankLessThanTest: {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    for (int v = 0; v <= 6; v++) {
                        int rankLessThan = m.rankLessThan(v, i, j);
                        int cnt = 0;
                        for (int k = i; k < j; k++) {
                            if (a[k] < v) cnt++;
                        }
                        if (rankLessThan != cnt) {
                            System.out.printf("Error in rankLessThan(v = %d, i = %d, j = %d)\n", v, i, j);
                            System.out.printf("\texpected: %d\n", cnt);
                            System.out.printf("\tactual  : %d\n", rankLessThan);
                        }
                    }
                }
            }
        }
        nextValueTest: {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    for (int v = 0; v <= 6; v++) {
                        int nextValue = m.nextValue(v, i, j);
                        int cor = -1;
                        for (int k = i; k < j; k++) {
                            if (a[k] >= v) {
                                if (cor < 0) {
                                    cor = a[k];
                                } else {
                                    cor = Math.min(cor, a[k]);
                                }
                            }
                        }
                        if (nextValue != cor) {
                            System.out.printf("Error in nextValue(v = %d, i = %d, j = %d)\n", v, i, j);
                            System.out.printf("\texpected: %d\n", cor);
                            System.out.printf("\tactual  : %d\n", nextValue);
                        }
                    }
                }
            }
        }
        prevValueTest: {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    for (int v = 1; v <= 7; v++) {
                        int prevValue = m.prevValue(v, i, j);
                        int cor = -1;
                        for (int k = i; k < j; k++) {
                            if (a[k] < v) {
                                if (cor < 0) {
                                    cor = a[k];
                                } else {
                                    cor = Math.max(cor, a[k]);
                                }
                            }
                        }
                        if (prevValue != cor) {
                            System.out.printf("Error in prevValue(v = %d, i = %d, j = %d)\n", v, i, j);
                            System.out.printf("\texpected: %d\n", cor);
                            System.out.printf("\tactual  : %d\n", prevValue);
                        }
                    }
                }
            }
        }
        rangeMaxTest: {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    int rangeMax = m.rangeMax(i, j);
                    int max = 0;
                    for (int k = i; k < j; k++) {
                        max = Math.max(max, a[k]);
                    }
                    if (rangeMax != max) {
                        System.out.printf("Error in rangeMax(i = %d, j = %d)\n", i, j);
                        System.out.printf("\texpected: %d\n", max);
                        System.out.printf("\tactual  : %d\n", rangeMax);
                    }
                }
            }
        }
        rangeMinTest: {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j <= n; j++) {
                    int rangeMin = m.rangeMin(i, j);
                    int min = 7;
                    for (int k = i; k < j; k++) {
                        min = Math.min(min, a[k]);
                    }
                    if (rangeMin != min) {
                        System.out.printf("Error in rangeMin(i = %d, j = %d)\n", i, j);
                        System.out.printf("\texpected: %d\n", min);
                        System.out.printf("\tactual  : %d\n", rangeMin);
                    }
                }
            }
        }
    }
}