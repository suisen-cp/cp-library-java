package lib.datastructure;

import java.util.OptionalInt;
import java.util.PrimitiveIterator;

public class RangeOrderedMultiSet {
    final int[] cnt;
    final int maxExclusive;
    final int n;
    final int bias;

    /**
     * @param l left bound of range (<b>inclusive</b>)
     * @param r right bound of range (<b>exclusive</b>)
     */
    public RangeOrderedMultiSet(int l, int r) {
        this.bias = -l;
        int w = r - l;
        int nn = 1; while (nn < w) nn <<= 1;
        this.n = nn;
        this.maxExclusive = l + w;
        this.cnt = new int[n << 1];
    }

    public int add(int i) {
        return add(i, 1);
    }
    public int add(int i, int num) {
        int sum = count(i) + num;
        update(bias + i, sum);
        return sum;
    }
    public boolean addIfAbsent(int i) {
        if (contains(i)) return false;
        update(bias + i, 1);
        return true;
    }

    public boolean remove(int i) {
        if (!contains(i)) return false;
        update(bias + i, 0);
        return true;
    }
    /**
     * trys to remove specified <code>num</code> elements from this set and returns the actual number of removed elements.
     * @param element the element to be removed
     * @param num the number of elements to be removed
     * @return the actual number of removed elements
     */
    public int remove(int element, int num) {
        if (num <= 0) return 0;
        int c = count(element);
        int rem = Math.min(c, num);
        if (rem > 0) update(bias + element, c - rem);
        return rem;
    }
    public int removeAll(int i) {
        return remove(i, count(i));
    }

    public OptionalInt removeFirst() {
        if (cnt[1] == 0) OptionalInt.empty();
        int k = 1;
        while (k < n) {
            cnt[k]--;
            k <<= 1;
            if (cnt[k] == 0) k |= 1;
        }
        cnt[k]--;
        return OptionalInt.of(k - n - bias);
    }

    public OptionalInt removeLast() {
        if (cnt[1] == 0) OptionalInt.empty();
        int k = 1;
        while (k < n) {
            cnt[k]--;
            k <<= 1;
            if (cnt[k | 1] > 0) k |= 1;
        }
        cnt[k]--;
        return OptionalInt.of(k - n - bias);
    }

    public boolean contains(int i) {
        return cnt[bias + i + n] > 0;
    }

    public OptionalInt first() {
        if (cnt[1] == 0) OptionalInt.empty();
        int k = 1;
        while (k < n) {
            k <<= 1;
            if (cnt[k] == 0) k |= 1;
        }
        return OptionalInt.of(k - n - bias);
    }

    public OptionalInt last() {
        if (cnt[1] == 0) OptionalInt.empty();
        int k = 1;
        while (k < n) {
            k <<= 1;
            if (cnt[k | 1] > 0) k |= 1;
        }
        return OptionalInt.of(k - n - bias);
    }

    public OptionalInt kthElement(int k) {
        if (cnt[1] <= k) OptionalInt.empty();
        int i = 1;
        int s = 0;
        k++;
        while (i < n) {
            i <<= 1;
            if (s + cnt[i] < k) {
                s += cnt[i];
                i |= 1;
            }
        }
        return OptionalInt.of(i - n - bias);
    }

    public OptionalInt lower(int i) {
        int r = bias + i + n;
        do {
            r--;
            while (r > 1 && (r & 1) == 1) r >>= 1;
            if (cnt[r] > 0) {
                while (r < n) {
                    r = r << 1 | 1;
                    if (cnt[r] == 0) r ^= 1;
                }
                return OptionalInt.of(r - n - bias);
            }
        } while ((r & -r) != r);
        return OptionalInt.empty();
    }

    public OptionalInt floor(int i) {
        return contains(i) ? OptionalInt.of(i) : lower(i);
    }

    public OptionalInt higher(int i) {
        int l = bias + i + 1 + n;
        do {
            l >>= Integer.numberOfTrailingZeros(l);
            if (cnt[l] != 0) {
                while (l < n) {
                    l = l << 1;
                    if (cnt[l] == 0) l |= 1;
                }
                return OptionalInt.of(l - n - bias);
            }
            l++;
        } while ((l & -l) != l);
        return OptionalInt.empty();
    }

    public OptionalInt ceiling(int i) {
        return contains(i) ? OptionalInt.of(i) : higher(i);
    }

    public boolean isEmpty() {
        return cnt[1] == 0;
    }

    public int size() {
        return cnt[1];
    }

    public int count(int l, int r) {
        l += bias; r += bias;
        if (l < 0 || r > n) throw new OutOfRangeException("Out of Range.");
        l += n; r += n;
        int res = 0;
        while (r > l) {
            if ((l & 1) == 1) res += cnt[l++];
            if ((r & 1) == 1) res += cnt[--r];
            l >>= 1; r >>= 1;
        }
        return res;
    }

    void update(int i, final int c) {
        cnt[i += n] = c;
        while ((i >>= 1) > 0) cnt[i] = cnt[i << 1] + cnt[(i << 1) | 1];
    }

    public int count(int i) {return cnt[bias + i + n];}
    public PrimitiveIterator.OfInt iterator() {return new AscendingMultiSetIterator();}
    public PrimitiveIterator.OfInt descendingIterator() {return new DescendingMultiSetIterator();}
    class AscendingMultiSetIterator implements PrimitiveIterator.OfInt {
        int num = 0;
        OptionalInt it = first();
        public boolean hasNext() {return it.isPresent();}
        public int nextInt() {
            int ret = it.getAsInt();
            if (++num == count(ret)) {
                num = 0;
                it = higher(ret);
            }
            return ret;
        }
    }
    class DescendingMultiSetIterator implements PrimitiveIterator.OfInt {
        int num = 0;
        OptionalInt it = last();
        public boolean hasNext() {return it.isPresent();}
        public int nextInt() {
            int ret = it.getAsInt();
            if (++num == count(ret)) {
                num = 0;
                it = lower(ret);
            }
            return ret;
        }
    }
    private static class OutOfRangeException extends RuntimeException {
        private static final long serialVersionUID = 1437290411625321824L;
        private OutOfRangeException() {super();}
        private OutOfRangeException(@SuppressWarnings("SameParameterValue") String s) {super(s);}
    }
}