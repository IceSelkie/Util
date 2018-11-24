package util;

import apcslib.Format;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static util.Prime.getPrimeFactorsAsMap;
import static util.Prime.isPrime;

/**
 * Generic utility methods that could be used any number of times.
 */
public class Util
{

  /**
   * moneyValue - Converts a double value representing an amount of money
   * in USD into the corresponding string value.
   * \nEx: moneyValue(7523.1) will return "$7,523.10"
   *
   * @param value Quantity of money in USD
   * @return String representation
   */
  public static String moneyValue(double value)
  {
    // Add the values in the hundreds place and below to the string
    String ret = Format.left(value % 1000, 4, 2);

    // Previous variable for leading zeros check
    int prev = (int) value;
    // Continue adding digits on as needed
    int rem = prev / 1000;
    while (rem > 0)
    {
      // Add leading zeros when necessary
      if (prev % 1000 < 100)
        ret = "0" + ret;
      if (prev % 1000 < 10)
        ret = "0" + ret;

      // Add next digits on
      ret = rem % 1000 + "," + ret;

      // Prepare for next cycle
      prev = rem;
      rem /= 1000;
    }

    // Add dollar sign on and return!
    return "$" + ret;
  }

  /**
   * Choose - Choose/combination function from math.
   * How many different ways can you <b><i>choose</i></b>
   * <b><i>k</b></i> objects out of <b><i>n</i></b> objects.
   * Also said as "n choose k"
   *
   * @param n Quantity to choose from (like 52 cards)
   * @param k Quantity being chosen (like 5 card hand)
   * @return Number of ways you can choose n objects out of k objects.
   */
  public static BigInteger choose(int n, int k)
  {
    // You cant choose more objects than are in the set, or less than 0 objects.
    if (!(k <= n && k >= 0))
      return BigInteger.ZERO;

    // Efficiency, prevent excess use of math.
    // ~~~~ 0 1 2 3 4 5 6 7 8 9 ~~~~
    // k=0: 1          // 0
    // k=1: 1 1        // 0
    // k=2: 1 2 1      // 1
    // k=3: 1 3 3 1    // 1
    // k=4: 1 4 6 4 1  // 2
    // k=5: 1 5 0 0 5 1// 2
    if (k < n / 2)
      k = n - k;

    // Actual math:
    //   Initial value
    BigInteger val = BigInteger.ONE;
    //   Numerator loop
    for (Long i = (long) n; i > (long) k; i--)
      val = val.multiply(new BigInteger(i.toString()));
    //   Denominator loop (prevent identical choose patterns)
    for (Long i = (long) (n - k); i > 1L; i--)
      val = val.divide(new BigInteger(i.toString()));

    // Return
    if (!val.min(BigInteger.ZERO).equals(BigInteger.ZERO))
      System.out.println("\nERROR: UNDERFLOW");
    return val;
  }

  /**
   * chooseFactor - Efficiently tells if a choose value has a factor of a given value or not.
   *
   * @param n      Quantity to choose from (like 52 cards)
   * @param k      Quantity being chosen (like 5 card hand)
   * @param factor Factor to test for
   */
  public static boolean chooseFactor(int n, int k, int factor)
  {
    // You cant choose more objects than are in the set, or less than 0 objects.
    if (!(k <= n && k >= 0))
      return false;

    // Change k when needed for efficiency (~3x)
    if (k < n / 2)
      k = n - k;

    HashMap<Long, Integer> factorFactors = getPrimeFactorsAsMap(factor);
    HashMap<Long, Integer> factorsFound = new HashMap<>();
    for (Long fac : factorFactors.keySet())
      factorsFound.put(fac, 0);

    // When the number being multiplied
    for (int i = n; i > k; i--)
      for (Long j : factorFactors.keySet())
        if (factorFactors.containsKey(j))
          factorsFound.replace(j, factorsFound.get(j) + factorQuantity(i, j));
    for (int i = (n - k); i > 1; i--)
      for (Long j : factorFactors.keySet())
        if (factorFactors.containsKey(j))
          factorsFound.replace(j, factorsFound.get(j) - factorQuantity(i, j));

    int numberOfFactors = 0;

    for (Long fac : factorFactors.keySet())
    {
      factorsFound.replace(fac, Math.max(factorsFound.get(fac), 0));
      numberOfFactors = Math.min(factorsFound.get(fac) / factorFactors.get(fac), numberOfFactors);
    }
    return numberOfFactors > 0;
  }

  public static boolean chooseHasFactorPrime(int n, int k, int factor)
  {
    if (!(k <= n && k >= 0))
      return false;

    if (k < n / 2)
      k = n - k;

    int facCount = 0;
    for (int i = n; i > k; i--)
      facCount += factorQuantity(i, factor);
    for (int i = (n - k); i > 1; i--)
      facCount -= factorQuantity(i, factor);

    return facCount > 0;
  }

  /**
   * hasFactor - Tests to see if a number <b><i>n</i></b> has a factor of <b><i>f</i></b>.
   *
   * @param n Number to check for factors in
   * @param f The factor being tested for
   */
  public static boolean hasFactor(long n, long f)
  {
    return n % f == 0;
  }

  /**
   * factorQuantity - Takes a number <b><i>n</i></b>, and returns the
   * quantity of factor it has that are <b><i>f</i></b>.
   *
   * @param n Number to check for factors in
   * @param f The factor being tested for
   * @return Quantity of factors
   */
  public static int factorQuantity(long n, long f)
  {
    if (f == 1)
      return 1;
    int q = 0;
    while (hasFactor(n, f))
    {
      n /= f;
      q++;
    }
    return q;
  }


  /**
   * Calculates the least common multiple of two passed integers.
   *
   * @param a One of the two integers to find the common multiple of.
   * @param b One of the two integers to find the common multiple of.
   * @return The multiple found.
   */
  public static long lcm(int a, int b) //TODO: doesnt seem to work. See ProjectEuler, Problem 4. Gave 931170240 instead of 232792560 (4x actual)
  {
    int n, min;
    long returnValue = (long) a * (long) b;

    // If both of them are prime, then the least common multiple is their product.
    if (isPrime(a) && isPrime(b))
      return returnValue;

    // Find factors that they share
    n = 2;
    min = Math.min(a, b);
    while (n <= min)
    {
      if (isPrime(n) && hasFactor(a, n) && hasFactor(b, n))
        returnValue /= n * Math.min(factorQuantity(a, n), factorQuantity(b, n));
      n++;
    }
    return returnValue;
  }

  /**
   * findAndReplaceAll takes a string, finds all instances of another string in it,
   * and replaces them with another string.
   *
   * @param orig       The original string to parse
   * @param find       The string to find in the original string
   * @param replace    The string to replace the one that is being searched for
   * @param ignoreCase If the case of the original string should be ignored in the search.
   * @return The string with the values replaced.
   */
  public static String findAndReplaceAll(String orig, String find, String replace, boolean ignoreCase)
  {
    String origToSearch = ignoreCase ? orig.toLowerCase() : orig;
    String stringToFind = ignoreCase ? find.toLowerCase() : find;
    int location = origToSearch.indexOf(stringToFind);
    String newString = new String(orig);

    while (location >= 0)
    {
      newString = newString.substring(0, location) + replace + newString.substring(location + find.length());
      origToSearch = ignoreCase ? newString.toLowerCase() : newString;
      location = origToSearch.indexOf(stringToFind);
    }
    return newString;
  }

  /**
   * Returns if the character is a vowel (in english) or not.
   */
  public static boolean isVowel(char letter)
  {
    return (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u');
  }

  /**
   * Returns if the first character in the string is a vowel (in english) or not.
   */
  public static boolean startsWithVowel(String str)
  {
    String fLetter = str.substring(0, 1).toLowerCase();
    return (fLetter.equals("a") || fLetter.equals("e") || fLetter.equals("i") || fLetter.equals("o") || fLetter.equals("u"));
  }

  /**
   * Returns if the string contains a vowel or not.
   */
  public static boolean containsVowel(String str)
  {
    boolean ret = false;
    long len = str.length();
    for (int i = 0; i < len && !ret; i++)
    {
      if (startsWithVowel(str.substring(i)))
        ret = true;
    }
    return ret;
  }

  /**
   * Returns if the character is a letter or not.
   */
  public static boolean isAlpha(char letter)
  {
    return ((letter >= 'a') && (letter >= 'z'));
  }

  /**
   * Returns if the first character in the string is a letter or not.
   */
  public static boolean startsWithAlpha(String str)
  {
    String fLetter = str.substring(0, 1).toLowerCase();
    return (fLetter.compareTo("a") >= 0 && fLetter.compareTo("z") <= 0);
  }

  /**
   * Returns if the string contains an english letter or not.
   */
  public static boolean containsAlpha(String str)
  {
    boolean ret = false;
    long len = str.length();
    for (int i = 0; i < len && !ret; i++)
    {
      if (startsWithAlpha(str.substring(i)))
        ret = true;
    }
    return ret;
  }

  /**
   * Returns if the character is a consonant (in english) or not.
   */
  public static boolean isCons(char letter)
  {
    return (isAlpha(letter) && !isVowel(letter));
  }

  /**
   * Returns if the first character in the string is a consonant (in english) or not.
   */
  public static boolean startsWithCons(String str)
  {
    return (startsWithAlpha(str) && !startsWithVowel(str));
  }

  /**
   * Returns if the string contains a vowel or not.
   */
  public static boolean containsCons(String str)
  {
    boolean ret = false;
    long len = str.length();
    for (int i = 0; i < len && !ret; i++)
    {
      if (startsWithCons(str.substring(i)))
        ret = true;
    }
    return ret;
  }

  public static int[] charArrayToIntArray(char[] array)
  {
    int[] newArray = new int[array.length];
    for (int i = 0; i < array.length; i++)
      newArray[i] = (int) array[i];
    return newArray;
  }

  public static Integer[] charArrayToIntegerArray(char[] array)
  {
    Integer[] newArray = new Integer[array.length];
    for (int i = 0; i < array.length; i++)
      newArray[i] = (int) array[i];
    return newArray;
  }

  public static <T> T[] arrayConcat(T[] arr0, T[]... rest) {
    Class commonSuperclass = arr0.getClass().getComponentType();
    int totalLen = arr0.length;
    for (T[] arr: rest) {
      totalLen += arr.length;
      Class compClass = arr.getClass().getComponentType();
      while (! commonSuperclass.isAssignableFrom(compClass)) {
        if (compClass.isAssignableFrom(commonSuperclass)) {
          commonSuperclass = compClass;
          break;
        }
        commonSuperclass = commonSuperclass.getSuperclass();
        compClass = compClass.getSuperclass();
      }
    }
    T[] all = (T[]) Array.newInstance(commonSuperclass, totalLen);
    int copied = arr0.length;
    System.arraycopy(arr0, 0, all, 0, copied);
    for (T[] arr: rest) {
      System.arraycopy(arr, 0, all, copied, arr.length);
      copied += arr.length;
    }
    return all;
  }

  public static Integer[] intArrayAsIntegerArray(int... ints)
  {
    int len = ints.length;
    Integer[] intgs = new Integer[len];
    for (int i = 0; i < len; i++)
      intgs[i] = ints[i];
    return intgs;
  }


  public static String toHex(char in)
  {
    //int in = (char) byt;
    //if (in<0)
    //  in= (char)(in-Byte.MIN_VALUE+Byte.MAX_VALUE);
    int firstval = in / 16;
    int secondval = in % 16;
    char first;
    if (firstval <= 9)
      first = (char) ('0' + firstval);
    else
      first = (char) ('a' + firstval - 10);
    char second = (char) (in % 16 > 9 ? 'a' + (in % 16) - 10 : '0' + in % 16);
    return "" + first + second;
    //return (""+(char)(in / 16 > 9 ? 'a'+ (in / 16)-10 : '0'+ in / 16)) + (char)(in % 16 > 9 ? 'a'+ (in % 16)-10 : '0'+ in % 16);
  }

  public static String toHex(char[] bytes)
  {
    String ret = "";
    for (char b : bytes)
    {
      ret += toHex(b);
    }
    return ret;
  }

  public static int[] iToArray(int... array)
  {
    return array;
  }

  public static int binOn(char byteVal)
  {
    int ret = 0;
    while (byteVal>0)
    {
      if (byteVal % 2 == 1)
        ret++;
      byteVal/=2;
    }
    return ret;
  }

  public static String aTS(int[] array)
  {
    if (array.length==0)
      return "{ }";
    String ret = "{";
    ret += array[0];//.toString();
    for (int i=1; i<array.length; i++)
      ret += ", "+array[i];
    return ret + "}";
  }
  public static String aTS(Object[] array)
  {
    if (array.length==0)
      return "{ }";
    String ret = "{";
    ret += array[0];//.toString();
    for (int i=1; i<array.length; i++)
      ret += ", "+array[i];
    return ret + "}";
  }
  public static String aaTS(int[][] array)
  {
    if (array.length==0)
      return "{ }";
    String ret = "{";
    ret += aTS(array[0]);//.toString();
    for (int i=1; i<array.length; i++)
      ret += ", "+aTS(array[i]);
    return ret + "}";
  }
  public static String aTS(boolean[] array)
  {
    if (array.length==0)
      return "{ }";
    String ret = "{";
    ret += (array[0]?"T":"F");//.toString();
    for (int i=1; i<array.length; i++)
      ret += ", "+(array[i]?"T":"F");
    return ret + "}";
  }
  public static String aaTS(boolean[][] array)
  {
    if (array.length==0)
      return "{ }";
    String ret = "{";
    ret += aTS(array[0]);//.toString();
    for (int i=1; i<array.length; i++)
      ret += ", "+aTS(array[i]);
    return ret + "}";
  }

  public static void displayHyEc()
  {
    System.out.println("\u00A7b\u00A7m\u00A7l>\u00A7r\u00A7e*\u00A7b\u00A7m\u00A7l<\u00A7r \u00A77Hyper\u00A7fEclipse\u00A76Tekkit Server!\u00A7r \u00A7b\u00A7m\u00A7l>\u00A7r\u00A7e*\u00A7b\u00A7m\u00A7l<\u00A7r");
  }

  public static String getTime()
  {
    return new SimpleDateFormat("YYYY-MM-dd").format(new Date()) + "T" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + "Z";
  }

  public static String getTime(Date date)
  {
    return new SimpleDateFormat("YYYY-MM-dd").format(date) + "T" + new SimpleDateFormat("HH:mm:ss.SSS").format(date) + "Z";
  }
}