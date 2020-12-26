package lib.util.pair;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author https://atcoder.jp/users/suisen
 */
public final class DoublePair {
    public static final Comparator<DoublePair> PRI_FIRST_COMPARATOR = Comparator.<DoublePair>comparingDouble(p -> p.fst).thenComparingDouble(p -> p.snd);
    public static final Comparator<DoublePair> PRI_FIRST_COMPARATOR_DESC = PRI_FIRST_COMPARATOR.reversed();
    public static final Comparator<DoublePair> PRI_SECOND_COMPARATOR = Comparator.<DoublePair>comparingDouble(p -> p.snd).thenComparingDouble(p -> p.fst);
    public static final Comparator<DoublePair> PRI_SECOND_COMPARATOR_DESC = PRI_SECOND_COMPARATOR.reversed();
    public double fst, snd;
    public DoublePair(final double fst, final double snd) {this.fst = fst; this.snd = snd;}
    @Override @SuppressWarnings("all")
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof DoublePair)) return false;
        final DoublePair p = (DoublePair) o;
        return this.fst == p.fst && this.snd == p.snd;
    }
    @Override
    public int hashCode() {return Objects.hash(fst, snd);}
    @Override
    public String toString() {return "(" + fst + ", " + snd + ")";}
}