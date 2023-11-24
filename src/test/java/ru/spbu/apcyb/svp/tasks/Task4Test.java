package ru.spbu.apcyb.svp.tasks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Task4Test {

  @Test
  void testGenerateNumbers() throws IOException {
    String filename = "testNumbers.txt";
    Task4.generateNumbers(filename, 100);
    Assertions.assertTrue(Files.exists(Paths.get(filename)));

    // Optionally, check the content of the file
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      for (int i = 0; i < 100; i++) {
        Assertions.assertEquals(String.valueOf(i), reader.readLine());
      }
    }
  }

  @Test
  void testReadNumbersFromFile() throws IOException {
    String filename = "testNumbers.txt";
    Task4.generateNumbers(filename, 10);
    List<Double> numbers = Task4.readNumbersFromFile(filename);
    Assertions.assertNotNull(numbers);
    Assertions.assertEquals(10, numbers.size());
    for (int i = 0; i < 10; i++) {
      Assertions.assertEquals((double) i, numbers.get(i));
    }
  }

  @Test
  void testCalculateTan() {
    List<Double> numbers = List.of(0.0, 45.0, 90.0);
    List<Double> tanResults = Task4.calculateTan(numbers, 2);
    Assertions.assertNotNull(tanResults);
    Assertions.assertEquals(3, tanResults.size());
    Assertions.assertEquals(0.0, tanResults.get(0));
    Assertions.assertEquals(Math.tan(Math.toRadians(45.0)), tanResults.get(1));
    Assertions.assertEquals(Math.tan(Math.toRadians(90.0)), tanResults.get(2));
  }

  @Test
  void testWriteNumbersInFile() throws IOException {
    String filename = "testTanNumbers.txt";
    List<Double> numbers = List.of(0.0, 1.0, Math.sqrt(3));
    Task4.writeNumbersInFile(numbers, filename);
    Assertions.assertTrue(Files.exists(Paths.get(filename)));

    // Optionally, check the content of the file
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      Assertions.assertEquals("0.0", reader.readLine());
      Assertions.assertEquals("1.0", reader.readLine());
      Assertions.assertEquals(String.valueOf(Math.sqrt(3)), reader.readLine());
    }
  }
}
