package lib.util.function;

import java.util.function.IntConsumer;

/**
 * @author https://atcoder.jp/users/suisen
 */
@FunctionalInterface
public interface IntBiConsumer {
    void accept(int x, int y);
    default IntConsumer curry(final int x) {return y -> accept(x, y);}
}