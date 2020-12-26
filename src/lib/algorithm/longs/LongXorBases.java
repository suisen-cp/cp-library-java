package lib.algorithm.longs;

import java.util.PrimitiveIterator;
import lib.util.collections.longs.LongArrayList;

public final class LongXorBases {
    private final LongArrayList v = new LongArrayList();

    public LongXorBases() {}
    public LongXorBases(int[] a) {for (int e : a) appendBase(e);}
    private long solve(long e) {
        PrimitiveIterator.OfLong iter = v.iterator();
        while (iter.hasNext()) e = Math.min(e, e ^ iter.nextLong());
        return e;
    }
    public boolean isExpressible(long e) {return solve(e) == 0;}
    public void appendBase(long e) {if ((e = solve(e)) != 0) v.add(e);}
}