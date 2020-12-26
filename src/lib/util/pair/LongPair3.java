package lib.util.pair;

import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class LongPair3 {
    public long fst, snd, trd;
    public LongPair3(final long fst, final long snd, final long trd) {this.fst = fst; this.snd = snd; this.trd = trd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LongPair3)) return false;
        final LongPair3 p = (LongPair3) o;
        return this.fst == p.fst && this.snd == p.snd && this.trd == p.trd;
    }
    @Override
    public int hashCode() {return Objects.hash(this.fst, this.snd, this.trd);}
    @Override
    public String toString() {return "(" + this.fst + ", " + this.snd + ", " + this.trd + ")";}
}