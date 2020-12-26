package lib.persistent;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PersistentStack<T> implements Iterable<T> {
    private PersistentStack(){}
    public PersistentStack<T> add(T val) {
        throw new UnsupportedOperationException();
    }
    public PersistentStack<T> remove() {
        throw new UnsupportedOperationException();
    }
    public Optional<PersistentStack<T>> safeRemove() {
        return isEmpty() ? Optional.empty() : Optional.of(remove());
    }
    public T first() {
        throw new UnsupportedOperationException();
    }
    public Optional<T> safeFirst() {
        return isEmpty() ? Optional.empty() : Optional.of(first());
    }
    public int size() {
        throw new UnsupportedOperationException();
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    PersistentStack<T> eval() {
        throw new UnsupportedOperationException();
    }
    PersistentStack<T> reverse() {
        throw new UnsupportedOperationException();
    }
    PersistentStack<T> concat(PersistentStack<T> y) {
        throw new UnsupportedOperationException();
    }
    public static <T> PersistentStack<T> empty() {
        return new PersistentStack<>() {
            @Override public PersistentStack<T> add(T val) {
                return new NonemptyStack<>(val, this);
            }
            @Override public PersistentStack<T> remove() {
                throw new NoSuchElementException("Stack is Empty.");
            }
            @Override public T first() {
                throw new NoSuchElementException("Stack is Empty.");
            }
            @Override public int size() {
                return 0;
            }
            @Override PersistentStack<T> eval() {
                return this;
            }
            @Override PersistentStack<T> reverse() {
                return this;
            }
            @Override PersistentStack<T> concat(PersistentStack<T> y) {
                return y.eval();
            }
        };
    }
    @Override public Iterator<T> iterator() {
        return new Iterator<>() {
            private PersistentStack<T> cur = PersistentStack.this.eval();
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
        StringBuilder sb = new StringBuilder().append('<');
        forEach(val -> sb.append(val).append(','));
        return sb.append(']').toString();
    }
    private static final class NonemptyStack<T> extends PersistentStack<T> {
        private PersistentStack<T> prev;
        private final T val;
        private final int size;
        NonemptyStack(T val, PersistentStack<T> prev) {
            this.prev = prev;
            this.val = val;
            this.size = prev.size() + 1;
        }
        @Override public PersistentStack<T> add(T val) {
            return new NonemptyStack<>(val, this);
        }
        @Override public PersistentStack<T> remove() {
            return prev = prev.eval();
        }
        @Override public T first() {
            return val;
        }
        @Override public int size() {
            return size;
        }
        @Override PersistentStack<T> eval() {
            return this;
        }
        @Override PersistentStack<T> reverse() {
            return new PersistentStack<>(){
                @Override public int size() {
                    return NonemptyStack.this.size();
                }
                @Override public PersistentStack<T> eval() {
                    PersistentStack<T> rev = empty();
                    for (final T t : NonemptyStack.this) {
                        rev = rev.add(t);
                    }
                    return rev;
                }
            };
        }
        @Override PersistentStack<T> concat(PersistentStack<T> y) {
            final int size = size() + y.size();
            final PersistentStack<T> t = remove();
            return new NonemptyStack<>(val, new PersistentStack<>(){
                @Override public int size() {
                    return size - 1;
                }
                @Override public PersistentStack<T> eval() {
                    return t.concat(y);
                }
            });
        }
    }
}
