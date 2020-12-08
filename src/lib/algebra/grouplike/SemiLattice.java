package lib.algebra.grouplike;

import java.util.function.BinaryOperator;

import lib.algebra.grouplike.properties.Commutative;
import lib.algebra.grouplike.properties.Idempotent;

public interface SemiLattice<T> extends SemiGroup<T>, Commutative, Idempotent {
    static <T> SemiLattice<T> of(BinaryOperator<T> op) {
        return op::apply;
    }
}
