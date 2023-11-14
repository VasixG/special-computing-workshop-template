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
        assertEquals(8, gh_conn.fetchNotifications().size());
    }

    @Test
    public void testfetchNotifications() {
        assertEquals("assign", gh_conn.fetchNotifications().get(0).getAsJsonObject().get("reason").getAsString());
    }

    @Test
    public void testfetchCommits() {
        assertEquals("https://github.com/VasixG/form/commit/11b8d987896abdac8de1264d60ea4e5171d216c8", gh_conn.fetchCommits("VasixG", "form").get(0).getAsJsonObject().get("html_url").getAsString());
    }

    @Test
    public void testfetchIssues() {
        assertEquals("https://github.com/VasixG/form/issues/1", gh_conn.fetchIssues("VasixG", "form").get(0).getAsJsonObject().get("html_url").getAsString());
    }

}
