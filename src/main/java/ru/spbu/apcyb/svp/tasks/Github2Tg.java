package ru.spbu.apcyb.svp.tasks;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@code Github2Tg} class makes communication between GitHub and Telegram. It allows for
 * fetching data (like notifications, commits, issues) from a specified GitHub repository and then
 * sending this data as messages to a Telegram user.
 *
 * <p>This class combines the functionalities of {@link GithubApiConnector} and
 * {@link TelegramApiConnector} to periodically check for updates in a GitHub repository and forward
 * these updates via Telegram. </p>
 */
public class Github2Tg {

  private final GithubApiConnector gh_conn;
  private final TelegramApiConnector tg_conn;

  private final String userName;

  private final String repoName;

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  /**
   * Constructs a {@code Github2Tg} instance for a specific GitHub repository and user.
   *
   * @param userName The GitHub username of the repository owner.
   * @param repoName The name of the GitHub repository to be monitored.
   */
  public Github2Tg(String userName, String repoName) {
    this.gh_conn = new GithubApiConnector();
    this.tg_conn = new TelegramApiConnector();
    this.repoName = repoName;
    this.userName = userName;
  }

  /**
   * Starts a periodic polling task to check for updates in the GitHub repository and sends
   * notifications to Telegram.
   *
   *
   * <p>The method leverages {@link ScheduledExecutorService} to manage the periodic execution
   * of the polling task, ensuring that it runs repeatedly at the specified interval.</p>
   *
   * @param intervalSeconds The interval, in seconds, at which the GitHub repository is checked for
   *                        updates.
   */

  public void startPolling(int intervalSeconds) {
    scheduler.scheduleAtFixedRate(() -> {
      try {
        Optional<JsonArray> notifications = this.gh_conn.fetchNotifications();
        notifications.ifPresent(this::processNotifications);

        Optional<JsonArray> commits = this.gh_conn.fetchCommits(this.userName, this.repoName);
        commits.ifPresent(this::processCommits);

        Optional<JsonArray> issues = this.gh_conn.fetchIssues(this.userName, this.repoName);
        issues.ifPresent(this::processIssues);
      } catch (Exception e) {
        System.err.println("Err");
      }
    }, 0, intervalSeconds, TimeUnit.SECONDS);
  }

  private void processNotifications(JsonArray notifications) {
    for (JsonElement notificationElement : notifications) {
      JsonObject notification = notificationElement.getAsJsonObject();
      String reason = notification.get("reason").getAsString();

      switch (reason) {
        case "assign":
          this.tg_conn.sendMessage(processAssignNotification(notification));
          break;
        case "review_requested", "author", "mention":
          this.tg_conn.sendMessage(processReviewRequestedAuthorMentionNotification(notification));
          break;
        default:
          System.out.println("Notification with unhandled reason: " + reason);
      }
    }
  }

  private static String processAssignNotification(JsonObject notification) {
    String reason = notification.get("reason").getAsString();
    String updatedAt = notification.get("updated_at").getAsString();

    ZonedDateTime utcDateTime = ZonedDateTime.parse(updatedAt);
    ZonedDateTime moscowDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Moscow"));
    String moscowTime = moscowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    JsonObject subject = notification.getAsJsonObject("subject");
    String subjectUrl = subject.get("url").getAsString()
        .replace("api.github.com/repos", "github.com").replace("/commits/", "/commit/");
    String subjectType = subject.get("type").getAsString();
    JsonObject repository = notification.getAsJsonObject("repository");
    String repositoryName = repository.get("name").getAsString();

    return String.format(
        "NOTIFICATION%nReason: %s%n Date: %s%n URL: %s%n Type: %s%n Repository: %s", reason,
        moscowTime, subjectUrl, subjectType, repositoryName);
  }

  private static String processReviewRequestedAuthorMentionNotification(JsonObject notification) {
    String reason = notification.get("reason").getAsString();
    String updatedAt = notification.get("updated_at").getAsString();

    ZonedDateTime utcDateTime = ZonedDateTime.parse(updatedAt);
    ZonedDateTime moscowDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Moscow"));
    String moscowTime = moscowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    JsonObject subject = notification.getAsJsonObject("subject");
    String subjectTitle = subject.get("title").getAsString();
    String subjectUrl = subject.get("url").getAsString()
        .replace("api.github.com/repos", "github.com").replace("/pulls/", "/pull/")
        .replace("/commits/", "/commit/");
    String subjectType = subject.get("type").getAsString();
    JsonObject repository = notification.getAsJsonObject("repository");
    String repositoryName = repository.get("name").getAsString();

    return String.format(
        "NOTIFICATION%nReason: %s%n Date: %s%n URL: %s%n Type: %s%n Title: %s%n Repository: %s",
        reason, moscowTime, subjectUrl, subjectType, subjectTitle, repositoryName);
  }

  private void processCommits(JsonArray commits) {
    for (JsonElement commitElement : commits) {
      JsonObject commit = commitElement.getAsJsonObject();
      this.tg_conn.sendMessage(processCommit(commit));
    }
  }

  private static String processCommit(JsonObject commit) {

    JsonObject commitObj = commit.getAsJsonObject("commit");
    JsonObject committerObj = commitObj.getAsJsonObject("committer");

    String date = committerObj.get("date").getAsString();

    ZonedDateTime utcDateTime = ZonedDateTime.parse(date);
    ZonedDateTime moscowDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Moscow"));
    String moscowTime = moscowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    String message = commitObj.get("message").getAsString();

    String htmlUrl = commit.get("html_url").getAsString();

    String committer = committerObj.get("name").getAsString();

    return String.format("COMMIT in %s%n Committer: %s%n Message: '%s'%n Date: %s%n URL: %s",
        htmlUrl.substring(0, htmlUrl.indexOf("/commit")), committer, message, moscowTime, htmlUrl);
  }

  private void processIssues(JsonArray issues) {
    for (JsonElement issueElement : issues) {
      JsonObject commit = issueElement.getAsJsonObject();
      this.tg_conn.sendMessage(processIssue(commit));
    }
  }

  private static String processIssue(JsonObject issue) {

    String date = issue.get("updated_at").getAsString();

    ZonedDateTime utcDateTime = ZonedDateTime.parse(date);
    ZonedDateTime moscowDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("Europe/Moscow"));
    String moscowTime = moscowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    String title = issue.get("title").getAsString();

    String htmlUrl = issue.get("html_url").getAsString();

    JsonArray assignees = issue.getAsJsonArray("assignees");

    List<String> logins = new ArrayList<>();
    for (JsonElement assigneeElement : assignees) {
      JsonObject assigneeObject = assigneeElement.getAsJsonObject();
      String login = assigneeObject.get("login").getAsString();
      logins.add(login);
    }
    String assigneesStr = String.join(", ", logins);

    return String.format("ISSUE in %s%n Assignees: %s%n Title: '%s'%n Date: %s%n URL: %s",
        htmlUrl.substring(0, htmlUrl.indexOf("/issues")), assigneesStr, title, moscowTime, htmlUrl);
  }


  public static void main(String[] args) {

    Github2Tg poller = new Github2Tg("VasixG", "form");
    poller.startPolling(60); // Poll every 60 seconds
  }
}
