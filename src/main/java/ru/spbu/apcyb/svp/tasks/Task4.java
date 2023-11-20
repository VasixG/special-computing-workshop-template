package ru.spbu.apcyb.svp.tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Entrypoint.
 */
public class Task4 {

  /**
   * Generate file with some numbers.
   *
   * @param filename path to file to be generated
   * @param count    number of numbers
   * @throws IOException if some problems with IO
   */
  public static void generateNumbers(String filename, int count)
      throws IOException {
    try (var fileWriter = new FileWriter(filename)) {
      for (int i = 0; i < count; i++) {
        fileWriter.write(i + System.lineSeparator());
      }
      fileWriter.flush();
    }
  }

  /**
   * Read numbers from file and add it to List.
   *
   * @param filename path to file to be read
   * @return List with numbers from file
   */
  public static List<Double> readNumbersFromFile(String filename) {
    List<Double> numbers = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        numbers.add(Double.parseDouble(line));
      }
    } catch (IOException e) {
      System.out.println("An error while reading the file: " + e.getMessage());
    }
    return numbers;
  }

  /**
   * Write numbers from List to file.
   *
   * @param tanResults write numbers from List to file
   * @param filename   path to the file to be written
   */
  public static void writeNumbersInFile(List<Double> tanResults, String filename) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (Double result : tanResults) {
        writer.write(result.toString());
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Map tan using threads.
   *
   * @param numbers    List with numbers to be mapped
   * @param threadsNum Number of threads
   * @return List with mapped numbers
   */
  public static List<Double> calculateTan(List<Double> numbers, int threadsNum) {

    ExecutorService executor = Executors.newFixedThreadPool(threadsNum);

    try {
      List<CompletableFuture<Double>> futures = numbers.stream()
          .map(number -> CompletableFuture.supplyAsync(
              () -> Math.tan(Math.toRadians(number)), executor))
          .toList();

      return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
          .thenApplyAsync(___ -> futures.stream().map(CompletableFuture::join).toList()).join();

    } catch (ThreadDeath e) {
      System.err.println("ThreadDeath");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      executor.shutdown();
    }
    return Collections.emptyList();
  }

  private static double getComputationTimeThreads(List<Double> numbers, Integer threadNum) {
    final long startTime = System.nanoTime();
    calculateTan(numbers, threadNum);
    return System.nanoTime() - startTime;
  }

  private static double getComputationTimeOneThread(List<Double> numbers) {
    final long startTime = System.nanoTime();
    List<Double> result = new ArrayList<>();
    for(Double num: numbers){
      result.add(Math.tan(Math.toRadians(num)));
    }
    return System.nanoTime() - startTime;
  }

  private static void compareTime(Integer count, Integer threadNum1)
      throws IOException {
    generateNumbers("numbers.txt", count);
    List<Double> numbers = readNumbersFromFile("numbers.txt");

    double timeWithOneThread = getComputationTimeOneThread(numbers);
    double timeWithThreads = getComputationTimeThreads(numbers, threadNum1);
    System.out.printf("Time for %d numbers with %d thread(s) if %f seconds%n", count, 1,
        timeWithOneThread / Math.pow(10, 9));
    System.out.printf("Time for %d numbers with %d thread(s) if %f seconds%n", count,threadNum1,
        timeWithThreads / Math.pow(10, 9));
  }


  /**
   * Class entry point.
   */
  public static void main(String[] args) throws IOException {
    compareTime(1, 10);
    compareTime(100, 10);
    compareTime(1000000,  10);
  }
}
