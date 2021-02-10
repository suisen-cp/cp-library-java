package lib.tree.dp.function;

@FunctionalInterface
public interface IntTransitionParentToChild {
    public int transition(int parent, int value, int child);
}
