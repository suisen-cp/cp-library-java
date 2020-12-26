package lib.algorithm.ints;

import lib.util.array.IntArrayFactory;

import java.util.Collection;
import java.util.HashMap;

public final class IntCompress {
    private final int n;
    private final HashMap<Integer, Integer> compressMap;
    private final int[] compressed;
    private final int[] sorted;
    public IntCompress(int[] a, boolean oneIndexed) {
        this.n = a.length;
        this.sorted = sort(IntArrayFactory.unique(a));
        this.compressMap = new HashMap<>();
        for (int i = 0; i < sorted.length; i++) {
            compressMap.put(sorted[i], oneIndexed ? i + 1 : i);
        }
        this.compressed = IntArrayFactory.setAll(n, i -> compressMap.get(a[i]));
    }
    public IntCompress(Collection<Integer> collection, boolean oneIndexed) {
        this(IntArrayFactory.toArray(collection), oneIndexed);
    }
    public int compressedSize() {
        return sorted.length;
    }
    public int[] compressed() {
        return compressed;
    }
    public int restore(int i) {
        return sorted[i];
    }
    public int compress(int x) {
        return compressMap.get(x);
    }
    private static int[] sort(int[] a) {
        int[]b=new int[a.length];
        int[]c0=new int[0x101],c1=new int[0x101],c2=new int[0x101],c3=new int[0x101];
        for(int v:a){c0[(v&0xff)+1]++;c1[(v>>>8&0xff)+1]++;c2[(v>>>16&0xff)+1]++;c3[(v>>>24^0x80)+1]++;}
        for(int i=0;i<0x100;i++){c0[i+1]+=c0[i];c1[i+1]+=c1[i];c2[i+1]+=c2[i];c3[i+1]+=c3[i];}
        for(int v:a)b[c0[v&0xff]++]=v;for(int v:b)a[c1[v>>>8&0xff]++]=v;
        for(int v:a)b[c2[v>>>16&0xff]++]=v;for(int v:b)a[c3[v>>>24^0x80]++]=v;
        return a;
    }
}
