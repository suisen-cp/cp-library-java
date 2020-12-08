package lib.algebra.grouplike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import lib.algebra.grouplike.properties.Unital;

public interface Monoid<T> extends SemiGroup<T>, Unital<T> {
    static <T> Monoid<T> of(BinaryOperator<T> op, Supplier<T> id) {
        //noinspection Convert2Diamond
        return new Monoid<T>(){
            @Override public T id() {return id.get();}
            @Override public T op(T x, T y) {return op.apply(x, y);}
        };
    }
    static <T> Monoid<T> of(BinaryOperator<T> op, T id) {
        //noinspection Convert2Diamond
        return new Monoid<T>(){
            @Override public T id() {return id;}
            @Override public T op(T x, T y) {return op.apply(x, y);}
        };
    }
}
