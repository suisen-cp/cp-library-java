package lib.tree.dp.function;

@FunctionalInterface
public interface ParentChildConsumer {
    public void accept(int parent, int child);
}
