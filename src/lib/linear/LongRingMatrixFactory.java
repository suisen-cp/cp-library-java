package lib.linear;

import java.util.Objects;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

import lib.base.ArithmeticOperations;

public class LongRingMatrixFactory {
    public final LongBinaryOperator add;
    public final LongBinaryOperator mul;
    public final long E0;
    public final long E1;
    public final LongUnaryOperator addInv;
    public final LongUnaryOperator mulInv;
    public LongRingMatrixFactory(LongBinaryOperator add, long e0, LongBinaryOperator mul, long e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = null;
        this.mulInv = null;
    }
    public LongRingMatrixFactory(LongBinaryOperator add, LongUnaryOperator addInv, long e0, LongBinaryOperator mul, long e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = addInv;
        this.mulInv = null;
    }
    public LongRingMatrixFactory(LongBinaryOperator add, long e0, LongBinaryOperator mul, LongUnaryOperator mulInv, long e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = null;
        this.mulInv = mulInv;
    }
    public LongRingMatrixFactory(LongBinaryOperator add, LongUnaryOperator addInv, long e0, LongBinaryOperator mul, LongUnaryOperator mulInv, long e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = addInv;
        this.mulInv = mulInv;
    }
    long add(long a, long b) {
        return add.applyAsLong(a, b);
    }
    long addInv(long a) {
        return addInv.applyAsLong(a);
    }
    long sub(long a, long b) {
        return add.applyAsLong(a, addInv.applyAsLong(b));
    }
    long mul(long a, long b) {
        return mul.applyAsLong(a, b);
    }
    long mulInv(long a) {
        return mulInv.applyAsLong(a);
    }
    long div(long a, long b) {
        return mul.applyAsLong(a, mulInv.applyAsLong(b));
    }
    public LongRingMatrix createE0(int n, int m) {
        return new LongRingMatrix(rawE0(n, m));
    }
    public LongRingMatrix createE0(int n) {
        return new LongRingMatrix(rawE0(n));
    }
    public LongRingMatrix createE1(int n) {
        return new LongRingMatrix(rawE1(n));
    }
    public LongRingMatrix create(long[][] A) {
        return new LongRingMatrix(A);
    }
    public LongRingMatrix add(LongRingMatrix A, LongRingMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        long[][] c = new long[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = add(A.A[i][j], B.A[i][j]);
        }
        return new LongRingMatrix(c);
    }
    public LongRingMatrix sub(LongRingMatrix A, LongRingMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        Objects.requireNonNull(addInv);
        final int N = A.N, M = A.M;
        long[][] c = new long[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = sub(A.A[i][j], B.A[i][j]);
        }
        return new LongRingMatrix(c);
    }
    public LongRingMatrix mul(LongRingMatrix A, LongRingMatrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        long[][] c = new long[N][L];
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] = add(c[i][j], mul(A.A[i][k], B.A[k][j]));
        }
        return new LongRingMatrix(c);
    }
    public LongRingMatrix div(LongRingMatrix A, LongRingMatrix B) {
        LongRingMatrix iB = inv(B);
        if (iB == null) {
            throw new ArithmeticException("divide by irregular matrix.");
        }
        return mul(A, iB);
    }
    public LongRingMatrix inv(LongRingMatrix A) {
        dimensionCheck(A.N, A.M);
        Objects.requireNonNull(addInv);
        Objects.requireNonNull(mulInv);
        final int N = A.N;
        long[][] a = A.copyOfRawMatrix();
        long[][] b = rawE1(N);
        for (int i = 0; i < N; i++) {
            if (a[i][i] == E0) {
                for (int j = i + 1; j < N; j++) {
                    if (a[j][i] == E0) continue;
                    swapRow(a, i, j);
                    break;
                }
                if (a[i][i] == E0) return null;
            }
            long inv = mulInv(a[i][i]);
            for (int k = i; k < N; k++) a[i][k] = mul(a[i][k], inv);
            for (int k = 0; k < N; k++) b[i][k] = mul(b[i][k], inv);
            for (int j = i + 1; j < N; j++) {
                long x = a[j][i];
                for (int k = i; k < N; k++) a[j][k] = sub(a[j][k], mul(x, a[i][k]));
                for (int k = 0; k < N; k++) b[j][k] = sub(b[j][k], mul(x, b[i][k]));
            }
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                long x = a[j][i];
                for (int k = 0; k < N; k++) b[j][k] = sub(b[j][k], mul(x, b[i][k]));
                a[j][i] = E0;
            }
        }
        return new LongRingMatrix(b);
    }
    public long det(LongRingMatrix A) {
        dimensionCheck(A.N, A.M);
        Objects.requireNonNull(addInv);
        Objects.requireNonNull(mulInv);
        final int N = A.N;
        long[][] a = A.copyOfRawMatrix();
        long det = E1;
        for (int i = 0; i < N; i++) {
            if (a[i][i] == E0) {
                for (int j = i + 1; j < N; j++) {
                    if (a[j][i] == E0) continue;
                    swapRow(a, i, j);
                    det = addInv(det);
                    break;
                }
                if (a[i][i] == E0) return E0;
            }
            det = mul(det, a[i][i]);
            long inv = mulInv(a[i][i]);
            for (int j = i + 1; j < N; j++) {
                long r = mul(a[j][i], inv);
                for (int k = i; k < N; k++) a[j][k] = sub(a[j][k], mul(a[i][k], r));
            }
        }
        return det;
    }
    public LongRingMatrix pow(LongRingMatrix A, long n) {
        LongRingMatrix ret = createE1(A.N);
        LongRingMatrix pow = A;
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
    public long[] mul(LongRingMatrix A, long[] x) {
        dimensionCheck(A.M, x.length);
        long[] b = new long[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] = add(b[i], mul(A.A[i][j], x[j]));
            }
        }
        return b;
    }
    public class LongRingMatrix implements ArithmeticOperations<LongRingMatrix> {
        final int N, M;
        final long[][] A;
        LongRingMatrix(int n, int m) {
            this.N = n;
            this.M = m;
            this.A = new long[N][M];
        }
        LongRingMatrix(long[][] A) {
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
        public LongRingMatrix add(LongRingMatrix B) {
            return LongRingMatrixFactory.this.add(this, B);
        }
        public LongRingMatrix sub(LongRingMatrix B) {
            return LongRingMatrixFactory.this.sub(this, B);
        }
        public LongRingMatrix mul(LongRingMatrix B) {
            return LongRingMatrixFactory.this.mul(this, B);
        }
        public LongRingMatrix div(LongRingMatrix B) {
            return LongRingMatrixFactory.this.div(this, B);
        }
        public LongRingMatrix inv() {
            return LongRingMatrixFactory.this.inv(this);
        }
        public long det() {
            return LongRingMatrixFactory.this.det(this);
        }
        public LongRingMatrix pow(long n) {
            return LongRingMatrixFactory.this.pow(this, n);
        }
        public long[] mul(long[] x) {
            return LongRingMatrixFactory.this.mul(this, x);
        }

        public long[][] copyOfRawMatrix() {
            long[][] ret = new long[N][M];
            for (int i = 0; i < N; i++) ret[i] = A[i].clone();
            return ret;
        }
        public long[][] getRawMatrix() {
            return A;
        }
    }

    long[][] rawE0(int n, int m) {
        long[][] e0 = new long[n][m];
        for (long[] e0i : e0) java.util.Arrays.fill(e0i, E0);
        return e0;
    }

    long[][] rawE0(int n) {
        return rawE0(n, n);
    }

    long[][] rawE1(int n) {
        long[][] A = rawE0(n);
        for (int i = 0; i < n; i++) A[i][i] = E1;
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