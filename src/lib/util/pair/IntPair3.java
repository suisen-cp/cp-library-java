package lib.util.pair;

import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntPair3 {
    public int fst, snd, trd;
    public IntPair3(final int fst, final int snd, final int trd) {this.fst = fst; this.snd = snd; this.trd = trd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPair3)) return false;
        final IntPair3 p = (IntPair3) o;
        return this.fst == p.fst && this.snd == p.snd && this.trd == p.trd;
    }
    @Override
    public int hashCode() {return Objects.hash(this.fst, this.snd, this.trd);}
    @Override
    public String toString() {return "(" + this.fst + ", " + this.snd + ", " + this.trd + ")";}
}