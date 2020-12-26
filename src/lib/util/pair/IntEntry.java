package lib.util.pair;

import java.util.Objects;

public class IntEntry<V> {
    public int key;
    public V val;
    public IntEntry(int key, V val) {
        this.key = key;
        this.val = val;
    }
    public int getKey() {return key;}
    public V getValue() {return val;}
    public V setValue(V val) {
        V oldValue = this.val;
        this.val = val;
        return oldValue;
    }
    public boolean equals(Object o) {
        if (!(o instanceof IntEntry)) return false;
        IntEntry<?> e = (IntEntry<?>) o;
        return key == e.getKey() && Objects.equals(val, e.val);
    }
    public int hashCode() {
        int keyHash = key;
        int valueHash = (val == null ? 0 : val.hashCode());
        return keyHash ^ valueHash;
    }
    public String toString() {return key + "=" + val;}
}