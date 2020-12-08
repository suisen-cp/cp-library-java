package lib.util.pair;

import java.util.Comparator;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class IntPair {
    public static final Comparator<IntPair> PRI_FIRST_COMPARATOR = Comparator.<IntPair>comparingInt(p -> p.fst).thenComparingInt(p -> p.snd);
    public static final Comparator<IntPair> PRI_FIRST_COMPARATOR_DESC = PRI_FIRST_COMPARATOR.reversed();
    public static final Comparator<IntPair> PRI_SECOND_COMPARATOR = Comparator.<IntPair>comparingInt(p -> p.snd).thenComparingInt(p -> p.fst);
    public static final Comparator<IntPair> PRI_SECOND_COMPARATOR_DESC = PRI_SECOND_COMPARATOR.reversed();
    public int fst, snd;
    public IntPair(final int fst, final int snd) {this.fst = fst; this.snd = snd;}
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof IntPair)) return false;
        final IntPair p = (IntPair) o;
        return this.fst == p.fst && this.snd == p.snd;
    }
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + fst;
        hash = hash * 31 + snd;
        return hash;
    }
    @Override
    public String toString() {return "(" + this.fst + ", " + this.snd + ")";}
}