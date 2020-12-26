package lib.util.collections.longs;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Implementation of deque for primitive long type, using Ring Buffer.
 */
public final class LongDeque implements Iterable<Long> {
    static final int DEFAULT_CAPACITY = 1 << 6;
    long[] buf;
    int len = 1;
    int mask;
    int head = 0;
    int tail = 0;
    public LongDeque(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                String.format("Capacity %d is negative.", capacity)
            );
        }
        while (this.len < capacity) {
            this.len <<= 1;
        }
        this.mask = this.len - 1;
        this.buf = new long[len];
    }
    public LongDeque() {
        this(DEFAULT_CAPACITY);
    }
    public long getLast() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[(tail - 1) & mask];
    }
    public long getFirst() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[head & mask];
    }
    public long get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for length %d.", index, size())
            );
        }
        return buf[(head + index) & mask];
    }
    public void addLast(long v) {
        if (size() == len) grow();
        buf[tail++ & mask] = v;
    }
    public void addFirst(long v) {
        if (size() == len) grow();
        buf[--head & mask] = v;
    }
    public long removeLast() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[--tail & mask];
    }
    public long removeFirst() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[head++ & mask];
    }
    public OptionalLong pollLast() {
        if (size() == 0) return OptionalLong.empty();
        return OptionalLong.of(removeLast());
    }
    public OptionalLong pollFirst() {
        if (size() == 0) return OptionalLong.empty();
        return OptionalLong.of(removeFirst());
    }
    public int size() {
        return tail - head;
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public void clear() {
        head = tail = 0;
    }
    public long[] toArray() {
        long[] ret = new long[size()];
        PrimitiveIterator.OfLong it = iterator();
        Arrays.setAll(ret, i -> it.nextLong());
        return ret;
    }
    private void grow() {
        long[] newBuf = new long[len << 1];
        head &= mask;
        tail &= mask;
        int len1 = len - head;
        int len2 = head;
        System.arraycopy(buf, head, newBuf, 0, len1);
        System.arraycopy(buf, 0, newBuf, len1, len2);
        this.head = 0;
        this.tail = this.len;
        this.len <<= 1;
        this.mask = this.len - 1;
        this.buf = newBuf;
    }
    public PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong(){
            int it = head;
            public boolean hasNext() {return it < tail;}
            public long nextLong() {return buf[it++ & mask];}
        };
    }
    public PrimitiveIterator.OfLong descendingIterator() {
        return new PrimitiveIterator.OfLong(){
            int it = tail;
            public boolean hasNext() {return it > head;}
            public long nextLong() {return buf[--it & mask];}
        };
    }

    /***************************** DEBUG *********************************/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        PrimitiveIterator.OfLong it = iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }
}