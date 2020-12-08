package lib.util.function;

import java.util.function.LongConsumer;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface LongBiConsumer {
    void accept(long x, long y);
    default LongConsumer curry(final int x) {return y -> accept(x, y);}
}