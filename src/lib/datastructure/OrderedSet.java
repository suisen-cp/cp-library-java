package lib.datastructure;

import java.util.Comparator;

public class OrderedSet<T> extends OrderedMultiSet<T> {
    public OrderedSet(Comparator<T> comparator) {
        super(comparator);
    }
    @Override
    Node insertKey(Node t, T key) {
        if (count(t, key) > 0) return t;
        return insert(t, leqCount(t, key), key, null);
    }
    @Override
    public void insertKey(T e) {
        root = insertKey(root, e);
    }
}