package lib.util.function;

import java.util.function.LongUnaryOperator;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface IntLongToLongFunction {
    long apply(int i, long l);
    default LongUnaryOperator curry(final int i) {return l -> apply(i, l);}
}