package lib.util.collections;

import java.util.NoSuchElementException;

import lib.util.itertools.ExtendedPrimitiveIterator;

/**
 * @author https://atcoder.jp/users/suisen
 * 
 * Implementation of deque for primitive char type, using Ring Buffer.
 */
public final class CharDeque implements Iterable<Character> {
    private static final int DEFAULT_CAPACITY = 64;
    private char[] ringBuffer;
    private int head;
    private int tail;
    private int len;
    private int mask;
    private int size;
    public CharDeque(int capacity) {
        initialize(capacity);
    }
    public CharDeque() {
        this(DEFAULT_CAPACITY);
    }
    public void addFirst(char val) {
        if (size == len) grow();
        head = (head - 1 + len) & mask;
        ringBuffer[head] = val;
        size++;
    }
    public void addLast(char val) {
        if (size == len) grow();
        ringBuffer[tail] = val;
        tail = (tail + 1) & mask;
        size++;
    }
    public char removeFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        char ret = ringBuffer[head];
        head = (head + 1) & mask;
        size--;
        return ret;
    }
    public char removeLast() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        tail = (tail - 1 + len) & mask;
        char ret = ringBuffer[tail];
        size--;
        return ret;
    }
    public char get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        return ringBuffer[(head + index) & mask];
    }
    public char getFirst() {
        if (size == 0) throw new NoSuchElementException("Deque is empty.");
        return ringBuffer[head];
    }
    public char getLast() {
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
    public ExtendedPrimitiveIterator.OfChar iterator() {
        return new DequeIterator();
    }
    public ExtendedPrimitiveIterator.OfChar descendingIterator() {
        return new DescendingDequeIterator();
    }
    public char[] toArray() {
        char[] ret = new char[size];
        for (int i = 0; i < size; i++) ret[i] = ringBuffer[(head + i) & mask];
        return ret;
    }
    private void grow() {
        int len1 = len - head, len2 = len - len1;
        char[] newArray = new char[len << 1];
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
        this.ringBuffer = new char[k];
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

    private class DequeIterator implements ExtendedPrimitiveIterator.OfChar {
        private int it;
        public boolean hasNext() {return it < size;}
        public char nextChar() {return ringBuffer[(head + it++) & mask];}
    }
    private class DescendingDequeIterator implements ExtendedPrimitiveIterator.OfChar {
        private int it = size;
        public boolean hasNext() {return it > 0;}
        public char nextChar() {return ringBuffer[(head + --it) & mask];}
    }
}