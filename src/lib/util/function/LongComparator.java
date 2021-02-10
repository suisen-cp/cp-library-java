package lib.util.function;

@FunctionalInterface
public interface LongComparator {
    int compare(long i, long j);
    default boolean eq(long i, long j) { return compare(i, j) == 0; }
    default boolean ne(long i, long j) { return compare(i, j) != 0; }
    default boolean gt(long i, long j) { return compare(i, j) > 0;  }
    default boolean ge(long i, long j) { return compare(i, j) >= 0; }
    default boolean lt(long i, long j) { return compare(i, j) < 0;  }
    default boolean le(long i, long j) { return compare(i, j) <= 0; }

    default LongComparator reversed() { return (i, j) -> compare(j, i); }
}