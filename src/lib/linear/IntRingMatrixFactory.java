package lib.linear;

import java.util.Objects;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import lib.base.ArithmeticOperations;

public class IntRingMatrixFactory {
    public final IntBinaryOperator add;
    public final IntBinaryOperator mul;
    public final int E0;
    public final int E1;
    public final IntUnaryOperator addInv;
    public final IntUnaryOperator mulInv;
    public IntRingMatrixFactory(IntBinaryOperator add, int e0, IntBinaryOperator mul, int e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = null;
        this.mulInv = null;
    }
    public IntRingMatrixFactory(IntBinaryOperator add, IntUnaryOperator addInv, int e0, IntBinaryOperator mul, int e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = addInv;
        this.mulInv = null;
    }
    public IntRingMatrixFactory(IntBinaryOperator add, int e0, IntBinaryOperator mul, IntUnaryOperator mulInv, int e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = null;
        this.mulInv = mulInv;
    }
    public IntRingMatrixFactory(IntBinaryOperator add, IntUnaryOperator addInv, int e0, IntBinaryOperator mul, IntUnaryOperator mulInv, int e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = addInv;
        this.mulInv = mulInv;
    }
    int add(int a, int b) {
        return add.applyAsInt(a, b);
    }
    int addInv(int a) {
        return addInv.applyAsInt(a);
    }
    int sub(int a, int b) {
        return add.applyAsInt(a, addInv.applyAsInt(b));
    }
    int mul(int a, int b) {
        return mul.applyAsInt(a, b);
    }
    int mulInv(int a) {
        return mulInv.applyAsInt(a);
    }
    int div(int a, int b) {
        return mul.applyAsInt(a, mulInv.applyAsInt(b));
    }
    public IntRingMatrix createE0(int n, int m) {
        return new IntRingMatrix(rawE0(n, m));
    }
    public IntRingMatrix createE0(int n) {
        return new IntRingMatrix(rawE0(n));
    }
    public IntRingMatrix createE1(int n) {
        return new IntRingMatrix(rawE1(n));
    }
    public IntRingMatrix create(int[][] A) {
        return new IntRingMatrix(A);
    }
    public IntRingMatrix add(IntRingMatrix A, IntRingMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        int[][] c = new int[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = add(A.A[i][j], B.A[i][j]);
        }
        return new IntRingMatrix(c);
    }
    public IntRingMatrix sub(IntRingMatrix A, IntRingMatrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        Objects.requireNonNull(addInv);
        final int N = A.N, M = A.M;
        int[][] c = new int[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = sub(A.A[i][j], B.A[i][j]);
        }
        return new IntRingMatrix(c);
    }
    public IntRingMatrix mul(IntRingMatrix A, IntRingMatrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        int[][] c = new int[N][L];
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] = add(c[i][j], mul(A.A[i][k], B.A[k][j]));
        }
        return new IntRingMatrix(c);
    }
    public IntRingMatrix div(IntRingMatrix A, IntRingMatrix B) {
        IntRingMatrix iB = inv(B);
        if (iB == null) {
            throw new ArithmeticException("divide by irregular matrix.");
        }
        return mul(A, iB);
    }
    public IntRingMatrix inv(IntRingMatrix A) {
        dimensionCheck(A.N, A.M);
        Objects.requireNonNull(addInv);
        Objects.requireNonNull(mulInv);
        final int N = A.N;
        int[][] a = A.copyOfRawMatrix();
        int[][] b = rawE1(N);
        for (int i = 0; i < N; i++) {
            if (a[i][i] == E0) {
                for (int j = i + 1; j < N; j++) {
                    if (a[j][i] == E0) continue;
                    swapRow(a, i, j);
                    break;
                }
                if (a[i][i] == E0) return null;
            }
            int inv = mulInv(a[i][i]);
            for (int k = i; k < N; k++) a[i][k] = mul(a[i][k], inv);
            for (int k = 0; k < N; k++) b[i][k] = mul(b[i][k], inv);
            for (int j = i + 1; j < N; j++) {
                int x = a[j][i];
                for (int k = i; k < N; k++) a[j][k] = sub(a[j][k], mul(x, a[i][k]));
                for (int k = 0; k < N; k++) b[j][k] = sub(b[j][k], mul(x, b[i][k]));
            }
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                int x = a[j][i];
                for (int k = 0; k < N; k++) b[j][k] = sub(b[j][k], mul(x, b[i][k]));
                a[j][i] = E0;
            }
        }
        return new IntRingMatrix(b);
    }
    public int det(IntRingMatrix A) {
        dimensionCheck(A.N, A.M);
        Objects.requireNonNull(addInv);
        Objects.requireNonNull(mulInv);
        final int N = A.N;
        int[][] a = A.copyOfRawMatrix();
        int det = E1;
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
            int inv = mulInv(a[i][i]);
            for (int j = i + 1; j < N; j++) {
                int r = mul(a[j][i], inv);
                for (int k = i; k < N; k++) a[j][k] = sub(a[j][k], mul(a[i][k], r));
            }
        }
        return det;
    }
    public IntRingMatrix pow(IntRingMatrix A, long n) {
        IntRingMatrix ret = createE1(A.N);
        IntRingMatrix pow = A;
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
    public int[] mul(IntRingMatrix A, int[] x) {
        dimensionCheck(A.M, x.length);
        int[] b = new int[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] = add(b[i], mul(A.A[i][j], x[j]));
            }
        }
        return b;
    }
    public class IntRingMatrix implements ArithmeticOperations<IntRingMatrix> {
        final int N, M;
        final int[][] A;
        IntRingMatrix(int n, int m) {
            this.N = n;
            this.M = m;
            this.A = new int[N][M];
        }
        IntRingMatrix(int[][] A) {
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
        public IntRingMatrix add(IntRingMatrix B) {
            return IntRingMatrixFactory.this.add(this, B);
        }
        public IntRingMatrix sub(IntRingMatrix B) {
            return IntRingMatrixFactory.this.sub(this, B);
        }
        public IntRingMatrix mul(IntRingMatrix B) {
            return IntRingMatrixFactory.this.mul(this, B);
        }
        public IntRingMatrix div(IntRingMatrix B) {
            return IntRingMatrixFactory.this.div(this, B);
        }
        public IntRingMatrix inv() {
            return IntRingMatrixFactory.this.inv(this);
        }
        public int det() {
            return IntRingMatrixFactory.this.det(this);
        }
        public IntRingMatrix pow(long n) {
            return IntRingMatrixFactory.this.pow(this, n);
        }
        public int[] mul(int[] x) {
            return IntRingMatrixFactory.this.mul(this, x);
        }

        public int[][] copyOfRawMatrix() {
            int[][] ret = new int[N][M];
            for (int i = 0; i < N; i++) ret[i] = A[i].clone();
            return ret;
        }
        public int[][] getRawMatrix() {
            return A;
        }
    }

    int[][] rawE0(int n, int m) {
        int[][] e0 = new int[n][m];
        for (int[] e0i : e0) java.util.Arrays.fill(e0i, E0);
        return e0;
    }

    int[][] rawE0(int n) {
        return rawE0(n, n);
    }

    int[][] rawE1(int n) {
        int[][] A = rawE0(n);
        for (int i = 0; i < n; i++) A[i][i] = E1;
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