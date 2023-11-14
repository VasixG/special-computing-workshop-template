package ru.spbu.apcyb.svp.tasks;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TelegramApiConnectorTest {
    private TelegramApiConnector tg_conn;

    @Before
    public void setUp() {
        tg_conn = new TelegramApiConnector();
    }

    @Test
    public void testSendMessageOK() {
        tg_conn.sendMessage("Hello world");
    }

}
