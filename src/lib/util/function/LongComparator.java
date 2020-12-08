package lib.util.function;

@FunctionalInterface
public interface LongComparator {
    int compare(long i, long j);
    default boolean eq(final long i, final long j) {return compare(i, j) == 0;}
    default boolean ne(final long i, final long j) {return compare(i, j) != 0;}
    default boolean gt(final long i, final long j) {return compare(i, j) > 0;}
    default boolean ge(final long i, final long j) {return compare(i, j) >= 0;}
    default boolean lt(final long i, final long j) {return compare(i, j) < 0;}
    default boolean le(final long i, final long j) {return compare(i, j) <= 0;}
}