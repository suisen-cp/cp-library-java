package lib.util.pair;

import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class Pair3<E0, E1, E2> {
    public E0 fst;
    public E1 snd;
    public E2 trd;
    public Pair3(final E0 fst, final E1 snd, final E2 trd) {this.fst = fst; this.snd = snd; this.trd = trd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair3)) return false;
        final Pair3 p = (Pair3) o;
        return this.fst.equals(p.fst) && this.snd.equals(p.snd) && this.trd.equals(p.trd);
    }
    @Override
    public int hashCode() {return Objects.hash(this.fst, this.snd, this.trd);}
    @Override
    public String toString() {return "(" + this.fst.toString() + ", " + this.snd.toString() + ", " + this.trd.toString() + ")";}
}