package lib.tree.dp.function;

@FunctionalInterface
public interface LongChildrenMerger {
    public long merge(long mergingValue, long appendingValue);
}
