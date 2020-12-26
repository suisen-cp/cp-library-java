package lib.algebra.grouplike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import lib.algebra.grouplike.properties.Commutative;

public interface AbelianGroup<T> extends Group<T>, Commutative {
    static <T> AbelianGroup<T> of(BinaryOperator<T> op, Supplier<T> id, UnaryOperator<T> inv) {
        //noinspection Convert2Diamond
        return new AbelianGroup<T>(){
            @Override public T id() {return id.get();}
            @Override public T inv(T x) {return inv.apply(x);}
            @Override public T op(T x, T y) {return op.apply(x, y);}
        };
    }
    static <T> AbelianGroup<T> of(BinaryOperator<T> op, T id, UnaryOperator<T> inv) {
        //noinspection Convert2Diamond
        return new AbelianGroup<T>(){
            @Override public T id() {return id;}
            @Override public T inv(T x) {return inv.apply(x);}
            @Override public T op(T x, T y) {return op.apply(x, y);}
        };
    }
}
