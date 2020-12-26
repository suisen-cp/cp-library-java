package lib.base;

public interface PrimitiveComparable {
    int compareTo(long v);
    default int compareTo(int v) {return compareTo((long) v);}
    default int compareTo(short v) {return compareTo((long) v);}
    default int compareTo(char v) {return compareTo((long) v);}
    default int compareTo(byte v) {return compareTo((long) v);}
    int compareTo(double v);
    default int compareTo(float v) {return compareTo((double) v);}
}