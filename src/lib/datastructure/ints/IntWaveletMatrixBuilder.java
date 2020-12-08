package lib.datastructure.ints;

import java.util.Arrays;
import java.util.HashMap;

import lib.datastructure.BitVector;
import lib.datastructure.BitVectorBuilder;

public class IntWaveletMatrixBuilder {
    final int Length;
    final int BitLength;
    final BitVector[] Matrix;
    final int[] ZeroCount;
    final HashMap<Integer, Integer> IndexMap;
    int[] Sorted;

    public IntWaveletMatrixBuilder(int length, int bitLength) {
        this.Length = length;
        this.BitLength = bitLength;
        this.Matrix = new BitVector[BitLength];
        this.ZeroCount = new int[BitLength];
        this.IndexMap = new HashMap<>();
        this.Sorted = new int[Length];
    }

    public void set(int index, int val) {
        if ((val >> BitLength) != 0) {
            throw new IllegalArgumentException();
        }
        Sorted[index] = val;
    }

    public IntWaveletMatrix build() {
        int[] work = new int[Length];
        for (int i = BitLength - 1; i >= 0; i--) {
            BitVectorBuilder builder = new BitVectorBuilder(Length);
            for (int j = 0; j < Length; j++) {
                boolean bit = ((Sorted[j] >> i) & 1) == 1;
                builder.set(j, bit);
                if (!bit) ZeroCount[i]++;
            }
            Matrix[i] = builder.build();
            int[] tmp = Sorted; Sorted = work; work = tmp;
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
        return new IntWaveletMatrix(Matrix, ZeroCount, IndexMap, Sorted);
    }

    public static IntWaveletMatrix fromArray(int[] a, int bitLength) {
        IntWaveletMatrixBuilder builder = new IntWaveletMatrixBuilder(a.length, bitLength);
        for (int i = 0; i < a.length; i++) builder.set(i, a[i]);
        return builder.build();
    }

    public static void main(String[] args) {
        int n = 12;
        int[] a = {5,4,5,5,2,1,5,6,1,3,5,0};
        IntWaveletMatrixBuilder builder = new IntWaveletMatrixBuilder(n, 3);
        for (int i = 0; i < n; i++) {
            builder.set(i, a[i]);
        }
        IntWaveletMatrix m = builder.build();
        System.out.println(Arrays.toString(m.Sorted));
        System.out.println(Arrays.toString(m.ZeroCount));
        for (var e : m.IndexMap.entrySet()) {
            int val = e.getKey();
            int index = e.getValue();
            System.out.printf("%d -> %d\n", val, index);
        }

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
    }
}