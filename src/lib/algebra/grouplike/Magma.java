package lib.algebra.grouplike;

import java.util.function.BinaryOperator;

@FunctionalInterface
public interface Magma<T> {
    T op(T x, T y);
    static <T> Magma<T> of(BinaryOperator<T> op) {
        return op::apply;
    }
}
