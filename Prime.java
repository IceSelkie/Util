package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static util.Util.factorQuantity;
import static util.Util.hasFactor;

public class Prime
{
  private static ArrayList<Long> primes = new ArrayList<>();
  private static ArrayList<Double> logPrime = new ArrayList<>();
  private static HashMap<Long, HashMap<Long, Integer>> compositeFactorizations = new HashMap<>();
  private static long highestValue = 0;


  /**
   * Checks to see if an integer is prime.
   * Stores a list of prime integers up to the square root of the largest
   * number checked since program was started.
   *
   * @param n Number checked
   * @return if said number is prime or not.
   */
  public static boolean isPrime(long n)
  {
    if (n < 2)
      return false;
    int index = Collections.binarySearch(primes, (Long)n);
    if (index >= 0)
      return true;
    if (-index - 1 != primes.size())
      return false;

    int sqrt = (int)Math.sqrt(n);

    // Make sure primes list is filled up to the needed point
    if (highestValue < sqrt + 1)
      findMorePrimesTo(sqrt + 1);
    if (highestValue > n)
      return Collections.binarySearch(primes, (Long)n) >= 0;

    // See if it is composite to any previous prime
    boolean prime = true;
    int place = 0;
    long fac = 0;
    while (prime && fac < sqrt)
    {
      fac = primes.get(place++);
      if (n % fac == 0 && fac != n)
        prime = false;
    }

    // And return if it is prime or not.
    return prime;
  }

  public static long isPowerOfPrime(long n)
  {
    if (isPrime(n))
      return n;
    if (primes.size()>logPrime.size())
      for (int i = logPrime.size(); i<primes.size(); i++)
        logPrime.add(i,Math.log(primes.get(i)));
    long sqrt = Math.round(Math.sqrt(n));
    double log = Math.log(n);
    double pow;
    for (int i = 0; getPrime(i)<sqrt; i++)
    {
      pow = log / logPrime.get(i);
      if (Math.abs(pow-((long)pow))<0.0001) // Todo: Maybe make more efficent?
      {
        if (factorQuantity(n, getPrime(i)) == Math.round(pow))
          return getPrime(i);
      }
    }
    return 0L;
  }

  public static long getPrime(int n)
  {
    if (primes.size() <= n)
      findNMorePrimes(n - primes.size()+1);
    return primes.get(n);
  }

  public static int getPrimePlace(long p)
  {
    if (!isPrime(p))
      throw new IllegalArgumentException("The passed long is not prime!");
    return primes.indexOf(p);
  }

  /**
   * Returns the nearest prime to a given value. If two are equidistant, returns the lowest one.
   * @param val
   * @return
   */
  public static long nearestPrime(long val)
  {
    if (isPrime(val))
      return val;
    if (val < 2)
      return 2;
    findMorePrimesTo(val);
    int suggestedIndex = -(1 + Collections.binarySearch(primes, val));
    long primeAbove = getPrime(suggestedIndex);
    long primeBelow = getPrime(suggestedIndex - 1);
    //System.out.println("{"+primeBelow+", "+val+", "+primeAbove+"}");

    return (val - primeBelow <= primeAbove - val) ? primeBelow : primeAbove;
  }

  // up to the value of n
  public static void findMorePrimesTo(long n)
  {
    while (highestValue < n)
      findNMorePrimes(1);
    //findNMorePrimes(1);
  }

  public static void findNMorePrimes(int n)
  {
    if (highestValue == 0)
    {
      primes.add(2L);
      highestValue = 2;
    }

    long i = highestValue;
    int numFound = 0;
    while (numFound < n)
    {
      int sqrt = (int) Math.sqrt(++i);
      boolean found = false;
      int f = 0;
      long val = 0;
      while (!found && val < sqrt)
      {
        val = primes.get(f++);
        if (i % val == 0)
          found = true;
      }
      if (!found)
      {
        primes.add(i);
        numFound++;
      }
    }
    highestValue = i;
  }

  public static HashMap<Long, Integer> getPrimeFactorsAsMap(long composite)
  {
    if (isPrime(composite))
    {
      HashMap<Long, Integer> ret = new HashMap<Long, Integer>();
      ret.put(composite,1);
      return ret;
    }
    else
      if (compositeFactorizations.containsKey(composite))
        return (HashMap<Long, Integer>) compositeFactorizations.get(composite).clone();

    HashMap<Long, Integer> map = new HashMap<>();
    long sqrt = (long) Math.sqrt(composite);
    long p = 0;
    for (int i = 0; p<sqrt; i++)
    {
      p = getPrime(i);
      if (hasFactor(composite, p))
      {
        int facQuant = factorQuantity(composite, p);
        map.put(p, facQuant);
        composite /= Math.round(Math.pow(p,facQuant));
        long primePow = isPowerOfPrime(composite);
        if (primePow>0)
        {
          map.put(primePow, factorQuantity(composite,primePow));
          composite = 1;
        }
        sqrt = (long) Math.sqrt(composite);
      }
    }
    compositeFactorizations.put(composite, (HashMap<Long, Integer>) map.clone());
    return map;
  }
}


























