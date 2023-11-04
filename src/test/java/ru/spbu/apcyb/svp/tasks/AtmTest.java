package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * ATM tests.
 */
public class AtmTest {

  @Test
  public void testGetChangeByDenominatorsSingleDenomination() {
    Atm atm = new Atm();
    long[] denominations = {1};
    long result = atm.getChangeByDenominators(3, denominations);
    assertEquals(1, result);
  }

  @Test
  public void testGetChangeByDenominatorsMultipleDenominations() {
    Atm atm = new Atm();
    long[] denominations = {1, 2, 5};
    long result = atm.getChangeByDenominators(5, denominations);
    assertEquals(4, result);
  }

  @Test
  public void testGetChangeByDenominatorsNoCombinationsPossible() {
    Atm atm = new Atm();
    long[] denominations = {5, 10};
    long result = atm.getChangeByDenominators(3, denominations);
    assertEquals(0, result);
  }

  @Test
  public void testGetChangeByDenominatorsZeroAmount() {
    Atm atm = new Atm();
    long[] denominations = {5, 10};
    long result = atm.getChangeByDenominators(0, denominations);
    assertEquals(1, result);
  }
}

