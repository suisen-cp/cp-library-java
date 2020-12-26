package lib.util.pair;

import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntLongPair {
    public int fst;
    public long snd;
    public IntLongPair(final int fst, final long snd) {this.fst = fst; this.snd = snd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof IntLongPair)) return false;
        final IntLongPair p = (IntLongPair) o;
        return this.fst == p.fst && this.snd == p.snd;
    }
    @Override
    public int hashCode() {return Objects.hash(this.fst, this.snd);}
    @Override
    public String toString() {return "(" + this.fst + ", " + this.snd + ")";}
}