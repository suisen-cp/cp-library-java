package lib.util.array;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.IntFunction;

@SuppressWarnings("unchecked")
public class ObjArrayFactory {
    public static <T> T[] setAll(int n, IntFunction<T> f, Class<T> clazz) {
        T[] ret = (T[]) Array.newInstance(clazz, n);
        Arrays.setAll(ret, f);
        return ret;
    }
}