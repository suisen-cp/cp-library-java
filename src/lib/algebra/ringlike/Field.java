package lib.algebra.ringlike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import lib.algebra.ringlike.properties.MultiplicativeInvertible;

public interface Field<T> extends CommutativeRing<T>, MultiplicativeInvertible<T> {
    static <T> Field<T> of(BinaryOperator<T> add, Supplier<T> zero, UnaryOperator<T> addInv, BinaryOperator<T> mul, Supplier<T> one, UnaryOperator<T> mulInv) {
        //noinspection Convert2Diamond
        return new Field<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero.get();}
            @Override public T one() {return one.get();}
            @Override public T addInv(T x) {return addInv.apply(x);}
            @Override public T mulInv(T x) {return mulInv.apply(x);}
        };
    }
    static <T> Field<T> of(BinaryOperator<T> add, T zero, UnaryOperator<T> addInv, BinaryOperator<T> mul, T one, UnaryOperator<T> mulInv) {
        //noinspection Convert2Diamond
        return new Field<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero;}
            @Override public T one() {return one;}
            @Override public T addInv(T x) {return addInv.apply(x);}
            @Override public T mulInv(T x) {return mulInv.apply(x);}
        };
    }
}
