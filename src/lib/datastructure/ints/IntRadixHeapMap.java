package lib.datastructure.ints;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class IntRadixHeapMap<V> {
    private final Entry[][] vecs;
    private final int[] tls;
    private int last;
    private int size;
    public IntRadixHeapMap() {
        this.vecs = (Entry[][]) Array.newInstance(Entry.class, 32, 64);
        this.tls = new int[32];
        this.last = 0;
        this.size = 0;
    }
    public void push(int k, V v) {
        if (k < last) {
            throw new IllegalArgumentException(
                String.format("Cannot push values less than the element popped last.\nval: %d, last: %d.", k, last)
            );
        }
        size++;
        int bsr = 32 - Integer.numberOfLeadingZeros(k ^ last);
        if (tls[bsr] == vecs[bsr].length) grow(bsr);
        vecs[bsr][tls[bsr]++] = new Entry(k, v);
    }
    public Entry pop() {
        if (size == 0) throw new NoSuchElementException("Empty.");
        if (tls[0] == 0) {
            int i = 1;
            while (tls[i] == 0) i++;
            Entry[] vec = vecs[i];
            int min = vec[0].key;
            for (int j = 1, l = tls[i]; j < l; j++) min = Math.min(min, vec[j].key);
            for (int j = 0, l = tls[i]; j < l; j++) {
                int bsr = 32 - Integer.numberOfLeadingZeros(vec[j].key ^ min);
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
        public int key; 
        public V val;
        private Entry(int key, V val) {
            this.key = key;
            this.val = val;
        }
    }
}