package lib.datastructure.ints;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

import lib.util.BitUtil;
import lib.util.collections.ints.IntArrayList;

public class IntRadixHeap {
    private final IntArrayList[] v;
    private int last, size;
    public IntRadixHeap() {
        this.v = new IntArrayList[32];
        for (int i = 0; i < 32; i++) v[i] = new IntArrayList();
        this.last = 0;
        this.size = 0;
    }
    public void push(int val) {
        if (val < last) {
            throw new IllegalArgumentException(
                String.format("Cannot push values less than the element popped last.\nval: %d, last: %d.", val, last)
            );
        }
        size++;
        v[BitUtil.bsr(val ^ last) + 1].add(val);
    }
    public int pop() {
        if (size == 0) throw new NoSuchElementException("Empty.");
        if (v[0].size() == 0) {
            int i = 1;
            while (v[i].size() == 0) i++;
            int min = v[i].get(0);
            PrimitiveIterator.OfInt it = v[i].iterator();
            while (it.hasNext()) min = Math.min(min, it.nextInt());
            it = v[i].iterator();
            while (it.hasNext()) {
                int e = it.nextInt();
                v[BitUtil.bsr(e ^ min) + 1].add(e);
            }
            last = min;
            v[i].clear();
        }
        size--;
        return v[0].removeLast();
    }
}