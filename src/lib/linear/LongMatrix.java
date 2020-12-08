package lib.linear;

import lib.base.ArithmeticOperations;

public class LongMatrix implements ArithmeticOperations<LongMatrix> {
    final int N, M;
    final long[][] A;
    public LongMatrix(int n, int m) {
        this.N = n;
        this.M = m;
        this.A = new long[N][M];
    }
    public LongMatrix(long[][] A) {
        this.N = A.length;
        this.M = A[0].length;
        this.A = new long[N][M];
        for (int i = 0; i < N; i++) this.A[i] = A[i].clone();
    }
    public long get(int i, int j) {
        return A[i][j];
    }
    public void set(int i, int j, long val) {
        A[i][j] = val;
    }
    public LongMatrix add(LongMatrix B) {
        return add(this, B);
    }
    public LongMatrix sub(LongMatrix B) {
        return sub(this, B);
    }
    public LongMatrix mul(LongMatrix B) {
        return mul(this, B);
    }
    public LongMatrix div(LongMatrix B) {
        throw new UnsupportedOperationException();
    }
    public LongMatrix pow(long n) {
        return pow(this, n);
    }
    public long[] mul(long[] x) {
        return mul(this, x);
    }

    public long[][] copyOfRawMatrix() {
        long[][] ret = new long[N][M];
        for (int i = 0; i < N; i++) ret[i] = A[i].clone();
        return ret;
    }
    public long[][] getRawMatrix() {
        return A;
    }

    public static LongMatrix createE0(int n, int m) {
        return new LongMatrix(rawE0(n, m));
    }
    public static LongMatrix createE0(int n) {
        return new LongMatrix(rawE0(n));
    }
    public static LongMatrix createE1(int n) {
        return new LongMatrix(rawE1(n));
    }
    public static LongMatrix create(long[][] A) {
        return new LongMatrix(A);
    }
    public static LongMatrix add(LongMatrix A, LongMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        long[][] c = new long[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] + B.A[i][j];
        }
        return new LongMatrix(c);
    }
    public static LongMatrix sub(LongMatrix A, LongMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        long[][] c = new long[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] - B.A[i][j];
        }
        return new LongMatrix(c);
    }
    public static LongMatrix mul(LongMatrix A, LongMatrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        long[][] c = new long[N][L];
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] += A.A[i][k] * B.A[k][j];
        }
        return new LongMatrix(c);
    }
    public static LongMatrix pow(LongMatrix A, long n) {
        LongMatrix ret = createE1(A.N);
        LongMatrix pow = A;
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
    public static long[] mul(LongMatrix A, long[] x) {
        dimensionCheck(A.M, x.length);
        long[] b = new long[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] += A.A[i][j] * x[j];
            }
        }
        return b;
    }

    static long[][] rawE0(int n, int m) {
        return new long[n][m];
    }

    static long[][] rawE0(int n) {
        return new long[n][n];
    }

    static long[][] rawE1(int n) {
        long[][] A = new long[n][n];
        for (int i = 0; i < n; i++) A[i][i] = 1;
        return A;
    }

    static void swapRow(long[][] A, int i, int j) {
        long[] tmp = A[i]; A[i] = A[j]; A[j] = tmp;
    }

    static void dimensionCheck(int dim1, int dim2) {
        if (dim1 != dim2) {
            throw new ArithmeticException(
                String.format("Dimension Error: %d != %d.", dim1, dim2)
            );
        }
    }
}
