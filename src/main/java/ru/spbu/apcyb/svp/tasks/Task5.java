package ru.spbu.apcyb.svp.tasks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Program entrypoint
 */
public class Task5 {

  static final Logger logger = Logger.getLogger("");

  static Map<String, Long> countWords(Path filePath, Charset charset) {
    try (Stream<String> lines = Files.lines(filePath, charset)) {
      return lines.flatMap(line -> Arrays.stream(line.split("[^a-zA-ZЁёА-я0-9]")))
          .filter(line -> !line.isEmpty()).map(String::toLowerCase)
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    } catch (IOException e) {
      logger.log(Level.INFO, "IO error");
      return Collections.emptyMap();
    }
  }

  static void writeWordCounts(Map<String, Long> wordCounts, String fileName) {
    Path outputPath = Paths.get(fileName);
    try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
      for (Map.Entry<String, Long> entry : wordCounts.entrySet()) {
        writer.write(entry.getKey() + ": " + entry.getValue());
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void writeWordsAsync(Map<String, Long> words, String filepath) {
    ExecutorService executor = Executors.newFixedThreadPool(
        10);
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (Map.Entry<String, Long> entry : words.entrySet()) {
      CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        try {
          Path path = Paths.get(filepath + entry.getKey() + ".txt");
          Files.write(path, Collections.singleton(entry.getKey()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }, executor);

      futures.add(future);
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    executor.shutdown();
  }

  /**
   * Class entrypoint
   */
  public static void main(String[] args) {
    Path filePath = Paths.get("text/task5.txt");
    Map<String, Long> wordCounts = countWords(filePath, Charset.forName("Windows-1251"));

    writeWordCounts(wordCounts, "text/counts.txt");

    writeWordsAsync(wordCounts, "text/words/");
  }

}
