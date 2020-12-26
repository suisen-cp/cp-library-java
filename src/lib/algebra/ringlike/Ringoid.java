package lib.algebra.ringlike;

import java.util.function.BinaryOperator;

public interface Ringoid<T> {
    T add(T x, T y);
    T mul(T x, T y);

    static <T> Ringoid<T> of(BinaryOperator<T> add, BinaryOperator<T> mul) {
        //noinspection Convert2Diamond
        return new Ringoid<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
        };
    }
}
