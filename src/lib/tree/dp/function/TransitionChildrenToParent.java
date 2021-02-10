package lib.tree.dp.function;

@FunctionalInterface
public interface TransitionChildrenToParent<T> {
    public T transition(T merged, int parent);
}
