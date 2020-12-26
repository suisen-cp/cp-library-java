package lib.util;

import java.util.Comparator;

public final class ObjectUtils {
    private ObjectUtils(){}

    public static <T extends Comparable<T>> T max(T o1, T o2) {
        return o1.compareTo(o2) >= 0 ? o1 : o2;
    }
    public static <T> T max(T o1, T o2, Comparator<T> comparator) {
        return comparator.compare(o1, o2) >= 0 ? o1 : o2;
    }
    public static <T extends Comparable<T>> T min(T o1, T o2) {
        return o1.compareTo(o2) <= 0 ? o1 : o2;
    }
    public static <T> T min(T o1, T o2, Comparator<T> comparator) {
        return comparator.compare(o1, o2) <= 0 ? o1 : o2;
    }
}
