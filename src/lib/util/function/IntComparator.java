package lib.util.function;

@FunctionalInterface
public interface IntComparator {
    int compare(int i, int j);
    default boolean eq(final int i, final int j) {return compare(i, j) == 0;}
    default boolean ne(final int i, final int j) {return compare(i, j) != 0;}
    default boolean gt(final int i, final int j) {return compare(i, j) > 0;}
    default boolean ge(final int i, final int j) {return compare(i, j) >= 0;}
    default boolean lt(final int i, final int j) {return compare(i, j) < 0;}
    default boolean le(final int i, final int j) {return compare(i, j) <= 0;}
}