package lib.algebra.grouplike.properties;

@FunctionalInterface
public interface Invertible<T> {
    T inv(T x);
}
