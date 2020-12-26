package lib.util.function;

import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface IntBiPredicate {
    int INT_BIT = 32;
    long MASK = 0xffff_ffffL;
    boolean test(int x, int y);
    default IntPredicate curry(final int x) {return y -> test(x, y);}
    default LongPredicate toLongPredicate() {return l -> test((int) (l >>> INT_BIT), (int) (l & MASK));}
    default IntBiPredicate negate() {return (x, y) -> !test(x, y);}
    default IntBiPredicate and(final IntBiPredicate other) {return (x, y) -> test(x, y) && other.test(x, y);}
    default IntBiPredicate or(final IntBiPredicate other) {return (x, y) -> test(x, y) || other.test(x, y);}
    default IntBiPredicate xor(final IntBiPredicate other) {return (x, y) -> test(x, y) ^ other.test(x, y);}
}