package ru.spbu.apcyb.svp.tasks;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GithubApiConnectorTest {

  private GithubApiConnector gh_conn;

  @Before
  public void setUp() {
    gh_conn = new GithubApiConnector();
  }

  @Test
  public void testfetchNotificationsSize() {
    assertEquals(9, gh_conn.fetchNotifications().get().size());
  }

  @Test
  public void testfetchNotifications() {
    assertEquals("author",
        gh_conn.fetchNotifications().get().get(0).getAsJsonObject().get("reason").getAsString());
  }

  @Test
  public void testfetchCommits() {
    assertEquals("https://github.com/VasixG/form/commit/a655713f62a5a50e1879b95dbaf2b7d5032df15e",
        gh_conn.fetchCommits("VasixG", "form").get().get(0).getAsJsonObject().get("html_url")
            .getAsString());
  }

  @Test
  public void testfetchIssues() {
    assertEquals("https://github.com/VasixG/form/issues/1",
        gh_conn.fetchIssues("VasixG", "form").get().get(0).getAsJsonObject().get("html_url")
            .getAsString());
  }

}
