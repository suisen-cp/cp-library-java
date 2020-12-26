package lib.util.pair;

import java.util.Comparator;
import java.util.Objects;

public class DoubleIntPair {
    public static final Comparator<DoubleIntPair> PRI_FIRST_COMPARATOR = (p1, p2) -> {
        int cmp1 = Double.compare(p1.fst, p2.fst);
        return cmp1 == 0 ? Integer.compare(p1.snd, p2.snd) : cmp1;
    };
    public static final Comparator<DoubleIntPair> PRI_FIRST_COMPARATOR_DESC = PRI_FIRST_COMPARATOR.reversed();
    public static final Comparator<DoubleIntPair> PRI_SECOND_COMPARATOR = (p1, p2) -> {
        int cmp1 = Integer.compare(p1.snd, p2.snd);
        return cmp1 == 0 ? Double.compare(p1.fst, p2.fst) : cmp1;
    };
    public static final Comparator<DoubleIntPair> PRI_SECOND_COMPARATOR_DESC = PRI_SECOND_COMPARATOR.reversed();
    public double fst;
    public int snd;
    public DoubleIntPair(final double fst, final int snd) {this.fst = fst; this.snd = snd;}
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
