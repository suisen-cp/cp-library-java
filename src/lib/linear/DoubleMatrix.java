package lib.linear;

import lib.base.ArithmeticOperations;

public class DoubleMatrix implements ArithmeticOperations<DoubleMatrix> {
    public static final double EPS = 1e-9;

    final int N, M;
    final double[][] A;
    public DoubleMatrix(int n, int m) {
        this.N = n;
        this.M = m;
        this.A = new double[N][M];
    }
    public DoubleMatrix(double[][] A) {
        this.N = A.length;
        this.M = A[0].length;
        this.A = new double[N][M];
        for (int i = 0; i < N; i++) this.A[i] = A[i].clone();
    }
    public static int sgn(double d) {
        return d > EPS ? 1 : d < -EPS ? -1 : 0;
    }
    public double get(int i, int j) {
        return A[i][j];
    }
    public void set(int i, int j, double val) {
        A[i][j] = val;
    }
    public DoubleMatrix add(DoubleMatrix B) {
        return add(this, B);
    }
    public DoubleMatrix sub(DoubleMatrix B) {
        return sub(this, B);
    }
    public DoubleMatrix mul(DoubleMatrix B) {
        return mul(this, B);
    }
    public DoubleMatrix div(DoubleMatrix B) {
        return div(this, B);
    }
    public DoubleMatrix inv() {
        return inv(this);
    }
    public double det() {
        return det(this);
    }
    public DoubleMatrix pow(long n) {
        return pow(this, n);
    }
    public double[] mul(double[] x) {
        return mul(this, x);
    }

    public double[][] copyOfRawMatrix() {
        double[][] ret = new double[N][M];
        for (int i = 0; i < N; i++) ret[i] = A[i].clone();
        return ret;
    }
    public double[][] getRawMatrix() {
        return A;
    }

    public static DoubleMatrix createE0(int n, int m) {
        return new DoubleMatrix(rawE0(n, m));
    }
    public static DoubleMatrix createE0(int n) {
        return new DoubleMatrix(rawE0(n));
    }
    public static DoubleMatrix createE1(int n) {
        return new DoubleMatrix(rawE1(n));
    }
    public static DoubleMatrix create(double[][] A) {
        return new DoubleMatrix(A);
    }
    public static DoubleMatrix add(DoubleMatrix A, DoubleMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        double[][] c = new double[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] + B.A[i][j];
        }
        return new DoubleMatrix(c);
    }
    public static DoubleMatrix sub(DoubleMatrix A, DoubleMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        double[][] c = new double[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] - B.A[i][j];
        }
        return new DoubleMatrix(c);
    }
    public static DoubleMatrix mul(DoubleMatrix A, DoubleMatrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        double[][] c = new double[N][L];
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] = Math.fma(A.A[i][k], B.A[k][j], c[i][j]);
        }
        return new DoubleMatrix(c);
    }
    public static DoubleMatrix div(DoubleMatrix A, DoubleMatrix B) {
        DoubleMatrix iB = inv(B);
        if (iB == null) {
            throw new ArithmeticException("divide by irregular matrix.");
        }
        return mul(A, iB);
    }
    public static DoubleMatrix inv(DoubleMatrix A) {
        dimensionCheck(A.N, A.M);
        final int N = A.N;
        double[][] a = A.copyOfRawMatrix();
        double[][] b = rawE1(N);
        for (int i = 0; i < N; i++) {
            double max = Math.abs(a[i][i]);
            int maxj = i;
            for (int j = i + 1; j < N; j++) {
                if (max < Math.abs(a[j][i])) {
                    max = Math.abs(a[j][i]);
                    maxj = j;
                }
            }
            if (i != maxj) {
                swapRow(a, i, maxj);
            }
            if (sgn(a[i][i]) == 0) return null;
            double inv = 1. / a[i][i];
            for (int k = i; k < N; k++) a[i][k] *= inv;
            for (int k = 0; k < N; k++) b[i][k] *= inv;
            for (int j = i + 1; j < N; j++) {
                double x = -a[j][i];
                for (int k = i; k < N; k++) a[j][k] = Math.fma(x, a[i][k], a[j][k]);
                for (int k = 0; k < N; k++) b[j][k] = Math.fma(x, b[i][k], b[j][k]);
            }
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                double x = -a[j][i];
                for (int k = 0; k < N; k++) b[j][k] = Math.fma(x, b[i][k], b[j][k]);
                a[j][i] = 0;
            }
        }
        return new DoubleMatrix(b);
    }
    public static double det(DoubleMatrix A) {
        dimensionCheck(A.N, A.M);
        final int N = A.N;
        double[][] a = A.copyOfRawMatrix();
        double det = 1;
        for (int i = 0; i < N; i++) {
            double max = Math.abs(a[i][i]);
            int maxj = i;
            for (int j = i + 1; j < N; j++) {
                if (max < Math.abs(a[j][i])) {
                    max = Math.abs(a[j][i]);
                    maxj = j;
                }
            }
            if (i != maxj) {
                det = -det;
                swapRow(a, i, maxj);
            }
            if (sgn(a[i][i]) == 0) return 0;
            det *= a[i][i];
            double inv = 1. / a[i][i];
            for (int j = i + 1; j < N; j++) {
                double r = -a[j][i] * inv;
                for (int k = i; k < N; k++) a[j][k] = Math.fma(a[i][k], r, a[j][k]);
            }
        }
        return det;
    }
    public static DoubleMatrix pow(DoubleMatrix A, long n) {
        DoubleMatrix ret = createE1(A.N);
        DoubleMatrix pow = A;
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
    public static double[] mul(DoubleMatrix A, double[] x) {
        dimensionCheck(A.M, x.length);
        double[] b = new double[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] = Math.fma(A.A[i][j], x[j], b[i]);
            }
        }
        return b;
    }

    static double[][] rawE0(int n, int m) {
        return new double[n][m];
    }

    static double[][] rawE0(int n) {
        return new double[n][n];
    }

    static double[][] rawE1(int n) {
        double[][] A = new double[n][n];
        for (int i = 0; i < n; i++) A[i][i] = 1;
        return A;
    }

    static void swapRow(double[][] A, int i, int j) {
        double[] tmp = A[i]; A[i] = A[j]; A[j] = tmp;
    }

    static void dimensionCheck(int dim1, int dim2) {
        if (dim1 != dim2) {
            throw new ArithmeticException(
                String.format("Dimension Error: %d != %d.", dim1, dim2)
            );
        }
    }
}
