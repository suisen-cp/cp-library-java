package lib.util.pair;

import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public class LongEntry<V> {
    public long key;
    public V val;
    public LongEntry(long key, V val) {
        this.key = key;
        this.val = val;
    }
    public long getKey() {return key;}
    public V getValue() {return val;}
    public V setValue(V val) {
        V oldValue = this.val;
        this.val = val;
        return oldValue;
    }
    public boolean equals(Object o) {
        if (!(o instanceof LongEntry)) return false;
        LongEntry<?> e = (LongEntry<?>) o;
        return key == e.getKey() && Objects.equals(val, e.val);
    }
    public int hashCode() {
        int keyHash = (int) (key ^ (key >>> 32));
        int valueHash = (val == null ? 0 : val.hashCode());
        return keyHash ^ valueHash;
    }
    public String toString() {return key + "=" + val;}
}