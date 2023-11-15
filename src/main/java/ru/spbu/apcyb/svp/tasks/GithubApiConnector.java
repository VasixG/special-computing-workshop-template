package ru.spbu.apcyb.svp.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Optional;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * The {@code GithubApiConnector} class is a connector to the GitHub API using the access token from
 * environment variables.
 *
 * <p>It uses the dotenv library to load environment variables from a .env file</p>
 *
 * <p>The class assumes that a .env file is present with GITHUB_TOKEN.
 * GITHUB_TOKEN is the access token for the GitHub API. </p>
 */
public class GithubApiConnector {

  private final String accessToken;
  private final static String[] reasons = {"mention", "review_requested", "assign", "author"};

  private String lastNotificationData = "2023-01-01T00:00:00Z";
  private String lastCommitData = "2023-01-01T00:00:00Z";

  private String lastIssueData = "2023-01-01T00:00:00Z";

  /**
   * Constructs a new {@code GithubApiConnector} instance.
   */
  public GithubApiConnector() {
    Dotenv dotenv = Dotenv.load();
    this.accessToken = dotenv.get("GITHUB_TOKEN");
  }

  /**
   * Fetches notifications for the authenticated user from the GitHub API.
   *
   * @return A {@link JsonArray} containing notification data. Each element in the array is a
   * {@link JsonObject} representing a single notification.
   */
  public Optional<JsonArray> fetchNotifications() {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      String url = String.format("https://api.github.com/notifications?all=true&since=%s",
          this.lastNotificationData);
      HttpGet request = new HttpGet(url);
      request.setHeader("Content-type", "application/json; charset=UTF-8");
      request.addHeader("Authorization", "Bearer " + this.accessToken);

      try (var response = client.execute(request)) {
        Optional<JsonArray> filtered_notifications = parseNotifications(
            EntityUtils.toString(response.getEntity()));
        filtered_notifications.ifPresent(
            jsonElements -> this.lastNotificationData = jsonElements.get(0).getAsJsonObject()
                .get("updated_at").getAsString());
        return filtered_notifications;
      }
    } catch (IOException e) {
      System.err.println("ClientIOErr");
      return Optional.empty();
    }
  }

  private static Optional<JsonArray> parseNotifications(String response) {
    JsonElement jsonElement = JsonParser.parseString(response);

    if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message")) {
      System.err.println(
          "Error from GitHub API: " + jsonElement.getAsJsonObject().get("message").getAsString());
      return Optional.empty();
    }

    JsonArray notifications = JsonParser.parseString(response).getAsJsonArray();
    JsonArray filtered_notifications = new JsonArray();
    for (JsonElement notificationElement : notifications) {
      JsonObject notification = notificationElement.getAsJsonObject();
      String reason = notification.get("reason").getAsString();
      if (Arrays.asList(reasons).contains(reason)) {
        filtered_notifications.add(notificationElement);
      }
    }
    return Optional.of(filtered_notifications);
  }

  /**
   * Fetches a list of commits from a specific GitHub repository.
   *
   * @param authorName The username of owner of the GitHub repository.
   * @param repoName   The name of the repository from which to fetch commits.
   * @return A {@link JsonArray} of commits. Each element in the array is a {@link JsonObject}
   * representing a single commit.
   */
  public Optional<JsonArray> fetchCommits(String authorName, String repoName) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpGet request = new HttpGet(
          "https://api.github.com/repos/" + authorName + "/" + repoName + String.format(
              "/commits?since=%s", this.lastCommitData));
      request.setHeader("Content-type", "application/json; charset=UTF-8");
      request.addHeader("Authorization", "Bearer " + this.accessToken);
      request.addHeader("Accept", "application/vnd.github+json");

      try (var response = client.execute(request)) {

        String jsonResponse = EntityUtils.toString(response.getEntity());
        Optional<JsonArray> commits = parseCommits(jsonResponse);
        commits.ifPresent(
            jsonElements -> this.lastCommitData = jsonElements.get(0).getAsJsonObject()
                .getAsJsonObject("commit")
                .getAsJsonObject("author").get("date").getAsString());
        return commits;
      }
    } catch (IOException e) {
      System.err.println("ClientIOErr");
      return Optional.empty();
    }
  }

  private static Optional<JsonArray> parseCommits(String response) {
    JsonElement jsonElement = JsonParser.parseString(response);

    if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message")) {
      System.err.println(
          "Error from GitHub API: " + jsonElement.getAsJsonObject().get("message")
              .getAsString());
      return Optional.empty();
    }
    JsonArray commits = JsonParser.parseString(response).getAsJsonArray();
    return Optional.of(commits);
  }

  /**
   * Fetches a list of issues from a specific GitHub repository.
   *
   * @param authorName The username of owner of the GitHub repository.
   * @param repoName   The name of the repository from which to fetch issues.
   * @return A {@link JsonArray} containing issue data. Each element in the array is a
   * {@link JsonObject} representing a single issue.
   */
  public Optional<JsonArray> fetchIssues(String authorName, String repoName) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpGet request = new HttpGet(
          "https://api.github.com/repos/" + authorName + "/" + repoName + String.format(
              "/issues?since=%s", this.lastIssueData));
      request.setHeader("Content-type", "application/json; charset=UTF-8");
      request.addHeader("Authorization", "Bearer " + this.accessToken);
      request.addHeader("Accept", "application/vnd.github+json");

      try (var response = client.execute(request)) {
        String jsonResponse = EntityUtils.toString(response.getEntity());
        Optional<JsonArray> issues = parseIssues(jsonResponse);
        issues.ifPresent(jsonElements -> this.lastIssueData = jsonElements.get(0).getAsJsonObject()
            .get("updated_at").getAsString());
        return issues;
      }
    } catch (IOException e) {
      System.err.println("ClientIOErr");
      return Optional.empty();
    }
  }

  private static Optional<JsonArray> parseIssues(String response) {
    JsonElement jsonElement = JsonParser.parseString(response);

    if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("message")) {
      System.err.println(
          "Error from GitHub API: " + jsonElement.getAsJsonObject().get("message")
              .getAsString());
      return Optional.empty();
    }
    JsonArray issues = JsonParser.parseString(response).getAsJsonArray();
    return Optional.of(issues);
  }

}
