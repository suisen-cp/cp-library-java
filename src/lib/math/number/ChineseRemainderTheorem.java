package lib.math.number;

public class ChineseRemainderTheorem {
    public static long[] solve(long[] r, ModArithmetic[] ma){
        if (r.length != ma.length) {
            throw new AssertionError("Different length.");
        }
        int n = r.length;
        long r0 = 0, m0 = 1;
        ModArithmetic ma0 = ModArithmetic1.INSTANCE;
        for (int i = 0; i < n; i++) {
            long m = ma[i].getMod();
            if (m < 1) {
                throw new AssertionError();
            }
            long m1 = m;
            ModArithmetic ma1 = ma[i];
            long r1 = ma1.mod(r[i]);
            if (r1 < 0) r1 += m1;
            if (m0 < m1){
                long tmp;
                tmp = r0; r0 = r1; r1 = tmp;
                tmp = m0; m0 = m1; m1 = tmp;
                ModArithmetic matmp;
                matmp = ma0; ma0 = ma1; ma1 = matmp; 
            }
            if (ma1.mod(m0) == 0) {
                if (ma1.mod(r0) != r1) return new long[]{0, 0};
                continue;
            }
            long[] gi = ma1.gcdInv(m0);
            long g = gi[0], im = gi[1];
            long u1 = m1 / g;
            if ((r1 - r0) % g != 0) return new long[]{0, 0};
            long x = (r1 - r0) / g % u1 * im % u1;
            r0 += x * m0;
            m0 *= u1;
            if (r0 < 0) r0 += m0;
        } 
        return new long[]{r0, m0};
    }
}
