package lib.util.itertools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PrimitiveIterator;

public class IterUtil {
    private static final Iterator<?> EMPTY_ITERATOR = new Iterator<>(){
        public boolean hasNext() {return false;}
        public Object next() {throw new UnsupportedOperationException();}
    };
    private static final PrimitiveIterator.OfInt EMPTY_INT_ITERATOR = new PrimitiveIterator.OfInt(){
        public boolean hasNext() {return false;}
        public int nextInt() {throw new UnsupportedOperationException();}
    };
    private static final PrimitiveIterator.OfLong EMPTY_LONG_ITERATOR = new PrimitiveIterator.OfLong(){
        public boolean hasNext() {return false;}
        public long nextLong() {throw new UnsupportedOperationException();}
    };
    private static final PrimitiveIterator.OfDouble EMPTY_DOUBLE_ITERATOR = new PrimitiveIterator.OfDouble(){
        public boolean hasNext() {return false;}
        public double nextDouble() {throw new UnsupportedOperationException();}
    };
    @SuppressWarnings("unchecked")
    private static final Iterable<?> EMPTY_ITERABLE = () -> (Iterator<Object>) EMPTY_ITERATOR;

    public static PrimitiveIterator.OfInt    emptyIntIterator()    {return EMPTY_INT_ITERATOR;}
    public static PrimitiveIterator.OfLong   emptyLongIterator()   {return EMPTY_LONG_ITERATOR;}
    public static PrimitiveIterator.OfDouble emptyDoubleIterator() {return EMPTY_DOUBLE_ITERATOR;}
    @SuppressWarnings("unchecked")
    public static <V> Iterator<V> emptyIterator() {return (Iterator<V>) EMPTY_ITERATOR;}
    @SuppressWarnings("unchecked")
    public static <V> Iterable<V> emptyIterable() {return (Iterable<V>) EMPTY_ITERABLE;}

    private static <V> Iterator<V> concatIterators(Iterator<? extends Iterator<V>> iterIterator) {
        return new Iterator<V>(){
            Iterator<V> it = emptyIterator();
            public boolean hasNext() {
                while (!it.hasNext()) {
                    if (!iterIterator.hasNext()) return false;
                    it = iterIterator.next();
                }
                return true;
            }
            public V next() {return it.next();}
        };
    }
    private static PrimitiveIterator.OfInt concatIntIterators(Iterator<PrimitiveIterator.OfInt> iterIterator) {
        return new PrimitiveIterator.OfInt(){
            PrimitiveIterator.OfInt it = emptyIntIterator();
            public boolean hasNext() {
                while (!it.hasNext()) {
                    if (!iterIterator.hasNext()) return false;
                    it = iterIterator.next();
                }
                return true;
            }
            public int nextInt() {return it.nextInt();}
        };
    }
    private static PrimitiveIterator.OfLong concatLongIterators(Iterator<PrimitiveIterator.OfLong> iterIterator) {
        return new PrimitiveIterator.OfLong(){
            PrimitiveIterator.OfLong it = emptyLongIterator();
            public boolean hasNext() {
                while (!it.hasNext()) {
                    if (!iterIterator.hasNext()) return false;
                    it = iterIterator.next();
                }
                return true;
            }
            public long nextLong() {return it.nextLong();}
        };
    }
    private static PrimitiveIterator.OfDouble concatDoubleIterators(Iterator<PrimitiveIterator.OfDouble> iterIterator) {
        return new PrimitiveIterator.OfDouble(){
            PrimitiveIterator.OfDouble it = emptyDoubleIterator();
            public boolean hasNext() {
                while (!it.hasNext()) {
                    if (!iterIterator.hasNext()) return false;
                    it = iterIterator.next();
                }
                return true;
            }
            public double nextDouble() {return it.nextDouble();}
        };
    }

    private static <V> Iterable<V> concatIterables(Iterator<? extends Iterable<V>> iterIterator) {
        return () -> new Iterator<V>(){
            Iterator<V> it = emptyIterator();
            public boolean hasNext() {
                while (!it.hasNext()) {
                    if (!iterIterator.hasNext()) return false;
                    it = iterIterator.next().iterator();
                }
                return true;
            }
            public V next() {return it.next();}
        };
    }

    public static <V> Iterator<V> concatIterators(Iterator<V> it1, Iterator<V> it2) {
        return concatIterators(List.of(it1, it2).iterator());
    }
    public static <V> Iterator<V> concatIterators(Iterator<V> it1, Iterator<V> it2, Iterator<V> it3) {
        return concatIterators(List.of(it1, it2, it3).iterator());
    }
    public static <V> Iterator<V> concatIterators(Iterator<V> it1, Iterator<V> it2, Iterator<V> it3, Iterator<V> it4) {
        return concatIterators(List.of(it1, it2, it3, it4).iterator());
    }
    public static <V> Iterator<V> concatIterators(Iterable<? extends Iterator<V>> its) {
        return concatIterators(its.iterator());
    }

    public static PrimitiveIterator.OfInt concatIntIterators(PrimitiveIterator.OfInt it1, PrimitiveIterator.OfInt it2) {
        return concatIntIterators(List.of(it1, it2).iterator());
    }
    public static PrimitiveIterator.OfInt concatIntIterators(PrimitiveIterator.OfInt it1, PrimitiveIterator.OfInt it2, PrimitiveIterator.OfInt it3) {
        return concatIntIterators(List.of(it1, it2, it3).iterator());
    }
    public static PrimitiveIterator.OfInt concatIntIterators(PrimitiveIterator.OfInt it1, PrimitiveIterator.OfInt it2, PrimitiveIterator.OfInt it3, PrimitiveIterator.OfInt it4) {
        return concatIntIterators(List.of(it1, it2, it3, it4).iterator());
    }
    public static PrimitiveIterator.OfInt concatIntIterators(Iterable<PrimitiveIterator.OfInt> its) {
        return concatIntIterators(its.iterator());
    }

    public static PrimitiveIterator.OfLong concatLongIterators(PrimitiveIterator.OfLong it1, PrimitiveIterator.OfLong it2) {
        return concatLongIterators(List.of(it1, it2).iterator());
    }
    public static PrimitiveIterator.OfLong concatLongIterators(PrimitiveIterator.OfLong it1, PrimitiveIterator.OfLong it2, PrimitiveIterator.OfLong it3) {
        return concatLongIterators(List.of(it1, it2, it3).iterator());
    }
    public static PrimitiveIterator.OfLong concatLongIterators(PrimitiveIterator.OfLong it1, PrimitiveIterator.OfLong it2, PrimitiveIterator.OfLong it3, PrimitiveIterator.OfLong it4) {
        return concatLongIterators(List.of(it1, it2, it3, it4).iterator());
    }
    public static PrimitiveIterator.OfLong concatLongIterators(Iterable<PrimitiveIterator.OfLong> its) {
        return concatLongIterators(its.iterator());
    }

    public static PrimitiveIterator.OfDouble concatDoubleIterators(PrimitiveIterator.OfDouble it1, PrimitiveIterator.OfDouble it2) {
        return concatDoubleIterators(List.of(it1, it2).iterator());
    }
    public static PrimitiveIterator.OfDouble concatDoubleIterators(PrimitiveIterator.OfDouble it1, PrimitiveIterator.OfDouble it2, PrimitiveIterator.OfDouble it3) {
        return concatDoubleIterators(List.of(it1, it2, it3).iterator());
    }
    public static PrimitiveIterator.OfDouble concatDoubleIterators(PrimitiveIterator.OfDouble it1, PrimitiveIterator.OfDouble it2, PrimitiveIterator.OfDouble it3, PrimitiveIterator.OfDouble it4) {
        return concatDoubleIterators(List.of(it1, it2, it3, it4).iterator());
    }
    public static PrimitiveIterator.OfDouble concatDoubleIterators(Iterable<PrimitiveIterator.OfDouble> its) {
        return concatDoubleIterators(its.iterator());
    }

    public static <V> Iterable<V> concatIterables(Iterable<V> it1, Iterable<V> it2) {
        return concatIterables(List.of(it1, it2));
    }
    public static <V> Iterable<V> concatIterables(Iterable<V> it1, Iterable<V> it2, Iterable<V> it3) {
        return concatIterables(List.of(it1, it2, it3));
    }
    public static <V> Iterable<V> concatIterables(Iterable<V> it1, Iterable<V> it2, Iterable<V> it3, Iterable<V> it4) {
        return concatIterables(List.of(it1, it2, it3, it4));
    }
    public static <V> Iterable<V> concatIterables(Iterable<? extends Iterable<V>> its) {
        return concatIterables(its.iterator());
    }

    public static PrimitiveIterator.OfInt ofIntIterator(int e) {
        return new PrimitiveIterator.OfInt(){
            boolean seen;
            public boolean hasNext() {return !seen;}
            public int nextInt() {seen = true; return e;}
        };
    }
    public static PrimitiveIterator.OfLong ofLongIterator(long e) {
        return new PrimitiveIterator.OfLong(){
            boolean seen;
            public boolean hasNext() {return !seen;}
            public long nextLong() {seen = true; return e;}
        };
    }
    public static PrimitiveIterator.OfDouble ofIntIterator(double e) {
        return new PrimitiveIterator.OfDouble(){
            boolean seen;
            public boolean hasNext() {return !seen;}
            public double nextDouble() {seen = true; return e;}
        };
    }
}

class IterConcatenatorTest {
    public static void main(String[] args) {
        verify();
        performanceTest();
    }

    public static void verify() {

    }

    public static void performanceTest() {
        final int N = 10000000;
        ArrayList<ArrayList<Integer>> ls = new ArrayList<>();
        for (int i = 0; i < N / 4; i++) {
            ArrayList<Integer> l = new ArrayList<>();
            l.add(i);
            ls.add(l);
        }
        ArrayList<Integer> lm = new ArrayList<>();
        for (int i = N / 4; i < N / 2; i++) {
            lm.add(i);
        }
        ArrayList<Integer> lr = new ArrayList<>();
        for (int i = N / 2; i < N; i++) {
            lm.add(i);
        }
        long s = 0;
        long beg = System.nanoTime();
        for (Integer e : IterUtil.concatIterables(IterUtil.concatIterables(ls), lm, lr)) s += e;
        long end = System.nanoTime();
        System.out.println("time : " + (end - beg) + " ns");
        // => time : 1110529100 ns
        System.out.println(s);
    }
}