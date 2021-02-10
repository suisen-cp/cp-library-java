package lib.datastructure;

import java.util.OptionalInt;
import java.util.PrimitiveIterator;

/**
 * Fast set for the limited range.
 */
public class RangeOrderedSet implements Iterable<Integer> {
    private static final int PRESENT = 1;
    private static final int ABSENT = 0;

    final int[] cnt;
    final int maxExclusive;
    final int n;
    final int bias;

    /**
     * @param l left bound of range (<b>inclusive</b>)
     * @param r right bound of range (<b>exclusive</b>)
     */
    public RangeOrderedSet(int l, int r) {
        this.bias = -l;
        this.n = ceilPow2(r - l);
        this.maxExclusive = inflate(r);
        this.cnt = new int[n << 1];
    }

    // * private methods *

    private static int ceilPow2(int n) {
        int k = 1;
        while (k < n) k <<= 1;
        return k;
    }

    private void updateCounter(int element, int num) {
        int k = inflate(element) + n;
        cnt[k] = num;
        while ((k >>= 1) > 0) cnt[k] = cnt[k << 1] + cnt[(k << 1) | 1];
    }

    private int inflate(int element) {
        return element + bias;
    }
    private int deflate(int index) {
        return index - bias;
    }

    private int clampLeft(int element) {
        return Math.max(0, inflate(element));
    }
    private int clampRight(int element) {
        return Math.min(maxExclusive, inflate(element));
    }

    private boolean indexOutOfRange(int index) {
        return index < 0 || index >= maxExclusive;
    }
    private boolean elementOutOfRange(int element) {
        return indexOutOfRange(inflate(element));
    }
    private void elementRangeCheck(int element) {
        if (elementOutOfRange(element)) {
            throw new AssertionError(
                String.format("%d is not in the range [%d, %d)", element, deflate(0), deflate(maxExclusive))
            );
        }
    }

    // * public methods *

    /**
     * @return {@code true} if this set contains the given element
     */
    public boolean contains(int element) {
        int index = inflate(element);
        if (indexOutOfRange(index)) return false;
        return cnt[index + n] > 0;
    }
    /**
     * Adds the given element to this set.
     * @param element the elemet to be added to this set
     * @return {@code true} if this set does not contain the given element
     */
    public boolean add(int element) {
        elementRangeCheck(element);
        if (contains(element)) {
            return false;
        } else {
            updateCounter(element, PRESENT);
            return true;
        }
    }
    /**
     * Removes the given element from this set.
     * @param element the elemet to be removed from this set
     * @return {@code true} if this set contains the given element
     */
    public boolean remove(int element) {
        if (contains(element)) {
            updateCounter(element, ABSENT);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Removes the specified element from this set if present.
     * @param optionalElement the optional elemet to be removed from this set
     * @return {@code true} if this set contains the given element
     */
    public boolean removeIfPresent(OptionalInt optionalElement) {
        if (optionalElement.isPresent()) {
            remove(optionalElement.getAsInt());
            return true;
        } else {
            return false;
        }
    }
    /**
     * Removes the smallest element from this set.
     * @return the smallest element, or {@code OptionalInt.empty()} if there is no such element.
     */
    public OptionalInt removeFirst() {
        OptionalInt elem = first();
        removeIfPresent(elem);
        return elem;
    }
    /**
     * Removes the largest element from this set if present.
     * @return the largest element, or {@code OptionalInt.empty()} if there is no such element.
     */
    public OptionalInt removeLast() {
        OptionalInt elem = last();
        removeIfPresent(elem);
        return elem;
    }
    /**
     * Returns the smallest element in this set, or {@code OptionalInt.empty()} if there is no such element.
     * @return the smallest element, or {@code OptionalInt.empty()} if there is no such element.
     */
    public OptionalInt first() {
        if (isEmpty()) OptionalInt.empty();
        int k = 1;
        while (k < n) {
            k <<= 1;
            if (cnt[k] == 0) k |= 1;
        }
        return OptionalInt.of(deflate(k - n));
    }
    /**
     * Returns the largest element in this set, or {@code OptionalInt.empty()} if there is no such element.
     * @return the largest element, or {@code OptionalInt.empty()} if there is no such element.
     */
    public OptionalInt last() {
        if (isEmpty()) OptionalInt.empty();
        int k = 1;
        while (k < n) {
            k <<= 1;
            if (cnt[k | 1] > 0) k |= 1;
        }
        return OptionalInt.of(deflate(k - n));
    }
    /**
     * Returns the k-th smallest element in this set, or {@code OptionalInt.empty()} if there is no such element.
     * @param k 0-indexed
     * @return the k-th smallest element, or {@code OptionalInt.empty()} if there is no such element.
     */
    public OptionalInt kthElement(int index) {
        if (size() <= index) OptionalInt.empty();
        int k = 1;
        int sum = 0;
        index++;
        while (k < n) {
            k <<= 1;
            if (sum + cnt[k] < index) {
                sum += cnt[k];
                k |= 1;
            }
        }
        return OptionalInt.of(deflate(k - n));
    }
    /**
     * Returns the greatest element in this set strictly less than the given element, or {@code OptionalInt.empty()} if there is no such element.
     * @param element
     * @return the greatest element in this set strictly less than the given element, or {@code OptionalInt.empty()} if there is no such element
     */
    public OptionalInt lower(int element) {
        int r = clampRight(element);
        if (indexOutOfRange(r - 1)) return OptionalInt.empty();
        r += n;
        do {
            r--;
            while (r > 1 && (r & 1) == 1) r >>= 1;
            if (cnt[r] > 0) {
                while (r < n) if (cnt[r = r << 1 | 1] == 0) r ^= 1;
                return OptionalInt.of(deflate(r - n));
            }
        } while ((r & -r) != r);
        return OptionalInt.empty();
    }
    /**
     * Returns the greatest element in this set less than or equal to the given element, or {@code OptionalInt.empty()} if there is no such element.
     * @param element
     * @return the greatest element in this set less than or equal to the given element, or {@code OptionalInt.empty()} if there is no such element
     */
    public OptionalInt floor(int element) {return lower(element + 1);}
    /**
     * Returns the least element in this set strictly greater than the given element, or {@code OptionalInt.empty()} if there is no such element.
     * @param element
     * @return the least element in this set strictly greater than the given element, or {@code OptionalInt.empty()} if there is no such element
     */
    public OptionalInt higher(int element) {return ceiling(element + 1);}
    /**
     * Returns the least element in this set greater than or equal to the given element, or {@code OptionalInt.empty()} if there is no such element.
     * @param element
     * @return the least element in this set greater than or equal to the given element, or {@code OptionalInt.empty()} if there is no such element
     */
    public OptionalInt ceiling(int element) {
        int l = clampLeft(element);
        if (indexOutOfRange(l)) return OptionalInt.empty();
        l += n;
        do {
            while ((l & 1) == 0) l >>= 1;
            if (cnt[l] != 0) {
                while (l < n) if (cnt[l = l << 1] == 0) l |= 1;
                return OptionalInt.of(deflate(l - n));
            }
            l++;
        } while ((l & -l) != l);
        return OptionalInt.empty();
    }
    /**
     * @return the number of elements in this set
     */
    public int size() {return cnt[1];}
    /**
     * @param l left bound of the range (<b>inclusive</b>)
     * @param r right bound of the range (<b>exclusive</b>)
     * @return the number of elements in the range [l, r)
     */
    public int count(int l, int r) {
        int res = 0;
        for (l = clampLeft(l) + n, r = clampRight(r) + n; r > l; l >>= 1, r >>= 1) {
            if ((l & 1) == 1) res += cnt[l++];
            if ((r & 1) == 1) res += cnt[--r];
        }
        return res;
    }
    /**
     * @return {@code true} if this set contains no elements
     */
    public boolean isEmpty() {return size() == 0;}

    private PrimitiveIterator.OfInt iterator(OptionalInt from) {
        return new PrimitiveIterator.OfInt(){
            private OptionalInt pre = OptionalInt.empty();
            private OptionalInt cur = from;
            public boolean hasNext() {return cur.isPresent();}
            public int nextInt() {
                int ret = (pre = cur).getAsInt();
                cur = higher(ret);
                return ret;
            }
            public void remove() {removeIfPresent(pre);}
        };
    }
    public PrimitiveIterator.OfInt iterator() {return iterator(first());}
    public PrimitiveIterator.OfInt iteratorFromKthElement(int k) {return iterator(kthElement(k));}
    public PrimitiveIterator.OfInt iteratorFromCeilingElement(int element) {return iterator(ceiling(element));}
    public PrimitiveIterator.OfInt iteratorFromHigherElement(int element) {return iterator(higher(element));}
    public PrimitiveIterator.OfInt iteratorFromFloorElement(int element) {return iterator(floor(element));}
    public PrimitiveIterator.OfInt iteratorFromLowerElement(int element) {return iterator(lower(element));}
    
    private PrimitiveIterator.OfInt descendingIterator(OptionalInt from) {
        return new PrimitiveIterator.OfInt(){
            private OptionalInt pre = OptionalInt.empty();
            private OptionalInt cur = from;
            public boolean hasNext() {return cur.isPresent();}
            public int nextInt() {
                int ret = (pre = cur).getAsInt();
                cur = lower(ret);
                return ret;
            }
            public void remove() {removeIfPresent(pre);}
        };
    }
    public PrimitiveIterator.OfInt descendingIterator() {return iterator(first());}
    public PrimitiveIterator.OfInt descendingIteratorFromKthElement(int k) {return descendingIterator(kthElement(k));}
    public PrimitiveIterator.OfInt descendingIteratorFromCeilingElement(int element) {return descendingIterator(ceiling(element));}
    public PrimitiveIterator.OfInt descendingIteratorFromHigherElement(int element) {return descendingIterator(higher(element));}
    public PrimitiveIterator.OfInt descendingIteratorFromFloorElement(int element) {return descendingIterator(floor(element));}
    public PrimitiveIterator.OfInt descendingIteratorFromLowerElement(int element) {return descendingIterator(lower(element));}

    /**
     * Returns an array containing all of the elements in this set in the ascending order.
     * @return an array containing all of the elements in this set in the ascending order
     */
    public int[] toArray() {
        int[] res = new int[size()];
        PrimitiveIterator.OfInt iter = iterator();
        for (int i = 0; iter.hasNext();) {
            while (iter.hasNext()) res[i++] = iter.nextInt();
        }
        return res;
    }
}