package lib.util.collections.longs;

import lib.datastructure.longs.LongOrderedMap;
import lib.util.pair.LongEntry;

public class LongHashMap<V> {
    static final double THRESHOLD_EXPANSION = 0.75;
    static final int DEFAULT_CAPACITY = 1 << 6;

    int size = 0;
    int capacity = 1;
    int mask;
    LongOrderedMap<V>[] tab;
    public LongHashMap(int initialCapacity) {
        while (capacity < initialCapacity) capacity <<= 1;
        mask = capacity - 1;
        tab = new LongOrderedMap[capacity];
    }
    public LongHashMap() {
        this(DEFAULT_CAPACITY);
    }
    private static int hash(long key) {
        int hash = (int) (key ^ (key >>> 32));
        return hash ^ (hash >>> 16);
    }
    public boolean containsKey(long key) {
        LongOrderedMap<V> t = tab[hash(key) & mask];
        return t != null && t.containsKey(key);
    }
    public void put(long key, V val) {
        int index = hash(key) & mask;
        if (tab[index] == null) tab[index] = new LongOrderedMap<V>();
        if (tab[index].put(key, val) == null && capacity * THRESHOLD_EXPANSION <= ++size) grow();
    }
    public boolean remove(long key) {
        LongOrderedMap<V> mp = tab[hash(key) & mask];
        if (mp == null) return false;
        if (mp.remove(key) != null) {
            size--;
            return true;
        }
        return false;
    }
    public V get(long key) {
        int index = hash(key) & mask;
        if (tab[index] == null) return null;
        return tab[index].get(key);
    }
    public V getOrDefault(long key, V defaultValue) {
        int index = hash(key) & mask;
        if (tab[index] == null) return defaultValue;
        V val = tab[index].get(key);
        return val == null ? defaultValue : val;
    }
    public int size() {return size;}
    private void grow() {
        capacity <<= 1;
        mask = capacity - 1;
        LongOrderedMap<V>[] newTab = new LongOrderedMap[capacity];
        for (LongOrderedMap<V> mp : tab) {
            if (mp == null) continue;
            for (LongEntry<V> e : mp) {
                int index = hash(e.key) & mask;
                if (newTab[index] == null) newTab[index] = new LongOrderedMap<>();
                newTab[index].put(e.key, e.val);
            }
        }
        tab = newTab;
    }
}