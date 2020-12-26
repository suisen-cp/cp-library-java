package lib.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;

public class LinearSieve {
    private final int N;
    private final int primeNum;
    private final int[] minPrimeFactor;
    private final int[] primes;
    public LinearSieve(int n) {
        this.N = n;
        this.minPrimeFactor = new int[N + 1];
        int[] primes = new int[N + 1];
        int primeNum = 0;
        for (int div = 2; div <= N; div++) {
            if (minPrimeFactor[div] == 0) {
                minPrimeFactor[div] = div;
                primes[primeNum++] = div;
            }
            int maxPrime = Math.min(N / div, minPrimeFactor[div]);
            for (int i = 0; i < primeNum; i++) {
                int prime = primes[i];
                if (prime > maxPrime) break;
                minPrimeFactor[prime * div] = prime;
            }
        }
        this.primes = Arrays.copyOf(primes, primeNum);
        this.primeNum = primeNum;
    }
    public int[] minPrimeFactor() {
        return minPrimeFactor;
    }
    public int[] primes() {
        return primes;
    }
    public int primeNum() {
        return primeNum;
    }
    public OptionalInt minPrimeFactor(int n) {
        int prime = minPrimeFactor[n];
        return prime == 0 ? OptionalInt.empty() : OptionalInt.of(prime);
    }
    public ArrayList<IntPrimePower> factorize(int n) {
        ArrayList<IntPrimePower> primeFactorPowers = new ArrayList<>();
        for (int prime = minPrimeFactor[n]; prime != 0; prime = minPrimeFactor[n]) {
            int index = 0;
            while (n % prime == 0) {
                n /= prime;
                index++;
            }
            primeFactorPowers.add(new IntPrimePower(prime, index));
        }
        return primeFactorPowers;
    }
}
