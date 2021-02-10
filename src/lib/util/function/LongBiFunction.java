package lib.util.function;

@FunctionalInterface
public interface LongBiFunction<R> {
    public R apply(long x, long y);
}
