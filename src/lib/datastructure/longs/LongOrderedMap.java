package lib.datastructure.longs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.Set;

import lib.util.Random;
import lib.util.pair.IntEntry;
import lib.util.pair.LongEntry;
import lib.util.pair.Pair;

/**
 * @author https://atcoder.jp/users/suisen
 */
public class LongOrderedMap<V> implements Iterable<LongEntry<V>> {
    private static final Random rnd = new Random();
    RBST<V> root;
    public LongOrderedMap() {}
    private LongOrderedMap(RBST<V> root) {this.root = root;}
    public static <V> LongOrderedMap<V> merge(LongOrderedMap<V> l, LongOrderedMap<V> r) {
        return l.mergeRight(r);
    }
    public LongOrderedMap<V> mergeLeft(LongOrderedMap<V> l) {
        return new LongOrderedMap<>(RBST.merge(l.root, root));
    }
    public LongOrderedMap<V> mergeRight(LongOrderedMap<V> r) {
        return new LongOrderedMap<>(RBST.merge(root, r.root));
    }
    public LongOrderedMap<V> splitRightUsingIndex(int k) {
        Pair<RBST<V>, RBST<V>> p = RBST.splitUsingIndex(root, k);
        LongOrderedMap<V> fst = new LongOrderedMap<>(p.fst);
        root = fst.root;
        LongOrderedMap<V> snd = new LongOrderedMap<>(p.snd);
        return snd;
    }
    public LongOrderedMap<V> splitLeftUsingIndex(int k) {
        Pair<RBST<V>, RBST<V>> p = RBST.splitUsingIndex(root, k);
        LongOrderedMap<V> fst = new LongOrderedMap<>(p.fst);
        LongOrderedMap<V> snd = new LongOrderedMap<>(p.snd);
        root = snd.root;
        return fst;
    }
    public LongOrderedMap<V> splitRightUsingKey(long key) {
        Pair<RBST<V>, RBST<V>> p = RBST.splitUsingKey(root, key);
        LongOrderedMap<V> fst = new LongOrderedMap<>(p.fst);
        root = fst.root;
        LongOrderedMap<V> snd = new LongOrderedMap<>(p.snd);
        return snd;
    }
    public LongOrderedMap<V> splitLeftUsingKey(long key) {
        Pair<RBST<V>, RBST<V>> p = RBST.splitUsingKey(root, key);
        LongOrderedMap<V> fst = new LongOrderedMap<>(p.fst);
        LongOrderedMap<V> snd = new LongOrderedMap<>(p.snd);
        root = snd.root;
        return fst;
    }
    public LongEntry<V> kthEntry(int k) {
        if (k < 0 || k >= size()) return null;
        return RBST.kthEntry(root, k);
    }
    public LongEntry<V> firstEntry() {
        return kthEntry(0);
    }
    public LongEntry<V> lastEntry() {
        return kthEntry(size() - 1);
    }
    public LongEntry<V> lowerEntry(long key) {
        return kthEntry(RBST.ltCount(root, key) - 1);
    }
    public LongEntry<V> floorEntry(long key) {
        return kthEntry(RBST.leqCount(root, key) - 1);
    }
    public LongEntry<V> higherEntry(long key) {
        return kthEntry(RBST.leqCount(root, key));
    }
    public LongEntry<V> ceilingEntry(long key) {
        return kthEntry(RBST.ltCount(root, key));
    }
    public V get(long key) {
        return RBST.get(root, key);
    }
    public V getOrDefault(long key, V defaultValue) {
        V res = RBST.get(root, key);
        return res != null ? res : defaultValue;
    }
    public V put(long key, V val) {
        if (RBST.contains(root, key)) {
            LongEntry<V> e = RBST.getEntry(root, key);
            V oldValue = e.getValue();
            e.setValue(val);
            return oldValue;
        }
        root = RBST.insert(root, key, val);
        return null;
    }
    public V putIfAbsent(long key, V val) {
        LongEntry<V> e = RBST.getEntry(root, key);
        if (e != null) return e.getValue();
        put(key, val);
        return null;
    }
    public LongEntry<V> removeKthEntry(int k) {
        if (k < 0 || k >= size()) return null;
        Pair<RBST<V>, LongEntry<V>> nodeAndEntry = RBST.eraseUsingIndex(root, k);
        root = nodeAndEntry.fst;
        return nodeAndEntry.snd;
    }
    public LongEntry<V> remove(long key) {
        if (!containsKey(key)) return null;
        Pair<RBST<V>, LongEntry<V>> nodeAndEntry = RBST.eraseUsingKey(root, key);
        root = nodeAndEntry.fst;
        return nodeAndEntry.snd;
    }
    public boolean remove(long key, V value) {
        LongEntry<V> e = RBST.getEntry(root, key);
        if (e == null) return false;
        if (Objects.equals(value, e.getValue())) {
            Pair<RBST<V>, LongEntry<V>> nodeAndEntry = RBST.eraseUsingKey(root, key);
            root = nodeAndEntry.fst;
            return true;
        }
        return false;
    }
    public V replace(long key, V newValue) {
        LongEntry<V> e = RBST.getEntry(root, key);
        if (e == null) return null;
        V oldValue = e.getValue();
        e.setValue(newValue);
        return oldValue;
    }
    public boolean replace(long key, V oldValue, V newValue) {
        LongEntry<V> e = RBST.getEntry(root, key);
        if (e == null) return false;
        V value = e.getValue();
        if (Objects.equals(value, oldValue)) {
            e.setValue(newValue);
            return true;
        }
        return false;
    }
    public int countLessThan(long key) {
        return RBST.ltCount(root, key);
    }
    public int countLessThanOrEqual(long key) {
        return RBST.leqCount(root, key);
    }
    public int countGreaterThan(long key) {
        return size() - countLessThanOrEqual(key);
    }
    public int countGreaterThanOrEqual(long key) {
        return size() - countLessThan(key);
    }
    public int countRange(long fromKey, long toKey) {
        return countLessThan(toKey) - countLessThan(fromKey);
    }
    public boolean containsKey(long key) {
        return RBST.contains(root, key);
    }
    public int size() {
        return RBST.size(root);
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public void clear() {
        root = null;
    }
    public Set<LongEntry<V>> entrySet() {
        return RBST.entrySet(root);
    }
    public Set<LongEntry<V>> descendingEntrySet() {
        return RBST.descendingEntrySet(root);
    }
    public Iterator<LongEntry<V>> iterator() {
        return RBST.iterator(root);
    }
    public Iterator<LongEntry<V>> descendingIterator() {
        return RBST.descendingIterator(root);
    }
    public PrimitiveIterator.OfLong keyIterator() {
        return RBST.keyIterator(root);
    }
    public PrimitiveIterator.OfLong descendingKeyIterator() {
        return RBST.descendingKeyIterator(root);
    }
    public Set<Long> keySet() {
        return RBST.keySet(root);
    }
    public Set<Long> descendingKeySet() {
        return RBST.descendingKeySet(root);
    }
    public Collection<V> values() {
        return RBST.values(root);
    }
    public Optional<V> safeGet(long key) {
        V res = get(key);
        return res != null ? Optional.of(res) : Optional.empty();
    }
    public Optional<LongEntry<V>> safeGetFirstEntry() {
        return size() > 0 ? Optional.of(kthEntry(0)) : Optional.empty();
    }
    public Optional<LongEntry<V>> safeGetLastEntry() {
        return size() > 0 ? Optional.of(kthEntry(size() - 1)) : Optional.empty();
    }
    public Optional<LongEntry<V>> safeGetLowerEntry(long key) {
        int k = RBST.ltCount(root, key) - 1;
        return k >= 0 ? Optional.of(kthEntry(k)) : Optional.empty();
    }
    public Optional<LongEntry<V>> safeGetFloorEntry(long key) {
        int k = RBST.leqCount(root, key) - 1;
        return k >= 0 ? Optional.of(kthEntry(k)) : Optional.empty();
    }
    public Optional<LongEntry<V>> safeGetHigherEntry(long key) {
        int k = RBST.leqCount(root, key);
        return k < size() ? Optional.of(kthEntry(k)) : Optional.empty();
    }
    public Optional<LongEntry<V>> safeGetCeilingEntry(long key) {
        int k = RBST.ltCount(root, key);
        return k < size() ? Optional.of(kthEntry(k)) : Optional.empty();
    }
    static final class RBST<V> extends LongEntry<V> {
		private RBST<V> l, r;
        private int size;
        private RBST(long key, V val) {super(key, val); this.size = 1;}
        private RBST<V> update() {
            size = size(l) + size(r) + 1;
            return this;
        }
        static <V> LongEntry<V> getEntry(RBST<V> t, long key) {
            while (t != null) {
                if (t.key == key) return t;
                t = t.key < key ? t.r : t.l;
            }
            return null;
        }
        static <V> V get(RBST<V> t, long key) {
            while (t != null) {
                if (t.key == key) return t.getValue();
                t = t.key < key ? t.r : t.l;
            }
            return null;
        }
        static <V> LongEntry<V> kthEntry(RBST<V> t, int k) {
            int c = size(t.l);
            if (k < c) return kthEntry(t.l, k);
            if (k == c) return t;
            return kthEntry(t.r, k - c - 1);
        }
        static <V> int leqCount(RBST<V> t, long key) {
            if (t == null) return 0;
            if (key < t.key) return leqCount(t.l, key);
            return leqCount(t.r, key) + size(t.l) + 1;
        }
        static <V> int ltCount(RBST<V> t, long key) {
            if (t == null) return 0;
            if (key <= t.key) return ltCount(t.l, key);
            return ltCount(t.r, key) + size(t.l) + 1;
        }
        static <V> RBST<V> merge(RBST<V> l, RBST<V> r) {
            if (l == null) return r;
            if (r == null) return l;
            if (rnd.nextInt() % (l.size + r.size) < l.size) {
                l.r = merge(l.r, r);
                return l.update();
            } else {
                r.l = merge(l, r.l);
                return r.update();
            }
        }
        static <V> Pair<RBST<V>, RBST<V>> splitUsingIndex(RBST<V> x, int k) {
            if (k < 0 || k > size(x)) {
                throw new IndexOutOfBoundsException(
                    String.format("index %d is out of bounds for the length of %d", k, size(x))
                );
            }
            if (x == null) {
                //noinspection Convert2Diamond
                return new Pair<RBST<V>, RBST<V>>(null, null);
            } else if (k <= size(x.l)) {
                Pair<RBST<V>, RBST<V>> p = splitUsingIndex(x.l, k);
                x.l = p.snd;
                p.snd = x.update();
                return p;
            } else {
                Pair<RBST<V>, RBST<V>> p = splitUsingIndex(x.r, k - size(x.l) - 1);
                x.r = p.fst;
                p.fst = x.update();
                return p;
            }
        }
        static <V> Pair<RBST<V>, RBST<V>> splitUsingKey(RBST<V> x, long key) {
            if (x == null) {
                //noinspection Convert2Diamond
                return new Pair<RBST<V>, RBST<V>>(null, null);
            } else if (key <= x.key) {
                Pair<RBST<V>, RBST<V>> p = splitUsingKey(x.l, key);
                x.l = p.snd;
                p.snd = x.update();
                return p;
            } else {
                Pair<RBST<V>, RBST<V>> p = splitUsingKey(x.r, key);
                x.r = p.fst;
                p.fst = x.update();
                return p;
            }
        }
        static <V> RBST<V> insert(RBST<V> t, long key, V val) {
            Pair<RBST<V>, RBST<V>> p = splitUsingKey(t, key);
            return RBST.merge(RBST.merge(p.fst, new RBST<>(key, val)), p.snd);
        }
        static <V> Pair<RBST<V>, LongEntry<V>> eraseUsingIndex(RBST<V> t, int k) {
            Pair<RBST<V>, RBST<V>> p = splitUsingIndex(t, k);
            Pair<RBST<V>, RBST<V>> q = splitUsingIndex(p.snd, 1);
            return new Pair<>(RBST.merge(p.fst, q.snd), q.fst);
        }
        static <V> Pair<RBST<V>, LongEntry<V>> eraseUsingKey(RBST<V> t, long key) {
            Pair<RBST<V>, RBST<V>> p = splitUsingKey(t, key);
            Pair<RBST<V>, RBST<V>> q = splitUsingIndex(p.snd, 1);
            return new Pair<>(RBST.merge(p.fst, q.snd), q.fst);
        }
        static <V> boolean contains(RBST<V> t, long key) {
            while (t != null) {
                if (t.key == key) return true;
                else if (t.key < key) t = t.r;
                else t = t.l;
            }
            return false;
        }
        static <V> int size(RBST<V> nd) {
            return nd == null ? 0 : nd.size;
        }
        static <V> Set<LongEntry<V>> entrySet(RBST<V> t) {
            LinkedHashSet<LongEntry<V>> set = new LinkedHashSet<>();
            if (t == null) return set;
            ArrayDeque<IntEntry<RBST<V>>> stack = new ArrayDeque<>();
            if (t.r != null) stack.addLast(new IntEntry<>(0, t.r));
            stack.addLast(new IntEntry<>(1, t));
            if (t.l != null) stack.addLast(new IntEntry<>(0, t.l));
            while (stack.size() > 0) {
                IntEntry<RBST<V>> p = stack.pollLast();
                RBST<V> u = p.getValue();
                if (p.key == 1) {
                    set.add(u);
                } else {
                    if (u.r != null) stack.addLast(new IntEntry<>(0, u.r));
                    stack.addLast(new IntEntry<>(1, u));
                    if (u.l != null) stack.addLast(new IntEntry<>(0, u.l));
                }
            }
            return set;
        }
        static <V> Set<LongEntry<V>> descendingEntrySet(RBST<V> t) {
            LinkedHashSet<LongEntry<V>> set = new LinkedHashSet<>();
            if (t == null) return set;
            ArrayDeque<IntEntry<RBST<V>>> stack = new ArrayDeque<>();
            if (t.l != null) stack.addLast(new IntEntry<>(0, t.l));
            stack.addLast(new IntEntry<>(1, t));
            if (t.r != null) stack.addLast(new IntEntry<>(0, t.r));
            while (stack.size() > 0) {
                IntEntry<RBST<V>> p = stack.pollLast();
                RBST<V> u = p.getValue();
                if (p.key == 1) {
                    set.add(u);
                } else {
                    if (u.l != null) stack.addLast(new IntEntry<>(0, u.l));
                    stack.addLast(new IntEntry<>(1, u));
                    if (u.r != null) stack.addLast(new IntEntry<>(0, u.r));
                }
            }
            return set;
        }
        static <V> Set<Long> keySet(RBST<V> t) {
            Set<Long> set = new LinkedHashSet<>();
            for (LongEntry<V> e : entrySet(t)) set.add(e.key);
            return set;
        }
        static <V> Set<Long> descendingKeySet(RBST<V> t) {
            Set<Long> set = new LinkedHashSet<>();
            for (LongEntry<V> e : descendingEntrySet(t)) set.add(e.key);
            return set;
        }
        static <V> Collection<V> values(RBST<V> t) {
            Collection<V> col = new ArrayList<>();
            for (LongEntry<V> e : entrySet(t)) col.add(e.getValue());
            return col;
        }
        static <V> Iterator<LongEntry<V>> iterator(RBST<V> t) {
            return entrySet(t).iterator();
        }
        static <V> Iterator<LongEntry<V>> descendingIterator(RBST<V> t) {
            return descendingEntrySet(t).iterator();
        }
        static <V> PrimitiveIterator.OfLong keyIterator(RBST<V> t) {
            return new PrimitiveIterator.OfLong(){
                final Iterator<LongEntry<V>> it = iterator(t);
                public boolean hasNext() {return it.hasNext();}
                public long nextLong() {return it.next().key;}
            };
        }
        static <V> PrimitiveIterator.OfLong descendingKeyIterator(RBST<V> t) {
            return new PrimitiveIterator.OfLong(){
                final Iterator<LongEntry<V>> it = descendingIterator(t);
                public boolean hasNext() {return it.hasNext();}
                public long nextLong() {return it.next().key;}
            };
        }
        public String toString() {
            return "(" + getKey() + " => " + getValue() + ")";
        }
    }
}