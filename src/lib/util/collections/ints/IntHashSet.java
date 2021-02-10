package lib.util.collections.ints;

import java.util.PrimitiveIterator;

public class IntHashSet {
    private static final Object PRESENT = new Object();

    private final IntHashMap<Object> map = new IntHashMap<>();

    public void add(int v) {
        map.put(v, PRESENT);
    }
    public boolean remove(int v) {
        return map.remove(v);
    }
    public int size() {
        return map.size();
    }
    // public boolean contains(int v) {
    //     return map.
    // }
}
