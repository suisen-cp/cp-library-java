package lib.linear;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import lib.base.ArithmeticOperations;

@SuppressWarnings("unchecked")
public class MatrixFactory<T> {
    public final BinaryOperator<T> add;
    public final BinaryOperator<T> mul;
    public final T E0;
    public final T E1;
    public final UnaryOperator<T> addInv;
    public final UnaryOperator<T> mulInv;
    public MatrixFactory(BinaryOperator<T> add, T e0, BinaryOperator<T> mul, T e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = null;
        this.mulInv = null;
    }
    public MatrixFactory(BinaryOperator<T> add, UnaryOperator<T> addInv, T e0, BinaryOperator<T> mul, T e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = addInv;
        this.mulInv = null;
    }
    public MatrixFactory(BinaryOperator<T> add, T e0, BinaryOperator<T> mul, UnaryOperator<T> mulInv, T e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = null;
        this.mulInv = mulInv;
    }
    public MatrixFactory(BinaryOperator<T> add, UnaryOperator<T> addInv, T e0, BinaryOperator<T> mul, UnaryOperator<T> mulInv, T e1) {
        this.add = add;
        this.mul = mul;
        this.E0 = e0;
        this.E1 = e1;
        this.addInv = addInv;
        this.mulInv = mulInv;
    }
    T add(T a, T b) {
        return add.apply(a, b);
    }
    T addInv(T a) {
        return addInv.apply(a);
    }
    T sub(T a, T b) {
        return add.apply(a, addInv.apply(b));
    }
    T mul(T a, T b) {
        return mul.apply(a, b);
    }
    T mulInv(T a) {
        return mulInv.apply(a);
    }
    T div(T a, T b) {
        return mul.apply(a, mulInv.apply(b));
    }
    public Matrix createE0(int n, int m) {
        return new Matrix(rawE0(n, m));
    }
    public Matrix createE0(int n) {
        return new Matrix(rawE0(n));
    }
    public Matrix createE1(int n) {
        return new Matrix(rawE1(n));
    }
    public Matrix create(T[][] A) {
        return new Matrix(A);
    }
    public Matrix add(Matrix A, Matrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        final int N = A.N, M = A.M;
        T[][] c = (T[][]) new Object[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = add(A.A[i][j], B.A[i][j]);
        }
        return new Matrix(c);
    }
    public Matrix sub(Matrix A, Matrix B) {
        dimensionCheck(A.N, B.N);
        dimensionCheck(A.M, B.M);
        Objects.requireNonNull(addInv);
        final int N = A.N, M = A.M;
        T[][] c = (T[][]) new Object[N][M];
        for (int i = 0; i < N; i++) for (int j = 0; j < M; j++) {
            c[i][j] = sub(A.A[i][j], B.A[i][j]);
        }
        return new Matrix(c);
    }
    public Matrix mul(Matrix A, Matrix B) {
        dimensionCheck(A.M, B.N);
        final int N = A.N, M = A.M, L = B.M;
        T[][] c = rawE0(N, L);
        for (int i = 0; i < N; i++) for (int k = 0; k < M; k++) for (int j = 0; j < L; j++) {
            c[i][j] = add(c[i][j], mul(A.A[i][k], B.A[k][j]));
        }
        return new Matrix(c);
    }
    public Matrix div(Matrix A, Matrix B) {
        Matrix iB = inv(B);
        if (iB == null) {
            throw new ArithmeticException("divide by irregular matrix.");
        }
        return mul(A, iB);
    }
    public Matrix inv(Matrix A) {
        dimensionCheck(A.N, A.M);
        Objects.requireNonNull(addInv);
        Objects.requireNonNull(mulInv);
        final int N = A.N;
        T[][] a = A.copyOfRawMatrix();
        T[][] b = rawE1(N);
        for (int i = 0; i < N; i++) {
            if (a[i][i] == E0) {
                for (int j = i + 1; j < N; j++) {
                    if (a[j][i] == E0) continue;
                    swapRow(a, i, j);
                    break;
                }
                if (a[i][i] == E0) return null;
            }
            T inv = mulInv(a[i][i]);
            for (int k = i; k < N; k++) a[i][k] = mul(a[i][k], inv);
            for (int k = 0; k < N; k++) b[i][k] = mul(b[i][k], inv);
            for (int j = i + 1; j < N; j++) {
                T x = a[j][i];
                for (int k = i; k < N; k++) a[j][k] = sub(a[j][k], mul(x, a[i][k]));
                for (int k = 0; k < N; k++) b[j][k] = sub(b[j][k], mul(x, b[i][k]));
            }
        }
        for (int i = N - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                T x = a[j][i];
                for (int k = 0; k < N; k++) b[j][k] = sub(b[j][k], mul(x, b[i][k]));
                a[j][i] = E0;
            }
        }
        return new Matrix(b);
    }
    public T det(Matrix A) {
        dimensionCheck(A.N, A.M);
        Objects.requireNonNull(addInv);
        Objects.requireNonNull(mulInv);
        final int N = A.N;
        T[][] a = A.copyOfRawMatrix();
        T det = E1;
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
            T inv = mulInv(a[i][i]);
            for (int j = i + 1; j < N; j++) {
                T r = mul(a[j][i], inv);
                for (int k = i; k < N; k++) a[j][k] = sub(a[j][k], mul(a[i][k], r));
            }
        }
        return det;
    }
    public Matrix pow(Matrix A, long n) {
        Matrix ret = createE1(A.N);
        Matrix pow = A;
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
    public T[] mul(Matrix A, T[] x) {
        dimensionCheck(A.M, x.length);
        T[] b = (T[]) new Object[A.N];
        for (int i = 0; i < A.N; i++) {
            for (int j = 0; j < A.M; j++) {
                b[i] = add(b[i], mul(A.A[i][j], x[j]));
            }
        }
        return b;
    }
    public class Matrix implements ArithmeticOperations<Matrix> {
        final int N, M;
        final T[][] A;
        Matrix(int n, int m) {
            this.N = n;
            this.M = m;
            this.A = (T[][]) new Object[N][M];
        }
        Matrix(T[][] A) {
            this.N = A.length;
            this.M = A[0].length;
            this.A = (T[][]) new Object[N][M];
            for (int i = 0; i < N; i++) this.A[i] = A[i].clone();
        }
        public T get(int i, int j) {
            return A[i][j];
        }
        public void set(int i, int j, T val) {
            A[i][j] = val;
        }
        public Matrix add(Matrix B) {
            return MatrixFactory.this.add(this, B);
        }
        public Matrix sub(Matrix B) {
            return MatrixFactory.this.sub(this, B);
        }
        public Matrix mul(Matrix B) {
            return MatrixFactory.this.mul(this, B);
        }
        public Matrix div(Matrix B) {
            return MatrixFactory.this.div(this, B);
        }
        public Matrix inv() {
            return MatrixFactory.this.inv(this);
        }
        public T det() {
            return MatrixFactory.this.det(this);
        }
        public Matrix pow(long n) {
            return MatrixFactory.this.pow(this, n);
        }
        public T[] mul(T[] x) {
            return MatrixFactory.this.mul(this, x);
        }

        public T[][] copyOfRawMatrix() {
            T[][] ret = (T[][]) new Object[N][M];
            for (int i = 0; i < N; i++) ret[i] = A[i].clone();
            return ret;
        }
        public T[][] getRawMatrix() {
            return A;
        }
    }

    T[][] rawE0(int n, int m) {
        T[][] e0 = (T[][]) new Object[n][m];
        for (T[] e0i : e0) java.util.Arrays.fill(e0i, E0);
        return e0;
    }

    T[][] rawE0(int n) {
        return rawE0(n, n);
    }

    T[][] rawE1(int n) {
        T[][] A = rawE0(n);
        for (int i = 0; i < n; i++) A[i][i] = E1;
        return A;
    }

    static <T> void swapRow(T[][] A, int i, int j) {
        T[] tmp = A[i]; A[i] = A[j]; A[j] = tmp;
    }

    static void dimensionCheck(int dim1, int dim2) {
        if (dim1 != dim2) {
            throw new ArithmeticException(
                String.format("Dimension Error: %d != %d.", dim1, dim2)
            );
        }
    }
}
