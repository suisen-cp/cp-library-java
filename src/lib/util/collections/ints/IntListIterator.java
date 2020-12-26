package lib.util.collections.ints;

import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface IntListIterator extends ListIterator<Integer> {
    boolean hasNext();
    int nextInt();
    boolean hasPrevious();
    int previousInt();
    void remove();
    void forEachRemaining(IntConsumer action);
    void forEachRemaining(Consumer<? super Integer> action);
    void add(int v);
    void set(int v);
    int nextIndex();
    int previousIndex();
}