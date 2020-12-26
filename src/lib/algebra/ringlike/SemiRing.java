package lib.algebra.ringlike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import lib.algebra.ringlike.properties.AdditiveAssociative;
import lib.algebra.ringlike.properties.AdditiveCommutative;
import lib.algebra.ringlike.properties.AdditiveUnital;
import lib.algebra.ringlike.properties.MultiplicativeAssociative;
import lib.algebra.ringlike.properties.MultiplicativeUnital;

public interface SemiRing<T> extends
    Ringoid<T>,
    AdditiveAssociative, AdditiveUnital<T>, AdditiveCommutative,
    MultiplicativeAssociative, MultiplicativeUnital<T>
{
    static <T> SemiRing<T> of(BinaryOperator<T> add, Supplier<T> zero, BinaryOperator<T> mul, Supplier<T> one) {
        //noinspection Convert2Diamond
        return new SemiRing<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero.get();}
            @Override public T one() {return one.get();}
        };
    }

    static <T> SemiRing<T> of(BinaryOperator<T> add, T zero, BinaryOperator<T> mul, T one) {
        //noinspection Convert2Diamond
        return new SemiRing<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero;}
            @Override public T one() {return one;}
        };
    }
}
