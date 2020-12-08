package lib.datastructure;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public class RangeOrderedSet {
    final int[] cnt;
    final int n;
    final int bias;

    public RangeOrderedSet(final int l, final int r) {
        this.bias = -l;
        int w = r - l;
        int nn = 1; while (nn < w) nn <<= 1;
        this.n = nn;
        this.cnt = new int[n << 1];
    }

    public void add(int i) {
        if (contains(i)) return;
        update(bias + i, 1);
    }

    public void remove(int i) {
        if (!contains(i)) return;
        update(bias + i, 0);
    }

    public int pollFirst() {
        int ret = first();
        remove(ret);
        return ret;
    }

    public int pollLast() {
        int ret = last();
        remove(ret);
        return ret;
    }

    public boolean contains(int i) {
        return cnt[bias + i + n] > 0;
    }

    public int first() {
        if (cnt[1] == 0) throw new NoSuchElementException();
        int k = 1;
        while (k < n) {
            k <<= 1;
            if (cnt[k] == 0) k |= 1;
        }
        return k - n - bias;
    }

    public int last() {
        if (cnt[1] == 0) throw new NoSuchElementException();
        int k = 1;
        while (k < n) {
            k <<= 1;
            if (cnt[k | 1] > 0) k |= 1;
        }
        return k - n - bias;
    }

    public int kthElement(int k) {
        if (cnt[1] <= k) throw new NoSuchElementException();
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
        return i - n - bias;
    }

    public int lower(int i) {
        int r = bias + i + n;
        r /= r & -r;
        while (cnt[--r] <= 0) {
            if (r == 0) throw new NoSuchElementException();
            r /= r & -r;
        }
        while (r < n) {
            r <<= 1;
            if (cnt[r | 1] > 0) r |= 1;
        }
        return r - n - bias;
    }

    public int floor(int i) {return contains(i) ? i : lower(i);}

    public int higher(int i) {
        int l = bias + i + 1 + n;
        l /= l & -l;
        while (cnt[l] <= 0) {
            if (++l == n << 1) throw new NoSuchElementException();
            l /= l & -l;
        }
        while (l < n) {
            l <<= 1;
            if (cnt[l] == 0) l |= 1;
        }
        return l - n - bias;
    }

    public int ceiling(int i) {return contains(i) ? i : higher(i);}

    public boolean isEmpty() {return cnt[1] == 0;}

    public int size() {return cnt[1];}

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
        i += n;
        cnt[i] = c;
        while ((i >>= 1) > 0) cnt[i] = cnt[i << 1] + cnt[(i << 1) | 1];
    }

    public PrimitiveIterator.OfInt iterator() {return new AscendingSetIterator();}

    public PrimitiveIterator.OfInt descendingIterator() {return new DescendingSetIterator();}

    class AscendingSetIterator implements PrimitiveIterator.OfInt {
        int it = size() > 0 ? first() : Integer.MAX_VALUE;
        final int max = size() > 0 ? last() : Integer.MIN_VALUE;
        public boolean hasNext() {return it <= max;}
        public int nextInt() {
            int ret = it;
            it = it == max ? Integer.MAX_VALUE : higher(it);
            return ret;
        }
    }
    class DescendingSetIterator implements PrimitiveIterator.OfInt {
        int it = size() > 0 ? last() : Integer.MIN_VALUE;
        final int min = size() > 0 ? first() : Integer.MAX_VALUE;
        public boolean hasNext() {return it >= min;}
        public int nextInt() {
            int ret = it;
            it = it == min ? Integer.MIN_VALUE : lower(it);
            return ret;
        }
    }
    private static class OutOfRangeException extends RuntimeException {
        private static final long serialVersionUID = 1437290411625321824L;
        private OutOfRangeException() {super();}
        private OutOfRangeException(@SuppressWarnings("SameParameterValue") String s) {super(s);}
    }
}