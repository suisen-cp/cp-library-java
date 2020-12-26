package lib.util.pair;

import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class LongPair {
    public long fst, snd;
    public LongPair(final long fst, final long snd) {this.fst = fst; this.snd = snd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LongPair)) return false;
        final LongPair p = (LongPair) o;
        return this.fst == p.fst && this.snd == p.snd;
    }
    @Override
    public int hashCode() {return Objects.hash(this.fst, this.snd);}
    @Override
    public String toString() {return "(" + this.fst + ", " + this.snd + ")";}
}