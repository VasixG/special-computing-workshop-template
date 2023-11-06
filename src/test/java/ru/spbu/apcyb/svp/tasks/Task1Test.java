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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class Task1Test {

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

  @ParameterizedTest
  @ValueSource(strings = {"-5\n1\n1", "5\n-1", "5\n2\n3\n-1\n", "5\n2\n3\n0\n"})
  void testInvalidDigitInput(String arg) {
    ByteArrayInputStream inContent = new ByteArrayInputStream(arg.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(output.contains("Invalid input. Please enter a positive integer:"));
  }

  @Test
  void testAllOK() {
    String input = "5\n3\n1\n2\n5\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    Task1.main(null);
    assertTrue(outContent.toString().contains("5")); // Replace with actual expected output
  }

  @ParameterizedTest
  @ValueSource(strings = {"5\n2\n3\na\n", "asd", "5\nsad"})
  void testInvalidCharacterInput(String arg) {
    ByteArrayInputStream inContent = new ByteArrayInputStream(arg.getBytes());
    System.setIn(inContent);

    Task1.main(null);

    String output = outContent.toString();
    assertTrue(outContent.toString().contains("The input contains invalid characters."));
  }

}
