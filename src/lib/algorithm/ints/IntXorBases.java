package lib.algorithm.ints;

import java.util.PrimitiveIterator;

import lib.util.collections.ints.IntArrayList;

public final class IntXorBases {
    private final IntArrayList v = new IntArrayList();

    public IntXorBases() {}
    public IntXorBases(int[] a) {for (int e : a) appendBase(e);}
    private int solve(int e) {
        PrimitiveIterator.OfInt iter = v.iterator();
        while (iter.hasNext()) e = Math.min(e, e ^ iter.nextInt());
        return e;
    }
    public boolean isExpressible(int e) {return solve(e) == 0;}
    public void appendBase(int e) {if ((e = solve(e)) != 0) v.add(e);}
    public int size() { return v.size(); }
}