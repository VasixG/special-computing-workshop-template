package ru.spbu.apcyb.svp.tasks;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

class Task5Test {

  @Test
  void testCountWords() throws IOException {

    Path testFile = Paths.get("src/test/resources/task5.txt");
    Files.writeString(testFile, "hello world\nhello meow");

    Map<String, Long> result = Task5.countWords(testFile);

    assertEquals(2, result.getOrDefault("hello", 0L));
    assertEquals(1, result.getOrDefault("world", 0L));
    assertEquals(1, result.getOrDefault("meow", 0L));
  }

  @Test
  void testWriteWordCounts() throws IOException {
    Map<String, Long> wordCounts = Map.of("hello", 2L, "world", 1L);

    Path outputFile = Paths.get("src/test/resources/counts.txt");
    Task5.writeWordCounts(wordCounts, outputFile.toString());

    String content = Files.readString(outputFile);
    assertTrue(content.contains("hello: 2"));
    assertTrue(content.contains("world: 1"));
  }

}

