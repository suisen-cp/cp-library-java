package lib.datastructure.longs;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

import lib.util.BitUtil;
import lib.util.collections.longs.LongArrayList;

public class LongRadixHeap {
    private final LongArrayList[] v;
    private long last;
    private int size;
    public LongRadixHeap() {
        this.v = new LongArrayList[64];
        for (int i = 0; i < 64; i++) v[i] = new LongArrayList();
        this.last = 0;
        this.size = 0;
    }
    public void push(long val) {
        if (val < last) {
            throw new IllegalArgumentException(
                String.format("Cannot push values less than the element popped last.\nval: %d, last: %d.", val, last)
            );
        }
        size++;
        v[BitUtil.bsr(val ^ last) + 1].add(val);
    }
    public long pop() {
        if (size == 0) throw new NoSuchElementException("Empty.");
        if (v[0].size() == 0) {
            int i = 1;
            while (v[i].size() == 0) i++;
            long min = v[i].get(0);
            PrimitiveIterator.OfLong it = v[i].iterator();
            while (it.hasNext()) min = Math.min(min, it.nextLong());
            it = v[i].iterator();
            while (it.hasNext()) {
                long e = it.nextLong();
                v[BitUtil.bsr(e ^ min) + 1].add(e);
            }
            last = min;
            v[i].clear();
        }
        size--;
        return v[0].removeLast();
    }
}