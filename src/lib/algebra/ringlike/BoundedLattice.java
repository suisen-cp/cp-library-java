package lib.algebra.ringlike;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import lib.algebra.ringlike.properties.AdditiveUnital;
import lib.algebra.ringlike.properties.MultiplicativeUnital;

public interface BoundedLattice<T> extends Lattice<T>, AdditiveUnital<T>, MultiplicativeUnital<T> {
    static <T> BoundedLattice<T> of(BinaryOperator<T> add, Supplier<T> zero, BinaryOperator<T> mul, Supplier<T> one) {
        //noinspection Convert2Diamond
        return new BoundedLattice<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero.get();}
            @Override public T one() {return one.get();}
        };
    }
    static <T> BoundedLattice<T> of(BinaryOperator<T> add, T zero, BinaryOperator<T> mul, T one) {
        //noinspection Convert2Diamond
        return new BoundedLattice<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
            @Override public T zero() {return zero;}
            @Override public T one() {return one;}
        };
    }
}
