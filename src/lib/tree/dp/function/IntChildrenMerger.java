package lib.tree.dp.function;

@FunctionalInterface
public interface IntChildrenMerger {
    public int merge(int mergingValue, int appendingValue);
}
