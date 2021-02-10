package lib.util.function;

@FunctionalInterface
public interface IntBiFunction<R> {
    public R apply(int x, int y);
}
