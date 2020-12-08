package lib.util.function;

import java.util.function.LongPredicate;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface LongBiPredicate {
    boolean test(long x, long y);
    default LongPredicate curry(final long x) {return y -> test(x, y);}
    default LongBiPredicate negate() {return (x, y) -> !test(x, y);}
    default LongBiPredicate and(final LongBiPredicate other) {return (x, y) -> test(x, y) && other.test(x, y);}
    default LongBiPredicate or(final LongBiPredicate other) {return (x, y) -> test(x, y) || other.test(x, y);}
    default LongBiPredicate xor(final LongBiPredicate other) {return (x, y) -> test(x, y) ^ other.test(x, y);}
}