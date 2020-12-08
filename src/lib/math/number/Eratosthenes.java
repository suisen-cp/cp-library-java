package lib.math.number;

public class Eratosthenes {
    public static Result solve(int n) {
        if (n <= 0) throw new AssertionError();
        int[] divisors = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            if ((i & 1) != 0) {
                divisors[i] = i;
            } else {
                divisors[i] = 2;
            }
        }
        int primeCount = n >= 2 ? 1 : 0;
        for (int i = 3; i <= n; i += 2) {
            if (divisors[i] == i) {
                primeCount++;
                if ((long) i * i > n) continue;
                for (int j = i * i; j <= n; j += i << 1) {
                    divisors[j] = i;
                }
            }
        }
        int[] primes = new int[primeCount];
        for (int i = 2, j = 0; i <= n; i++) {
            if (divisors[i] == i) primes[j++] = i;
        }
        return new Result(primes, divisors);
    }

    public static class Result {
        private final int[] primes;
        private final int[] divisors;
        Result(int[] primes, int[] divisors) {
            this.primes = primes;
            this.divisors = divisors;
        }
        public int[] primes() {
            return primes;
        }
        public int[] divisors() {
            return divisors;
        }
    }
}
