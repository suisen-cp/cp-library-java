package lib.util.collections;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Implementation of deque for primitive double type, using Ring Buffer.
 */
public final class DoubleDeque implements Iterable<Double> {
    private static final int DEFAULT_CAPACITY = 64;
    private double[] ringBuffer;
    private int head;
    private int tail;
    private int len;
    private int mask;
    private int size;
    public DoubleDeque(int capacity) {
        initialize(capacity);
    }
    public DoubleDeque() {
        this(DEFAULT_CAPACITY);
    }
    public void addFirst(double val) {
        if (size == len) grow();
        head = (head - 1 + len) & mask;
        ringBuffer[head] = val;
        size++;
    }
    public void addLast(double val) {
        if (size == len) grow();
        ringBuffer[tail] = val;
        tail = (tail + 1) & mask;
        size++;
    }
    public double removeFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        double ret = ringBuffer[head];
        head = (head + 1) & mask;
        size--;
        return ret;
    }
    public double removeLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        tail = (tail - 1 + len) & mask;
        double ret = ringBuffer[tail];
        size--;
        return ret;
    }
    public double get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        return ringBuffer[(head + index) & mask];
    }
    public double getFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        return ringBuffer[head];
    }
    public double getLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        return ringBuffer[(tail - 1 + len) & mask];
    }
    public int size() {
        return size;
    }
    public void clear() {
        head = tail = 0;
    }
    public void clear(int newCapacity) {
        initialize(newCapacity);
    }
    public PrimitiveIterator.OfDouble iterator() {
        return new DequeIterator();
    }
    public PrimitiveIterator.OfDouble descendingIterator() {
        return new DescendingDequeIterator();
    }
    public double[] toArray() {
        double[] ret = new double[size];
        for (int i = 0; i < size; i++) ret[i] = ringBuffer[(head + i) & mask];
        return ret;
    }
    private void grow() {
        int len1 = len - head, len2 = len - len1;
        double[] newArray = new double[len << 1];
        System.arraycopy(ringBuffer, head, newArray, 0, len1);
        System.arraycopy(ringBuffer, 0, newArray, len1, len2);
        ringBuffer = newArray;
        len <<= 1;
        mask = len - 1;
        head = 0; tail = size;
    }
    private void initialize(int capacity) {
        int k = 1; while (k < capacity) k <<= 1;
        this.len = k;
        this.mask = k - 1;
        this.ringBuffer = new double[k];
        this.head = 0;
        this.tail = 0;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(ringBuffer[(head + i) & mask]);
            if (i < size - 1) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    private class DequeIterator implements PrimitiveIterator.OfDouble {
        private int it;
        public boolean hasNext() {return it < size;}
        public double nextDouble() {return ringBuffer[(head + it++) & mask];}
    }
    private class DescendingDequeIterator implements PrimitiveIterator.OfDouble {
        private int it = size;
        public boolean hasNext() {return it > 0;}
        public double nextDouble() {return ringBuffer[(head + --it) & mask];}
    }
}