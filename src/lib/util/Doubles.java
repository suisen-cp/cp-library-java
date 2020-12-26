package lib.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.DoubleBinaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Doubles {
    private Doubles(){}
    public static double max(final double... a) {
        double ret = Double.MIN_VALUE;
        for (final double e : a) ret = Math.max(ret, e);
        return ret;
    }
    public static double min(final double... a) {
        double ret = Double.MAX_VALUE;
        for (final double e : a) ret = Math.min(ret, e);
        return ret;
    }
    public static double fold(final DoubleBinaryOperator func, final double a, final double... b) {
        double ret = a;
        for (final double c : b) ret = func.applyAsDouble(ret, c);
        return ret;
    }
    public static String join(final double[] a, final String sep) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(a[i]);
            if (i < a.length - 1) sb.append(sep);
        }
        return sb.toString();
    }
    public static long ceil(double d) {
        return (long) Math.ceil(d);
    }
    public static long floor(double d) {
        return (long) Math.floor(d);
    }
    public static long ceilingMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.CEILING);
        return m.longValueExact();
    }
    public static long floorMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.FLOOR);
        return m.longValueExact();
    }
    public static long halfUpMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.HALF_UP);
        return m.longValueExact();
    }
    public static long halfDownMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.HALF_DOWN);
        return m.longValueExact();
    }
    public static long halfEvenMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.HALF_EVEN);
        return m.longValueExact();
    }
    public static long toZeroMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.DOWN);
        return m.longValueExact();
    }
    public static long toInfMultiply(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.UP);
        return m.longValueExact();
    }
    public static long ceilingDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.CEILING);
        return m.longValueExact();
    }
    public static long floorDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.FLOOR);
        return m.longValueExact();
    }
    public static long halfUpDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.HALF_UP);
        return m.longValueExact();
    }
    public static long halfDownDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.HALF_DOWN);
        return m.longValueExact();
    }
    public static long halfEvenDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.HALF_EVEN);
        return m.longValueExact();
    }
    public static long toZeroDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.DOWN);
        return m.longValueExact();
    }
    public static long toInfDivide(double a, double b) {
        BigDecimal m = BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b));
        m.setScale(0, RoundingMode.UP);
        return m.longValueExact();
    }
    public static String toNonExponentialString(double d) {
        return BigDecimal.valueOf(d).toPlainString();
    }
}