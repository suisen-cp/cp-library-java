package lib.util.function;

@FunctionalInterface
public interface ObjLongToObjFunction<T, U> {
    U apply(T t, long i);
}