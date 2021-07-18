package lib.math.number;

import java.util.Arrays;

import lib.datastructure.longs.LongSegmentTree;

public class Combination {
    /**
     * Calculates C(N,i) for i = 0, ..., N in O(N(logN)^2/loglogN)
     * @param N <= 10^5
     * @param M <= 10^9
     * @return C(N,i) for i = 0, ..., N
     */
    public static long[] combinationsAnyMod2(final int N, final long M) {
        final ModArithmetic MA = new ModArithmeticBarrett(M);
        Eratosthenes.Result sieve = Eratosthenes.solve(N);
        int[] divisors = sieve.divisors();
        int[] primes = sieve.primes();
        int[] primeOrd = new int[N + 1];
        Arrays.fill(primeOrd, -1);
        final int primeNum = primes.length;
        for (int i = 0; i < primeNum; i++) {
            primeOrd[primes[i]] = i;
        }
        int[] primeCount = new int[primeNum];
        LongSegmentTree prodSegTree = new LongSegmentTree(primeNum, MA::mul, 1);
        long[] binom = new long[N + 1];
        binom[0] = 1;
        for (int i = 1; i <= N >> 1; i++) {
            for (int mul = N - i + 1; mul > 1;) {
                int prime = divisors[mul];
                int count = 0;
                do {
                    mul /= prime;
                    count++;
                } while (mul % prime == 0);
                int ord = primeOrd[prime];
                primeCount[ord] += count;
                prodSegTree.set(ord, MA.pow(prime, primeCount[ord]));
            }
            for (int div = i; div > 1;) {
                int prime = divisors[div];
                int count = 0;
                do {
                    div /= prime;
                    count++;
                } while (div % prime == 0);
                int ord = primeOrd[prime];
                primeCount[ord] -= count;
                prodSegTree.set(ord, MA.pow(prime, primeCount[ord]));
            }
            binom[i] = prodSegTree.allProd();
        }
        for (int i = N; i > N >> 1; i--) {
            binom[i] = binom[N - i];
        }
        return binom;
    }

    static final int MAX_PRIME_FACTOR_NUM = 9;
    /**
     * Calculates C(N,i) for i = 0, ..., N in O(N(loglogM)(logloglogM)) time.
     * @param N <= 10^5
     * @param M <= 10^9
     * @return C(N,i) for i = 0, ..., N
     */
    public static long[] combinationsAnyMod(final int N, final long M) {
        if (N == 0) return new long[]{M == 1 ? 0 : 1};
        final ModArithmetic MA = new ModArithmeticBarrett(M);
        int[] maxPrimeFactor = new int[N + 1];
        int primeFactorNum = 0;
        int[] primeFactor = new int[MAX_PRIME_FACTOR_NUM];
        int[] index = new int[N + 1];
        int[] minPrimeFactor = new int[N + 1];
        for (int div = 2, primeNum = 0, primes[] = new int[N + 1]; div <= N; div++) {
            if (minPrimeFactor[div] == 0) {
                minPrimeFactor[div] = div;
                primes[primeNum++] = div;
                if (M % div == 0) {
                    primeFactor[index[div] = primeFactorNum++] = div;
                    for (int i = div; i <= N; i += div) {
                        maxPrimeFactor[i] = div;
                    }
                }
            }
            int maxPrime = Math.min(N / div, minPrimeFactor[div]);
            for (int i = 0; i < primeNum; i++) {
                int prime = primes[i];
                if (prime > maxPrime) break;
                minPrimeFactor[prime * div] = prime;
            }
        }
        final int K = primeFactorNum;
        long[][] pow = new long[K][];
        for (int i = 0; i < K; i++) {
            int prime = primeFactor[i];
            int max = N / (prime - 1);
            long[] powi = pow[i] = new long[max + 1];
            powi[0] = 1;
            for (int j = 1; j <= max; j++) {
                powi[j] = MA.mul(powi[j - 1], prime);
            }
        }
        long[] inv = new long[N + 1];
        inv[1] = 1;
        for (int i = 2; i <= N; i++) {
            if (minPrimeFactor[i] == i) {
                if (M % i != 0) inv[i] = MA.inv(i);
            } else {
                int prime = minPrimeFactor[i];
                inv[i] = MA.mul(inv[prime], inv[i / prime]);
            }
        }
        ProdSegTree prodSegTree = new ProdSegTree(K, MA);
        int[] primeFactorCount = new int[K];
        long[] binom = new long[N + 1];
        binom[0] = 1;
        long Y = 1;
        for (int i = 1; i <= N >> 1; i++) {
            int num = N - i + 1;
            for (int prime = maxPrimeFactor[num]; prime > 0; prime = maxPrimeFactor[num]) {
                int count = 0;
                do {
                    num /= prime;
                    count++;
                } while (num % prime == 0);
                int idx = index[prime];
                prodSegTree.set(idx, pow[idx][primeFactorCount[idx] += count]);
            }
            int den = i;
            for (int prime = maxPrimeFactor[den]; prime > 0; prime = maxPrimeFactor[den]) {
                int count = 0;
                do {
                    den /= prime;
                    count++;
                } while (den % prime == 0);
                int idx = index[prime];
                prodSegTree.set(idx, pow[idx][primeFactorCount[idx] -= count]);
            }
            Y = MA.mul(Y, MA.mul(num, inv[den]));
            long Z = prodSegTree.prodAll();
            binom[i] = MA.mul(Y, Z);
        }
        for (int i = N; i > N >> 1; i--) {
            binom[i] = binom[N - i];
        }
        return binom;
    }

    @SuppressWarnings("PointlessBitwiseExpression")
    private static final class ProdSegTree {
        private final int N;
        private final ModArithmetic MA;
        private final long[] data;
        ProdSegTree(int n, ModArithmetic MA) {
            int k = 1;
            while (k < n) k <<= 1;
            this.N = k;
            this.data = new long[N << 1];
            Arrays.fill(data, 1);
            this.MA = MA;
        }
        void set(int p, long x) {
            data[p += N] = x;
            for (p >>= 1; p > 0; p >>= 1) data[p] = MA.mul(data[p << 1 | 0], data[p << 1 | 1]);
        }
        long prodAll() {return data[1];}
    }

    // ! Lucas's Theorem !

    public static int mod2(long n, long r) {
        if (n < 0 || r < 0) return 0;
        return (n | r) == n ? 1 : 0;
    }

    public static int mod2(int n, int r) {
        if (n < 0 || r < 0) return 0;
        return (n | r) == n ? 1 : 0;
    }

    /**
     * @return C(n, i) mod 2 for i = 0, ..., n
     */
    public static int[] combinationMod2(int n) {
        int[] binom = new int[n + 1];
        for (int i = n;; i &= n) {
            binom[i] = 1;
            if (--i < 0) return binom;
        }
    }
}
