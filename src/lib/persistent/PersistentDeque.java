package lib.persistent;

import lib.util.pair.Pair;

public class PersistentDeque<T> {
    private final PersistentArray<T> queue;
    private final int k;
    private final int mask;
    private int hd, tl;
    public PersistentDeque(int maxSize, T initialValue) {
        int k = 1;
        while (k < maxSize) k <<= 1;
        this.queue = new PersistentArray<>(k, initialValue);
        this.k = k;
        this.mask = k - 1;
    }
    private PersistentDeque(PersistentArray<T> queue, int hd, int tl, int k, int mask) {
        this.queue = queue;
        this.hd = hd;
        this.tl = tl;
        this.k = k;
        this.mask = mask;
    }
    public PersistentDeque<T> addFirst(T e) {
        PersistentArray<T> newQueue = queue.set((hd - 1) & mask, e);
        return new PersistentDeque<>(newQueue, hd - 1, tl, k, mask);
    }
    public PersistentDeque<T> addLast(T e) {
        PersistentArray<T> newQueue = queue.set(tl & mask, e);
        return new PersistentDeque<>(newQueue, hd, tl + 1, k, mask);
    }
    public Pair<T, PersistentDeque<T>> removeFirst() {
        T e = queue.get(hd & mask);
        PersistentArray<T> newQueue = queue.set(hd & mask, null);
        return new Pair<>(e, new PersistentDeque<>(newQueue, hd + 1, tl, k, mask));
    }
    public Pair<T, PersistentDeque<T>> removeLast() {
        T e = queue.get((tl - 1) & mask);
        PersistentArray<T> newQueue = queue.set((tl - 1) & mask, null);
        return new Pair<>(e, new PersistentDeque<>(newQueue, hd, tl - 1, k, mask));
    }
}
