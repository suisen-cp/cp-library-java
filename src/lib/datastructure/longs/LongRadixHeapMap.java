package lib.datastructure.longs;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class LongRadixHeapMap<V> {
    private final Entry[][] vecs;
    private final int[] tls;
    private long last;
    private int size;
    public LongRadixHeapMap() {
        this.vecs = (Entry[][]) Array.newInstance(Entry.class, 64, 64);
        this.tls = new int[64];
        this.last = 0;
        this.size = 0;
    }
    public void push(long k, V v) {
        if (k < last) {
            throw new IllegalArgumentException(
                String.format("Cannot push values less than the element popped last.\nval: %d, last: %d.", k, last)
            );
        }
        size++;
        int bsr = 64 - Long.numberOfLeadingZeros(k ^ last);
        if (tls[bsr] == vecs[bsr].length) grow(bsr);
        vecs[bsr][tls[bsr]++] = new Entry(k, v);
    }
    public Entry pop() {
        if (size == 0) throw new NoSuchElementException("Empty.");
        if (tls[0] == 0) {
            int i = 1;
            while (tls[i] == 0) i++;
            Entry[] vec = vecs[i];
            long min = vec[0].key;
            for (int j = 1, l = tls[i]; j < l; j++) min = Math.min(min, vec[j].key);
            for (int j = 0, l = tls[i]; j < l; j++) {
                int bsr = 64 - Long.numberOfLeadingZeros(vec[j].key ^ min);
                if (tls[bsr] == vecs[bsr].length) grow(bsr);
                vecs[bsr][tls[bsr]++] = vec[j];
            }
            last = min;
            tls[i] = 0;
        }
        size--;
        return vecs[0][--tls[0]];
    }

    public int size() {
        return size;
    }

    private void grow(int i) {
        int oldLen = vecs[i].length;
        int newLen = oldLen << 1;
        Entry[] newVec = (Entry[]) Array.newInstance(Entry.class, newLen);
        System.arraycopy(vecs[i], 0, newVec, 0, oldLen);
        vecs[i] = newVec;
    }

    public final class Entry {
        public long key; 
        public V val;
        private Entry(long key, V val) {
            this.key = key;
            this.val = val;
        }
    }
}