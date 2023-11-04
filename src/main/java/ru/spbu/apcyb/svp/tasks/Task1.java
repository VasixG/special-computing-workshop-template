package ru.spbu.apcyb.svp.tasks;

import java.util.Scanner;

/**
 * Start point class.
 */

public class Task1 {

  /**
   * Start point method.
   */
  public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.println("Enter the amount of change: ");
      long change = getLongFromUser(scanner);

      System.out.println("Input number of denominators: ");
      int numDenominators = getIntFromUser(scanner);

      long[] denominators = getDenominatorsFromUser(scanner, numDenominators);

      Atm atm = new Atm();
      long numCombs = atm.getChangeByDenominators(change, denominators);
      System.out.println(numCombs);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  private static long getLongFromUser(Scanner scanner) {
    while (true) {
      String input = scanner.nextLine().trim();
      try {
        long number = Long.parseLong(input);
        if (number <= 0) {
          System.out.println("Invalid input. Please enter a positive integer: ");
          continue;
        }
        return number;
      } catch (NumberFormatException e) {
        if (input.matches(".*\\D+.*")) {
          System.out.println(
              "The input contains invalid characters. Please enter a positive digit: ");
        }
      }
    }
  }

  private static int getIntFromUser(Scanner scanner) {
    while (true) {
      String input = scanner.nextLine().trim();
      try {
        int number = Integer.parseInt(input);
        if (number <= 0) {
          System.out.println("Invalid input. Please enter a positive integer: ");
          continue;
        }
        return number;
      } catch (NumberFormatException e) {
        if (input.matches(".*\\D+.*")) {
          System.out.println(
              "The input contains invalid characters. Please enter a positive integer: ");
        }
      }
    }
  }

  private static long[] getDenominatorsFromUser(Scanner scanner, int numDenominators) {
    long[] denominators = new long[numDenominators];
    for (int i = 0; i < numDenominators; i++) {
      System.out.print("Enter denominator " + (i + 1) + ": ");
      denominators[i] = getLongFromUser(scanner);
    }
    return denominators;
  }
}


