package lib.persistent;

import java.util.Iterator;
import java.util.Optional;

public class PersistentQueue<T> implements Iterable<T> {
    private static final PersistentStack<?> EMPTY = PersistentStack.empty();

    private final PersistentStack<T> f;
    private final PersistentStack<T> r;
    private PersistentQueue(PersistentStack<T> f, PersistentStack<T> r) {
        if (f.size() < r.size()) {
            this.f = f.concat(r.reverse());
            this.r = emptyStack();
        } else {
            this.f = f;
            this.r = r;
        }
    }
    @SuppressWarnings("unchecked")
    private static <T> PersistentStack<T> emptyStack() {
        return (PersistentStack<T>) EMPTY;
    }
    public static <T> PersistentQueue<T> empty() {
        return new PersistentQueue<>(emptyStack(), emptyStack());
    }
    public PersistentQueue<T> add(T val) {
        return new PersistentQueue<>(f, r.add(val));
    }
    public PersistentQueue<T> remove() {
        return new PersistentQueue<>(f.remove(), r);
    }
    public Optional<PersistentQueue<T>> safeRemove() {
        return isEmpty() ? Optional.empty() : Optional.of(remove());
    }
    public T first() {
        return f.first();
    }
    public Optional<T> safeFirst() {
        return isEmpty() ? Optional.empty() : Optional.of(first());
    }
    public boolean isEmpty() {
        return f.size() == 0;
    }
    public int size() {
        return f.size() + r.size();
    }
    @Override public Iterator<T> iterator() {
        return new Iterator<>() {
            private PersistentQueue<T> cur = PersistentQueue.this;
            @Override public boolean hasNext() {
                return !cur.isEmpty();
            }
            @Override public T next() {
                T val = cur.first();
                cur = cur.remove();
                return val;
            }
        };
    }
    @Override public String toString() {
        return "front : " + f + "\nrear  : " + r;
    }
}
