package lib.tree.dp.function;

@FunctionalInterface
public interface ChildrenMerger<T> {
    public T merge(T mergingValue, T appendingValue);
}
