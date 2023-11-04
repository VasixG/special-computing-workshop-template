package ru.spbu.apcyb.svp.tasks;

import java.io.InputStream;
import java.util.InputMismatchException;
import org.junit.Test.None;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class Task1Test {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final InputStream originalIn = System.in;

  @BeforeEach
  public void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setIn(originalIn);
  }

  @Test
  public void testNegativeChangeException() {
    String input = "-5\n1\n1";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(output.contains("Invalid input. Please enter a positive integer:"));
  }

  @Test
  public void testNegativeNumOfDenominatorsException() {
    String input = "5\n-1";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(output.contains("Invalid input. Please enter a positive integer:"));
  }

  @Test
  public void testNegativeDenominatorException() {
    String input = "5\n2\n3\n-1\n";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(output.contains("Invalid input. Please enter a positive integer:"));
  }

  @Test
  public void testZeroDenominatorException() {
    String input = "5\n2\n3\n0\n";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(output.contains("Invalid input. Please enter a positive integer:"));
  }

  @Test
  public void testAllOK() {
    String input = "5\n3\n1\n2\n5\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    Task1.main(null);
    assertTrue(outContent.toString().contains("5")); // Replace with actual expected output
  }

  @Test
  public void testNotNumberDenominatorException() {
    String input = "5\n2\n3\na\n";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(outContent.toString().contains("The input contains invalid characters."));
  }

  @Test
  public void testNotNumberChangeException() {
    String input = "asd";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(outContent.toString().contains("The input contains invalid characters."));
  }

  @Test
  public void testNotNumberNumOfDenominatorsException() {
    String input = "5\nsad";
    ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(outContent.toString().contains("The input contains invalid characters."));
  }

}
