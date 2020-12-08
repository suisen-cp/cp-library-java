package lib.util.ext_std;

import java.util.Map;
import java.util.function.BinaryOperator;

public class MapUtil {
    public static <K, V, Mp extends Map<K, V>> Mp merge(Mp m1, Mp m2, BinaryOperator<V> op) {
        if (m1.size() < m2.size()) return merge(m2, m1, op);
        m2.forEach((key, val) -> m1.merge(key, val, ($, v) -> op.apply(v, val)));
        return m1;
    }
}
