package lib.util.pair;

import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Pair<E0, E1> {
    public E0 fst;
    public E1 snd;
    public Pair(final E0 fst, final E1 snd) {this.fst = fst; this.snd = snd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o)  return true;
        if (!(o instanceof Pair)) return false;
        final Pair p = (Pair) o;
        return this.fst.equals(p.fst) && this.snd.equals(p.snd);
    }
    @Override
    public int hashCode() {return Objects.hash(this.fst, this.snd);}
    @Override
    public String toString() {return "(" + this.fst.toString() + ", " + this.snd.toString() + ")";}
}