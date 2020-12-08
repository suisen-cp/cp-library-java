package lib.algebra.grouplike;

import java.util.function.BinaryOperator;

import lib.algebra.grouplike.properties.Associative;

public interface SemiGroup<T> extends Magma<T>, Associative {
    static <T> SemiGroup<T> of(BinaryOperator<T> op) {
        return op::apply;
    }
}
