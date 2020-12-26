package lib.linear;

import lib.base.ArithmeticOperations;
import lib.math.number.ModArithmetic;

public final class ModMatrixFactory2 {
    public final long MOD;
    public final ModArithmetic MA;
    public ModMatrixFactory2(ModArithmetic ma) {
        this.MOD = ma.getMod();
        this.MA = ma;
    }
    public ModMatrix createE0() {
        return new ModMatrix();
    }
    public ModMatrix createE1() {
        return new ModMatrix(1, 0, 0, 1);
    }
    public ModMatrix create(long A_00, long A_01, long A_10, long A_11) {
        return new ModMatrix(A_00, A_01, A_10, A_11);
    }
    public ModMatrix add(ModMatrix A, ModMatrix B) {
        return new ModMatrix(
            MA.add(A.A_00, B.A_00), MA.add(A.A_01, B.A_01), 
            MA.add(A.A_10, B.A_10), MA.add(A.A_11, B.A_11));
    }
    public ModMatrix sub(ModMatrix A, ModMatrix B) {
        return new ModMatrix(
            MA.sub(A.A_00, B.A_00), MA.sub(A.A_01, B.A_01), 
            MA.sub(A.A_10, B.A_10), MA.sub(A.A_11, B.A_11));
    }
    public ModMatrix mul(ModMatrix A, ModMatrix B) {
        long C_00 = MA.mod(A.A_00 * B.A_00 + A.A_01 * B.A_10);
        long C_01 = MA.mod(A.A_00 * B.A_01 + A.A_01 * B.A_11);
        long C_10 = MA.mod(A.A_10 * B.A_00 + A.A_11 * B.A_10);
        long C_11 = MA.mod(A.A_10 * B.A_01 + A.A_11 * B.A_11);
        return new ModMatrix(C_00, C_01, C_10, C_11);
    }
    public ModMatrix div(ModMatrix A, ModMatrix B) {
        ModMatrix iB = inv(B);
        if (iB == null) {
            throw new ArithmeticException("divide by irregular matrix.");
        }
        return mul(A, iB);
    }
    public ModMatrix inv(ModMatrix A) {
        long den = MA.mod(A.A_00 * A.A_11 - A.A_01 * A.A_10);
        if (den == 0) return null;
        long inv = MA.inv(den);
        long C_00 = MA.mul(inv,       A.A_11);
        long C_01 = MA.mul(inv, MOD - A.A_01);
        long C_10 = MA.mul(inv, MOD - A.A_10);
        long C_11 = MA.mul(inv,       A.A_00);
        return new ModMatrix(C_00, C_01, C_10, C_11);
    }
    public long det(ModMatrix A) {
        return MA.mod(A.A_00 * A.A_11 - A.A_01 * A.A_10);
    }
    public ModMatrix pow(ModMatrix A, long n) {
        ModMatrix ret = createE1();
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
    public long[] mul(ModMatrix A, long x0, long x1) {
        long b_0 = MA.mod(A.A_00 * x0 + A.A_01 * x1);
        long b_1 = MA.mod(A.A_10 * x0 + A.A_11 * x1);
        return new long[]{b_0, b_1};
    }
    public class ModMatrix implements ArithmeticOperations<ModMatrix> {
        final long A_00, A_01, A_10, A_11;
        ModMatrix() {
            this.A_00 = this.A_01 = this.A_10 = this.A_11 = 0;
        }
        ModMatrix(long A_00, long A_01,
                  long A_10, long A_11) {
            this.A_00 = A_00; this.A_01 = A_01;
            this.A_10 = A_10; this.A_11 = A_11;
        }
        public long get(int i, int j) {
            if (i == 0) {
                return j == 0 ? A_00 : A_01;
            } else if (i == 1) {
                return j == 0 ? A_10 : A_11;
            }
            throw new IndexOutOfBoundsException();
        }
        public ModMatrix add(ModMatrix B) {
            return ModMatrixFactory2.this.add(this, B);
        }
        public ModMatrix sub(ModMatrix B) {
            return ModMatrixFactory2.this.sub(this, B);
        }
        public ModMatrix mul(ModMatrix B) {
            return ModMatrixFactory2.this.mul(this, B);
        }
        public ModMatrix div(ModMatrix B) {
            return ModMatrixFactory2.this.div(this, B);
        }
        public ModMatrix inv() {
            return ModMatrixFactory2.this.inv(this);
        }
        public long det() {
            return ModMatrixFactory2.this.det(this);
        }
        public ModMatrix pow(long n) {
            return ModMatrixFactory2.this.pow(this, n);
        }
        public long[] mul(long x0, long x1) {
            return ModMatrixFactory2.this.mul(this, x0, x1);
        }
    }
}
