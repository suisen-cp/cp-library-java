package lib.tree.dp.function;

@FunctionalInterface
public interface IntTransitionChildrenToParent {
    public int transition(int mergedValue, int parent);
}
