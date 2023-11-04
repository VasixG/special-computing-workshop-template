package ru.spbu.apcyb.svp.tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * ATM which can change.
 */
public class Atm {

  /**
   * Get all combinations of change by denominators.
   *
   * @param change       the amount of money which we need to change by denominators
   * @param denominators an array with denominators
   * @return number of combinations
   */
  public long getChangeByDenominators(long change, long[] denominators) {
    long[] counters = new long[denominators.length];
    long c = 0;

    while (true) {
      long summa = calculateSum(denominators, counters);

      if (summa == change) {
        c++;
        printCurrentCombination(denominators, counters);
      }

      boolean shouldContinue = incrementCounters(change, denominators, counters);
      if (!shouldContinue) {
        break;
      }
    }
    return c;
  }

  private long calculateSum(long[] denominators, long[] counters) {
    long summa = 0;
    for (int i = 0; i < denominators.length; ++i) {
      summa += counters[i] * denominators[i];
    }
    return summa;
  }

  private void printCurrentCombination(long[] denominators, long[] counters) {
    List<Long> currentCombination = new ArrayList<>();
    for (int i = 0; i < denominators.length; i++) {
      for (int j = 0; j < counters[i]; j++) {
        currentCombination.add(denominators[i]);
      }
    }
    System.out.println(currentCombination);
  }

  private boolean incrementCounters(long change, long[] denominators, long[] counters) {
    long summa = calculateSum(denominators, counters);
    int indexToIncrement = 0;
    while (indexToIncrement < denominators.length) {
      counters[indexToIncrement]++;
      summa += denominators[indexToIncrement];
      if (summa <= change) {
        return true;
      }
      summa -= counters[indexToIncrement] * denominators[indexToIncrement];
      counters[indexToIncrement] = 0;
      indexToIncrement++;
    }
    return false;
  }
}
