package lib.util.function;

@FunctionalInterface
public interface ObjIntToObjFunction<T, U> {
    U apply(T t, int i);
}