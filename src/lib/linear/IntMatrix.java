package lib.linear;

import lib.base.ArithmeticOperations;

public class IntMatrix implements ArithmeticOperations<IntMatrix> {
    final int N, M;
    final int[][] A;
    public IntMatrix(int n, int m) {
        this.N = n;
        this.M = m;
        this.A = new int[N][M];
    }
    public IntMatrix(int[][] A) {
        this.N = A.length;
        this.M = A[0].length;
        this.A = new int[N][M];
        for (int i = 0; i < N; i++) this.A[i] = A[i].clone();
    }
    public int get(int i, int j) {
        return A[i][j];
    }
    public void set(int i, int j, int val) {
        A[i][j] = val;
    }
    public IntMatrix add(IntMatrix B) {
        return add(this, B);
    }
    public IntMatrix sub(IntMatrix B) {
        return sub(this, B);
    }
    public IntMatrix mul(IntMatrix B) {
        return mul(this, B);
    }
    public IntMatrix div(IntMatrix B) {
        throw new UnsupportedOperationException();
    }
    public IntMatrix pow(long n) {
        return pow(this, n);
    }
    public int[] mul(int[] x) {
        return mul(this, x);
    }

    public int[][] copyOfRawMatrix() {
        int[][] ret = new int[N][M];
        for (int i = 0; i < N; i++) ret[i] = A[i].clone();
        return ret;
    }
    public int[][] getRawMatrix() {
        return A;
    }

    public static IntMatrix createE0(int n, int m) {
        return new IntMatrix(rawE0(n, m));
    }
    public static IntMatrix createE0(int n) {
        return new IntMatrix(rawE0(n));
    }
    public static IntMatrix createE1(int n) {
        return new IntMatrix(rawE1(n));
    }
    public static IntMatrix create(int[][] A) {
        return new IntMatrix(A);
    }
    public static IntMatrix add(IntMatrix A, IntMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        int[][] c = new int[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] + B.A[i][j];
        }
        return new IntMatrix(c);
    }
    public static IntMatrix sub(IntMatrix A, IntMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        int[][] c = new int[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] - B.A[i][j];
        }
        return new IntMatrix(c);
    }
    public static IntMatrix mul(IntMatrix A, IntMatrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        int[][] c = new int[N][L];
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] += A.A[i][k] * B.A[k][j];
        }
        return new IntMatrix(c);
    }
    public static IntMatrix pow(IntMatrix A, long n) {
        IntMatrix ret = createE1(A.N);
        IntMatrix pow = A;
        for (long c = 1; n > 0;) {
            long lsb = n & -n;
            while (lsb != c) {
                c <<= 1;
                pow = mul(pow, pow);
            }
            ret = mul(ret, pow);
            n ^= lsb;
        }
        return ret;
    }
    public static int[] mul(IntMatrix A, int[] x) {
        dimensionCheck(A.M, x.length);
        int[] b = new int[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] += A.A[i][j] * x[j];
            }
        }
        return b;
    }

    static int[][] rawE0(int n, int m) {
        return new int[n][m];
    }

    static int[][] rawE0(int n) {
        return new int[n][n];
    }

    static int[][] rawE1(int n) {
        int[][] A = new int[n][n];
        for (int i = 0; i < n; i++) A[i][i] = 1;
        return A;
    }

    static void swapRow(int[][] A, int i, int j) {
        int[] tmp = A[i]; A[i] = A[j]; A[j] = tmp;
    }

    static void dimensionCheck(int dim1, int dim2) {
        if (dim1 != dim2) {
            throw new ArithmeticException(
                String.format("Dimension Error: %d != %d.", dim1, dim2)
            );
        }
    }
}
