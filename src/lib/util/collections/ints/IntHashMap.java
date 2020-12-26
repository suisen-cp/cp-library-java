package lib.util.collections.ints;

import lib.datastructure.ints.IntOrderedMap;
import lib.util.pair.IntEntry;

public class IntHashMap<V> {
    static final double THRESHOLD_EXPANSION = 0.75;
    static final int DEFAULT_CAPACITY = 1 << 6;

    int size = 0;
    int capacity = 1;
    int mask;
    IntOrderedMap<V>[] tab;
    public IntHashMap(int initialCapacity) {
        while (capacity < initialCapacity) capacity <<= 1;
        mask = capacity - 1;
        tab = new IntOrderedMap[capacity];
    }
    public IntHashMap() {
        this(DEFAULT_CAPACITY);
    }
    private static int hash(int key) {
        return key ^ (key >>> 16);
    }
    public void put(int key, V val) {
        int index = hash(key) & mask;
        if (tab[index] == null) tab[index] = new IntOrderedMap<V>();
        if (tab[index].put(key, val) && capacity * THRESHOLD_EXPANSION <= ++size) grow();
    }
    public boolean remove(int key) {
        IntOrderedMap<V> mp = tab[hash(key) & mask];
        if (mp == null) return false;
        if (mp.remove(key)) {
            size--;
            return true;
        }
        return false;
    }
    public V get(int key) {
        int index = hash(key) & mask;
        if (tab[index] == null) return null;
        return tab[index].get(key);
    }
    public V getOrDefault(int key, V defaultValue) {
        int index = hash(key) & mask;
        if (tab[index] == null) return defaultValue;
        V val = tab[index].get(key);
        return val == null ? defaultValue : val;
    }
    public int size() {return size;}
    private void grow() {
        capacity <<= 1;
        mask = capacity - 1;
        IntOrderedMap<V>[] newTab = new IntOrderedMap[capacity];
        for (IntOrderedMap<V> mp : tab) {
            if (mp == null) continue;
            for (IntEntry<V> e : mp) {
                int index = hash(e.key) & mask;
                if (newTab[index] == null) newTab[index] = new IntOrderedMap<>();
                newTab[index].put(e.key, e.val);
            }
        }
        tab = newTab;
    }
}