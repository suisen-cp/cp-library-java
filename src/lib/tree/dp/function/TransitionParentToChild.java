package lib.tree.dp.function;

@FunctionalInterface
public interface TransitionParentToChild<T> {
    public T transition(int parent, T value, int child);
}
