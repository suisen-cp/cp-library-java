package lib.linear;

import lib.base.ArithmeticOperations;
import lib.math.number.ModArithmetic;

public final class ModMatrixFactory {
    public final long MOD;
    public final ModArithmetic MA;
    public ModMatrixFactory(ModArithmetic ma) {
        this.MOD = ma.getMod();
        this.MA = ma;
    }
    public ModMatrix createE0(int n, int m) {
        return new ModMatrix(rawE0(n, m));
    }
    public ModMatrix createE0(int n) {
        return new ModMatrix(rawE0(n));
    }
    public ModMatrix createE1(int n) {
        return new ModMatrix(rawE1(n));
    }
    public ModMatrix create(long[][] A) {
        return new ModMatrix(A);
    }
    public ModMatrix add(ModMatrix A, ModMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        long[][] c = new long[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] + B.A[i][j];
        }
        return new ModMatrix(c);
    }
    public ModMatrix sub(ModMatrix A, ModMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        long[][] c = new long[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = A.A[i][j] - B.A[i][j];
        }
        return new ModMatrix(c);
    }
    public ModMatrix mul(ModMatrix A, ModMatrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        long[][] c = new long[N][L];
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] += MA.mul(A.A[i][k], B.A[k][j]);
        }
        return new ModMatrix(c);
    }
    public ModMatrix div(ModMatrix A, ModMatrix B) {
        ModMatrix iB = inv(B);
        if (iB == null) {
            throw new ArithmeticException("divide by irregular matrix.");
        }
        return mul(A, iB);
    }
    public ModMatrix inv(ModMatrix A) {
        dimensionCheck(A.N, A.M);
        final int N = A.N;
        long[][] a = A.copyOfRawMatrix();
        long[][] b = rawE1(N);
        for (int i = 0; i < N; i++) {
            if (a[i][i] == 0) {
                for (int j = i + 1; j < N; j++) {
                    if (a[j][i] == 0) continue;
                    swapRow(a, i, j);
                    break;
                }
                if (a[i][i] == 0) return null;
            }
            long inv = MA.inv(a[i][i]);
            for (int k = i; k < N; k++) a[i][k] = MA.mul(a[i][k], inv);
            for (int k = 0; k < N; k++) b[i][k] = MA.mul(b[i][k], inv);
            for (int j = i + 1; j < N; j++) {
                long x = a[j][i];
                for (int k = i; k < N; k++) a[j][k] = MA.mod(a[j][k] - x * a[i][k]);
                for (int k = 0; k < N; k++) b[j][k] = MA.mod(b[j][k] - x * b[i][k]);
            }
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                long x = a[j][i];
                for (int k = 0; k < N; k++) b[j][k] = MA.mod(b[j][k] - x * b[i][k]);
                a[j][i] = 0;
            }
        }
        return new ModMatrix(b);
    }
    public long det(ModMatrix A) {
        dimensionCheck(A.N, A.M);
        if (MOD == 1) return 0;
        final int N = A.N;
        long[][] a = A.copyOfRawMatrix();
        long det = 1;
        for (int i = 0; i < N; i++) {
            if (a[i][i] == 0) {
                for (int j = i + 1; j < N; j++) {
                    if (a[j][i] == 0) continue;
                    swapRow(a, i, j);
                    det = MOD - det;
                    break;
                }
                if (a[i][i] == 0) return 0;
            }
            det = MA.mul(det, a[i][i]);
            long inv = MA.inv(a[i][i]);
            for (int j = i + 1; j < N; j++) {
                long r = MA.mul(a[j][i], inv);
                for (int k = i; k < N; k++) a[j][k] = MA.mod(a[j][k] - a[i][k] * r);
            }
        }
        return det;
    }
    public ModMatrix pow(ModMatrix A, long n) {
        ModMatrix ret = createE1(A.N);
        ModMatrix pow = A;
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
    public long[] mul(ModMatrix A, long[] x) {
        dimensionCheck(A.M, x.length);
        long[] b = new long[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] += MA.mul(A.A[i][j], x[j]);
            }
            b[i] = MA.mod(b[i]);
        }
        return b;
    }
    public class ModMatrix implements ArithmeticOperations<ModMatrix> {
        final int N, M;
        final long[][] A;
        ModMatrix(int n, int m) {
            this.N = n;
            this.M = m;
            this.A = new long[N][M];
        }
        ModMatrix(long[][] A) {
            this.N = A.length;
            this.M = this.N == 0 ? 0 : A[0].length;
            this.A = new long[N][M];
            for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
                this.A[i][j] = MA.mod(A[i][j]);
            }
        }
        public long get(int i, int j) {
            return A[i][j];
        }
        public void set(int i, int j, long val) {
            A[i][j] = MA.mod(val);
        }
        public ModMatrix add(ModMatrix B) {
            return ModMatrixFactory.this.add(this, B);
        }
        public ModMatrix sub(ModMatrix B) {
            return ModMatrixFactory.this.sub(this, B);
        }
        public ModMatrix mul(ModMatrix B) {
            return ModMatrixFactory.this.mul(this, B);
        }
        public ModMatrix div(ModMatrix B) {
            return ModMatrixFactory.this.div(this, B);
        }
        public ModMatrix inv() {
            return ModMatrixFactory.this.inv(this);
        }
        public long det() {
            return ModMatrixFactory.this.det(this);
        }
        public ModMatrix pow(long n) {
            return ModMatrixFactory.this.pow(this, n);
        }
        public long[] mul(long[] x) {
            return ModMatrixFactory.this.mul(this, x);
        }

        public long[][] copyOfRawMatrix() {
            long[][] ret = new long[N][M];
            for (int i = 0; i < N; i++) ret[i] = A[i].clone();
            return ret;
        }
        public long[][] getRawMatrix() {
            return A;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append('[');
            for (long[] row : A) sb.append(java.util.Arrays.toString(row)).append('\n');
            return sb.append(']').toString();
        }
    }

    long[][] rawE0(int n, int m) {
        return new long[n][m];
    }

    long[][] rawE0(int n) {
        return new long[n][n];
    }

    long[][] rawE1(int n) {
        long[][] A = new long[n][n];
        for (int i = 0; i < n; i++) A[i][i] = MOD == 1 ? 0 : 1;
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
