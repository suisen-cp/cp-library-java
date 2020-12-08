package lib.util.function;

import java.util.function.IntToLongFunction;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface IntToLongBiFunction {
    long apply(int x, int y);
    default IntToLongFunction curry(final int x) {return y -> apply(x, y);}
}