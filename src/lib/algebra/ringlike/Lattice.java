package lib.algebra.ringlike;

import java.util.function.BinaryOperator;

import lib.algebra.ringlike.properties.AdditiveAssociative;
import lib.algebra.ringlike.properties.AdditiveCommutative;
import lib.algebra.ringlike.properties.AdditiveIdempotent;
import lib.algebra.ringlike.properties.MultiplicativeAssociative;
import lib.algebra.ringlike.properties.MultiplicativeCommutative;
import lib.algebra.ringlike.properties.MultiplicativeIdempotent;

public interface Lattice<T> extends 
    Ringoid<T>,
    AdditiveAssociative, AdditiveCommutative, AdditiveIdempotent,
    MultiplicativeAssociative, MultiplicativeCommutative, MultiplicativeIdempotent
{
    static <T> Lattice<T> of(BinaryOperator<T> add, BinaryOperator<T> mul) {
        //noinspection Convert2Diamond
        return new Lattice<T>(){
            @Override public T add(T x, T y) {return add.apply(x, y);}
            @Override public T mul(T x, T y) {return mul.apply(x, y);}
        };
    }
}
