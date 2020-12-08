package lib.datastructure;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public class RangeOrderedMultiSet extends RangeOrderedSet {
    public RangeOrderedMultiSet(final int l, final int r) {super(l, r);}
    @Override
    public void add(final int i) {add(i, 1);}
    public void add(final int i, final int num) {update(bias + i, count(i) + num);}
    public void addIfAbsent(final int i) {super.add(i);}
    @Override
    public void remove(final int i) {remove(i, 1);}
    public void removeAll(final int i) {remove(i, count(i));}
    public void remove(final int i, final int num) {
        if (num == 0) return;
        final int c = count(i);
        if (c < num) throw new NoSuchElementException();
        if (c > 0) update(bias + i, c - num);
    }
    public int count(int i) {return cnt[bias + i + n];}
    @Override
    public PrimitiveIterator.OfInt iterator() {return new AscendingMultiSetIterator();}
    @Override
    public PrimitiveIterator.OfInt descendingIterator() {return new DescendingMultiSetIterator();}
    class AscendingMultiSetIterator extends AscendingSetIterator {
        int num = 0;
        @Override
        public boolean hasNext() {
            if (num < count(it)) {
                num++;
                return true;
            } else return it <= max;
        }
        @Override
        public int nextInt() {
            int ret = it;
            if (num == count(it)) {
                num = 0;
                it = it == max ? Integer.MAX_VALUE : higher(it);
            }
            return ret;
        }
    }
    class DescendingMultiSetIterator extends DescendingSetIterator {
        int num = 1;
        @Override
        public boolean hasNext() {
            return num < count(it) || it >= min;
        }
        @Override
        public int nextInt() {
            int ret = it;
            if (num < count(it)) {
                num++;
            } else {
                num = 1;
                it = it == min ? Integer.MIN_VALUE : lower(it);
            }
            return ret;
        }
    }
}