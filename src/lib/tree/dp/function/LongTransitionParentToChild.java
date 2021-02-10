package lib.tree.dp.function;

@FunctionalInterface
public interface LongTransitionParentToChild {
    public long transition(int parent, long value, int child);
}
