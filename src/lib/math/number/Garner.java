package lib.math.number;

import java.util.Arrays;

public class Garner {
    public static long solve(long[] c, ModArithmetic[] mas) {
        int n = c.length + 1;
        long[] cnst = new long[n];
        long[] coef = new long[n];
        Arrays.fill(coef, 1);
        for (int i = 0; i < n - 1; i++) {
            ModArithmetic ma1 = mas[i];
            long m1 = ma1.getMod();
            long v = ma1.mod(c[i] - cnst[i]);
            v = ma1.mul(v, ma1.pow(coef[i], m1 - 2));

            for (int j = i + 1; j < n; j++) {
                ModArithmetic ma2 = mas[j];
                cnst[j] = ma2.mod(cnst[j] + coef[j] * v);
                coef[j] = ma2.mul(coef[j], m1);
            }
        }
        return cnst[n - 1];
    }
}
