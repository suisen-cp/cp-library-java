package lib.algebra.ringlike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import lib.algebra.ringlike.properties.AdditiveInvertible;

public interface Ring<T> extends SemiRing<T>, AdditiveInvertible<T> {
    static <T> Ring<T> of(BinaryOperator<T> add, Supplier<T> zero, UnaryOperator<T> addInv, BinaryOperator<T> mul, Supplier<T> one) {
        //noinspection Convert2Diamond
        return new Ring<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero.get();}
            @Override public T one() {return one.get();}
            @Override public T addInv(T x) {return addInv.apply(x);}
        };
    }
    static <T> Ring<T> of(BinaryOperator<T> add, T zero, UnaryOperator<T> addInv, BinaryOperator<T> mul, T one) {
        //noinspection Convert2Diamond
        return new Ring<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero;}
            @Override public T one() {return one;}
            @Override public T addInv(T x) {return addInv.apply(x);}
        };
    }
}
