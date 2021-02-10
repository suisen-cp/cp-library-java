package lib.tree.dp.function;

@FunctionalInterface
public interface LongTransitionChildrenToParent {
    public long transition(long mergedValue, int parent);
}
