package lib.datastructure.longs;

import java.util.OptionalLong;
import java.util.PrimitiveIterator;

import lib.util.pair.LongEntry;

public class LongOrderedSet implements Iterable<Long> {
    private LongOrderedMap<Object> m = new LongOrderedMap<>();
    private static final Object PRESENT = new Object();
    public LongOrderedSet() {}
    private LongOrderedSet(LongOrderedMap<Object> m) {this.m = m;}
    public static LongOrderedSet merge(LongOrderedSet l, LongOrderedSet r) {
        return l.mergeRight(r);
    }
    public LongOrderedSet mergeLeft(LongOrderedSet l) {
        return new LongOrderedSet(LongOrderedMap.merge(l.m, m));
    }
    public LongOrderedSet mergeRight(LongOrderedSet r) {
        return new LongOrderedSet(LongOrderedMap.merge(m, r.m));
    }
    public LongOrderedSet splitRightUsingIndex(int k) {
        LongOrderedMap<Object> r = m.splitRightUsingIndex(k);
        return new LongOrderedSet(r);
    }
    public LongOrderedSet splitLeftUsingIndex(int k) {
        LongOrderedMap<Object> l = m.splitLeftUsingIndex(k);
        return new LongOrderedSet(l);
    }
    public LongOrderedSet splitRightUsingKey(long key) {
        LongOrderedMap<Object> r = m.splitRightUsingKey(key);
        return new LongOrderedSet(r);
    }
    public LongOrderedSet splitLeftUsingKey(long key) {
        LongOrderedMap<Object> l = m.splitLeftUsingKey(key);
        return new LongOrderedSet(l);
    }
    public boolean contains(long key) {
        return m.containsKey(key);
    }
    public boolean add(long key) {
        return m.put(key, PRESENT) == null;
    }
    public boolean remove(long key) {
        return m.remove(key) == PRESENT;
    }
    public OptionalLong removeKthElement(int k) {
        return convertEntryToOptional(m.removeKthEntry(k));
    }
    public OptionalLong kthElement(int k) {
        return convertEntryToOptional(m.kthEntry(k));
    }
    public OptionalLong first() {
        return convertEntryToOptional(m.firstEntry());
    }
    public OptionalLong last() {
        return convertEntryToOptional(m.lastEntry ());
    }
    public OptionalLong lower(long e) {
        return convertEntryToOptional(m.lowerEntry(e));
    }
    public OptionalLong floor(long e) {
        return convertEntryToOptional(m.floorEntry(e));
    }
    public OptionalLong ceiling(long e) {
        return convertEntryToOptional(m.ceilingEntry(e));
    }
    public OptionalLong higher (long e) {
        return convertEntryToOptional(m.higherEntry (e));
    }
    public OptionalLong removeFirst() {
        return convertEntryToOptional(m.removeKthEntry(0));
    }
    public OptionalLong removeLast() {
        return convertEntryToOptional(m.removeKthEntry(size() - 1));
    }
    private static OptionalLong convertEntryToOptional(LongEntry<?> e) {
        return e == null ? OptionalLong.empty() : OptionalLong.of(e.getKey());
    }
    public int countLessThan(long key) {
        return m.countLessThan(key);
    }
    public int countLessThanOrEqual(long key) {
        return m.countLessThanOrEqual(key);
    }
    public int countGreaterThan(long key) {
        return m.countGreaterThan(key);
    }
    public int countGreaterThanOrEqual(long key) {
        return m.countGreaterThanOrEqual(key);
    }
    public int countRange(long fromKey, long toKey) {
        return m.countRange(fromKey, toKey);
    }
    public int size() {
        return m.size();
    }
    public boolean isEmpty() {
        return m.size() == 0;
    }
    public void clear() {
        m.clear();
    }
    public PrimitiveIterator.OfLong iterator() {
        return m.keyIterator();
    }
    public PrimitiveIterator.OfLong descendingIterator() {
        return m.descendingKeyIterator();
    }
}