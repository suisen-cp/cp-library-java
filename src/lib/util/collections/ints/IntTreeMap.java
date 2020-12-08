package lib.util.collections.ints;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import lib.util.function.IntObjConsumer;
import lib.util.pair.IntEntry;

public class IntTreeMap<V> implements Iterable<IntTreeMap.Entry<V>> {
    private Entry<V> root;
    private int size = 0;
    private int modCount = 0;
    public int size() {return size;}
    public boolean containsKey(int key) {return getEntry(key) != null;}
    public V get(int key) {
        Entry<V> p = getEntry(key);
        return (p == null ? null : p.val);
    }
    final Entry<V> getEntry(int key) {
        Entry<V> p = root;
        while (p != null) {
            if (key < p.key) p = p.left;
            else if (key > p.key) p = p.right;
            else return p;
        }
        return null;
    }
    final Entry<V> getCeilingEntry(int key) {
        Entry<V> p = root;
        while (p != null) {
            if (key < p.key) {
                if (p.left != null) p = p.left;
                else return p;
            } else if (key > p.key) {
                if (p.right != null) {
                    p = p.right;
                } else {
                    Entry<V> parent = p.parent;
                    Entry<V> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else return p;
        }
        return null;
    }
    final Entry<V> getFloorEntry(int key) {
        Entry<V> p = root;
        while (p != null) {
            if (key > p.key) {
                if (p.right != null) p = p.right;
                else return p;
            } else if (key < p.key) {
                if (p.left != null) {
                    p = p.left;
                } else {
                    Entry<V> parent = p.parent;
                    Entry<V> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            } else return p;
        }
        return null;
    }
    final Entry<V> getHigherEntry(int key) {
        Entry<V> p = root;
        while (p != null) {
            if (key < p.key) {
                if (p.left != null) p = p.left;
                else return p;
            } else {
                if (p.right != null) {
                    p = p.right;
                } else {
                    Entry<V> parent = p.parent;
                    Entry<V> ch = p;
                    while (parent != null && ch == parent.right) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }
    final Entry<V> getLowerEntry(int key) {
        Entry<V> p = root;
        while (p != null) {
            if (key > p.key) {
                if (p.right != null) p = p.right;
                else return p;
            } else {
                if (p.left != null) {
                    p = p.left;
                } else {
                    Entry<V> parent = p.parent;
                    Entry<V> ch = p;
                    while (parent != null && ch == parent.left) {
                        ch = parent;
                        parent = parent.parent;
                    }
                    return parent;
                }
            }
        }
        return null;
    }
    public V put(int key, V value) {
        Entry<V> t = root;
        if (t == null) {
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        Entry<V> parent;
        do {
            parent = t;
            cmp = Integer.compare(key, t.key);
            if (cmp < 0) t = t.left;
            else if (cmp > 0) t = t.right;
            else return t.setValue(value);
        } while (t != null);
        Entry<V> e = new Entry<>(key, value, parent);
        if (cmp < 0) parent.left = e;
        else parent.right = e;
        fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }
    public V remove(int key) {
        Entry<V> p = getEntry(key);
        if (p == null) return null;
        V oldValue = p.val;
        deleteEntry(p);
        return oldValue;
    }
    public void clear() {
        modCount++;
        size = 0;
        root = null;
    }
    public IntEntry<V> firstEntry() {return getFirstEntry();}
    public IntEntry<V> lastEntry()  {return getLastEntry();}
    public IntEntry<V> pollFirstEntry() {
        Entry<V> p = getFirstEntry();
        Entry<V> result = p;
        if (p != null) deleteEntry(p);
        return result;
    }
    public IntEntry<V> pollLastEntry() {
        Entry<V> p = getLastEntry();
        Entry<V> result = p;
        if (p != null) deleteEntry(p);
        return result;
    }
    public IntEntry<V> lowerEntry(int key) {return getLowerEntry(key);}
    public IntEntry<V> floorEntry(int key) {return getFloorEntry(key);}
    public IntEntry<V> ceilingEntry(int key) {return getCeilingEntry(key);}
    public IntEntry<V> higherEntry(int key) {return getHigherEntry(key);}
    public boolean replace(int key, V oldValue, V newValue) {
        Entry<V> p = getEntry(key);
        if (p != null && Objects.equals(oldValue, p.val)) {
            p.val = newValue;
            return true;
        }
        return false;
    }
    public V replace(int key, V value) {
        Entry<V> p = getEntry(key);
        if (p!=null) {
            V oldValue = p.val;
            p.val = value;
            return oldValue;
        }
        return null;
    }
    public void forEach(IntObjConsumer<V> action) {
        Objects.requireNonNull(action);
        int expectedModCount = modCount;
        for (Entry<V> e = getFirstEntry(); e != null; e = successor(e)) {
            action.accept(e.key, e.val);
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
    public Iterator<Entry<V>> iterator() {
        return new EntryIterator(getFirstEntry());
    }
    public Iterator<Entry<V>> descendingIterator() {
        return new DescendingEntryIterator(getLastEntry());
    }
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<V> next;
        Entry<V> lastReturned;
        int expectedModCount;
        PrivateEntryIterator(Entry<V> first) {
            expectedModCount = modCount;
            lastReturned = null;
            next = first;
        }
        public final boolean hasNext() {return next != null;}
        final Entry<V> nextEntry() {
            Entry<V> e = next;
            if (e == null) throw new NoSuchElementException();
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
            next = successor(e);
            lastReturned = e;
            return e;
        }
        final Entry<V> prevEntry() {
            Entry<V> e = next;
            if (e == null) throw new NoSuchElementException();
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
            next = predecessor(e);
            lastReturned = e;
            return e;
        }
        public void remove() {
            if (lastReturned == null) throw new IllegalStateException();
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
            if (lastReturned.left != null && lastReturned.right != null) next = lastReturned;
            deleteEntry(lastReturned);
            expectedModCount = modCount;
            lastReturned = null;
        }
    }
    final class EntryIterator extends PrivateEntryIterator<Entry<V>> {
        EntryIterator(Entry<V> first) {super(first);}
        public Entry<V> next() {return nextEntry();}
    }
    final class DescendingEntryIterator extends PrivateEntryIterator<Entry<V>> {
        DescendingEntryIterator(Entry<V> first) {super(first);}
        public Entry<V> next() {return prevEntry();}
    }

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    static class Entry<V> extends IntEntry<V> {
        Entry<V> left;
        Entry<V> right;
        Entry<V> parent;
        boolean color = BLACK;
        Entry(int key, V val, Entry<V> parent) {
            super(key, val);
            this.parent = parent;
        }
    }
    final Entry<V> getFirstEntry() {
        Entry<V> p = root;
        if (p != null) while (p.left != null) p = p.left;
        return p;
    }
    final Entry<V> getLastEntry() {
        Entry<V> p = root;
        if (p != null) while (p.right != null) p = p.right;
        return p;
    }
    static <V> Entry<V> successor(Entry<V> t) {
        if (t == null) return null;
        else if (t.right != null) {
            Entry<V> p = t.right;
            while (p.left != null) p = p.left;
            return p;
        } else {
            Entry<V> p = t.parent;
            Entry<V> ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
    static <V> Entry<V> predecessor(Entry<V> t) {
        if (t == null) return null;
        else if (t.left != null) {
            Entry<V> p = t.left;
            while (p.right != null) p = p.right;
            return p;
        } else {
            Entry<V> p = t.parent;
            Entry<V> ch = t;
            while (p != null && ch == p.left) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }
    private static <V> boolean colorOf(Entry<V> p) {return p == null ? BLACK : p.color;}
    private static <V> Entry<V> parentOf(Entry<V> p) {return p == null ? null: p.parent;}
    private static <K,V> void setColor(Entry<V> p, boolean c) {if (p != null) p.color = c;}
    private static <V> Entry<V> leftOf (Entry<V> p) {return (p == null) ? null: p.left;}
    private static <V> Entry<V> rightOf(Entry<V> p) {return (p == null) ? null: p.right;}
    private void rotateLeft(Entry<V> p) {
        if (p != null) {
            Entry<V> r = p.right;
            p.right = r.left;
            if (r.left != null) r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null) root = r;
            else if (p.parent.left == p) p.parent.left = r;
            else p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }
    private void rotateRight(Entry<V> p) {
        if (p != null) {
            Entry<V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null) root = l;
            else if (p.parent.right == p) p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }
    private void fixAfterInsertion(Entry<V> x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Entry<V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Entry<V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }
    private void deleteEntry(Entry<V> p) {
        modCount++;
        size--;
        if (p.left != null && p.right != null) {
            Entry<V> s = successor(p);
            p.key = s.key;
            p.val = s.val;
            p = s;
        }
        Entry<V> replacement = (p.left != null ? p.left : p.right);
        if (replacement != null) {
            replacement.parent = p.parent;
            if (p.parent == null) root = replacement;
            else if (p == p.parent.left) p.parent.left  = replacement;
            else p.parent.right = replacement;
            p.left = p.right = p.parent = null;
            if (p.color == BLACK) fixAfterDeletion(replacement);
        } else if (p.parent == null) {
            root = null;
        } else {
            if (p.color == BLACK) fixAfterDeletion(p);
            if (p.parent != null) {
                if (p == p.parent.left) p.parent.left = null;
                else if (p == p.parent.right) p.parent.right = null;
                p.parent = null;
            }
        }
    }
    private void fixAfterDeletion(Entry<V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Entry<V> sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateLeft(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib))  == BLACK &&
                    colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    rotateLeft(parentOf(x));
                    x = root;
                }
            } else {
                Entry<V> sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rotateRight(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK &&
                    colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rotateRight(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }
}
