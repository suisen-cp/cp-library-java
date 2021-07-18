package lib.util.function;

@FunctionalInterface
public interface ObjLongToLongFunction<T> {
    public long apply(T func, long x);
}
