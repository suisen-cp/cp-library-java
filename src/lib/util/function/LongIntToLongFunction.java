package lib.util.function;

import java.util.function.IntToLongFunction;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface LongIntToLongFunction {
    long apply(long l, int i);
    default IntToLongFunction curry(final long l) {return i -> apply(l, i);}
}