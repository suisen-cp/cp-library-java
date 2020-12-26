package lib.util.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Implementation of deque for generic type, using Ring Buffer.
 */
@SuppressWarnings("unchecked")
public class Deque<T> implements Iterable<T>, RandomAccess {
    static final int DEFAULT_CAPACITY = 1 << 6;
    T[] buf;
    int len = 1;
    int mask;
    int head = 0;
    int tail = 0;
    public Deque(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException(
                String.format("Capacity %d is negative.", capacity)
            );
        }
        while (this.len < capacity) {
            this.len <<= 1;
        }
        this.mask = this.len - 1;
        this.buf = (T[]) new Object[len];
    }
    public Deque() {
        this(DEFAULT_CAPACITY);
    }
    public T getLast() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[(tail - 1) & mask];
    }
    public T getFirst() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[head & mask];
    }
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException(
                String.format("Index %d out of bounds for length %d.", index, size())
            );
        }
        return buf[(head + index) & mask];
    }
    public void addLast(T v) {
        if (size() == len) grow();
        buf[tail++ & mask] = v;
    }
    public void addFirst(T v) {
        if (size() == len) grow();
        buf[--head & mask] = v;
    }
    public T removeLast() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[--tail & mask];
    }
    public T removeFirst() {
        if (size() == 0) throw new NoSuchElementException();
        return buf[head++ & mask];
    }
    public T pollLast() {
        if (size() == 0) return null;
        return removeLast();
    }
    public T pollFirst() {
        if (size() == 0) return null;
        return removeFirst();
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
    public T[] toArray(Class<T> clazz) {
        T[] ret = (T[]) Array.newInstance(clazz, size());
        Iterator<T> it = iterator();
        Arrays.setAll(ret, i -> it.next());
        return ret;
    }
    private void grow() {
        T[] newBuf = (T[]) new Object[len << 1];
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
    public Iterator<T> iterator() {
        //noinspection Convert2Diamond
        return new Iterator<T>(){
            int it = head;
            public boolean hasNext() {return it < tail;}
            public T next() {return buf[it++ & mask];}
        };
    }
    public Iterator<T> descendingIterator() {
        //noinspection Convert2Diamond
        return new Iterator<T>(){
            int it = tail;
            public boolean hasNext() {return it > head;}
            public T next() {return buf[--it & mask];}
        };
    }

    /***************************** DEBUG *********************************/

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }
}