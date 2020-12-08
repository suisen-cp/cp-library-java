package lib.util.function;

import java.util.function.Consumer;

@FunctionalInterface
public interface IntObjConsumer<T> {
    void accept(int x, T y);
    default Consumer<T> curry(final int x) {return y -> accept(x, y);}
}