package lib.algebra.grouplike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import lib.algebra.grouplike.properties.Commutative;
import lib.algebra.grouplike.properties.Idempotent;

public interface BoundedSemiLattice<T> extends Monoid<T>, Commutative, Idempotent {
    static <T> BoundedSemiLattice<T> of(BinaryOperator<T> op, Supplier<T> id) {
        //noinspection Convert2Diamond
        return new BoundedSemiLattice<T>(){
            @Override public T id() {return id.get();}
            @Override public T op(T x, T y) {return op.apply(x, y);}
        };
    }
    static <T> BoundedSemiLattice<T> of(BinaryOperator<T> op, T id) {
        //noinspection Convert2Diamond
        return new BoundedSemiLattice<T>(){
            @Override public T id() {return id;}
            @Override public T op(T x, T y) {return op.apply(x, y);}
        };
    }
}
