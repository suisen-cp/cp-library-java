package lib.util.collections.ints;

import java.util.Iterator;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;

import lib.util.pair.IntEntry;

public class IntTreeSet implements Iterable<Integer> {
    private final IntTreeMap<Object> m = new IntTreeMap<>();
    private static final Object PRESENT = new Object();
    public PrimitiveIterator.OfInt iterator() {return new TreeSetIterator();}
    public PrimitiveIterator.OfInt descendingIterator() {return new DescendingTreeSetIterator();}
    public int size() {return m.size();}
    public boolean isEmpty() {return m.size() == 0;}
    public boolean contains(int key) {return m.containsKey(key);}
    public boolean add(int key) {return m.put(key, PRESENT) == null;}
    public boolean remove(int key) {return m.remove(key) == PRESENT;}
    public void clear() {m.clear();}
    public OptionalInt first() {return convertEntryToOptional(m.firstEntry());}
    public OptionalInt last () {return convertEntryToOptional(m.lastEntry ());}
    public OptionalInt lower  (int e) {return convertEntryToOptional(m.lowerEntry  (e));}
    public OptionalInt floor  (int e) {return convertEntryToOptional(m.floorEntry  (e));}
    public OptionalInt ceiling(int e) {return convertEntryToOptional(m.ceilingEntry(e));}
    public OptionalInt higher (int e) {return convertEntryToOptional(m.higherEntry (e));}
    public OptionalInt pollFirst() {return convertEntryToOptional(m.pollFirstEntry());}
    public OptionalInt pollLast()  {return convertEntryToOptional(m.pollLastEntry ());}
    private static OptionalInt convertEntryToOptional(IntEntry<?> e) {
        return e == null ? OptionalInt.empty() : OptionalInt.of(e.getKey());
    }
    private class TreeSetIterator implements PrimitiveIterator.OfInt {
        Iterator<IntTreeMap.Entry<Object>> it = m.iterator();
        public boolean hasNext() {return it.hasNext();}
        public int nextInt() {return it.next().key;}
    }
    private class DescendingTreeSetIterator implements PrimitiveIterator.OfInt {
        Iterator<IntTreeMap.Entry<Object>> it = m.descendingIterator();
        public boolean hasNext() {return it.hasNext();}
        public int nextInt() {return it.next().key;}
    }
}
