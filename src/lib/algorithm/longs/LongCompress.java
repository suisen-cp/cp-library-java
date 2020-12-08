package lib.algorithm.longs;

import lib.util.array.IntArrayFactory;
import lib.util.array.LongArrayFactory;

import java.util.Collection;
import java.util.HashMap;

public class LongCompress {
    private final int n;
    private final HashMap<Long, Integer> compressMap;
    private final int[] compressed;
    private final long[] sorted;
    public LongCompress(long[] a, boolean oneIndexed) {
        this.n = a.length;
        this.sorted = sort(LongArrayFactory.unique(a));
        this.compressMap = new HashMap<>();
        for (int i = 0; i < sorted.length; i++) {
            compressMap.put(sorted[i], oneIndexed ? i + 1 : i);
        }
        this.compressed = IntArrayFactory.setAll(n, i -> compressMap.get(a[i]));
    }
    public LongCompress(Collection<Long> collection, boolean oneIndexed) {
        this(LongArrayFactory.toArray(collection), oneIndexed);
    }
    public int compressedSize() {
        return sorted.length;
    }
    public int[] compressed() {
        return compressed;
    }
    public long restore(int i) {
        return sorted[i];
    }
    public int compress(long x) {
        return compressMap.get(x);
    }
    private static long[] sort(long[] a) {
        long[]b=new long[a.length];
        int[]c0=new int[0x101],c1=new int[0x101],c2=new int[0x101],c3=new int[0x101];
        int[]c4=new int[0x101],c5=new int[0x101],c6=new int[0x101],c7=new int[0x101];
        for(long v:a){
            c0[(int)(v&0xff)+1]++;c1[(int)(v>>>8&0xff)+1]++;c2[(int)(v>>>16&0xff)+1]++;c3[(int)(v>>>24&0xff)+1]++;
            c4[(int)(v>>>32&0xff)+1]++;c5[(int)(v>>>40&0xff)+1]++;c6[(int)(v>>>48&0xff)+1]++;c7[(int)(v>>>56^0x80)+1]++;
        }
        for(int i=0;i<0x100;i++){
            c0[i+1]+=c0[i];c1[i+1]+=c1[i];c2[i+1]+=c2[i];c3[i+1]+=c3[i];
            c4[i+1]+=c4[i];c5[i+1]+=c5[i];c6[i+1]+=c6[i];c7[i+1]+=c7[i];
        }
        for(long v:a)b[c0[(int)(v&0xff)]++]=v;for(long v:b)a[c1[(int)(v>>>8&0xff)]++]=v;
        for(long v:a)b[c2[(int)(v>>>16&0xff)]++]=v;for(long v:b)a[c3[(int)(v>>>24&0xff)]++]=v;
        for(long v:a)b[c4[(int)(v>>>32&0xff)]++]=v;for(long v:b)a[c5[(int)(v>>>40&0xff)]++]=v;
        for(long v:a)b[c6[(int)(v>>>48&0xff)]++]=v;for(long v:b)a[c7[(int)(v>>>56^0x80)]++]=v;
        return a;
    }
}
