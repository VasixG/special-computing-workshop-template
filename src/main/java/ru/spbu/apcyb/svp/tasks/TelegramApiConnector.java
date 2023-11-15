package ru.spbu.apcyb.svp.tasks;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The {@code TelegramApiConnector} class is a connector to the Telegram API using the access token
 * and user ID obtained from environment variables.
 *
 * <p>It uses the dotenv library to load environment variables from a .env file</p>
 *
 * <p>The class assumes that a .env file is present with TELEGRAM_TOKEN and USER_ID defined.
 * TELEGRAM_TOKEN is the access token for the Telegram bot, and USER_ID is the identifier of the
 * user. </p>
 */
public class TelegramApiConnector {

  private final String accessToken;

  private final String user_id;

  /**
   * Constructs a new {@code TelegramApiConnector} instance.
   */
  public TelegramApiConnector() {
    Dotenv dotenv = Dotenv.load();
    this.accessToken = dotenv.get("TELEGRAM_TOKEN");
    this.user_id = dotenv.get("USER_ID");
  }

  /**
   * Sends a message to a specified Telegram user or chat using the Telegram Bot API.
   *
   * @param message The text message to be sent via the Telegram bot.
   */
  public void sendMessage(String message) {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      String url = "https://api.telegram.org/bot" + this.accessToken + "/sendMessage";
      HttpPost httpPost = new HttpPost(url);

      String json = "{\"chat_id\":\"" + this.user_id + "\",\"text\":\"" + message + "\"}";
      StringEntity entity = new StringEntity(json,
          ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8));
      httpPost.setEntity(entity);
      httpPost.setHeader("Content-type", "application/json; charset=UTF-8");

      try (var response = client.execute(httpPost)) {
        String responseString = EntityUtils.toString(response.getEntity());
        JsonElement jsonElement = JsonParser.parseString(responseString);
        if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has("error_code")) {
          System.err.println(
              "Error from Telegram API: " + jsonElement.getAsJsonObject().get("description")
                  .getAsString());
        }
      }
    } catch (IOException e) {
      System.err.println("ClientIOErr");
    }
  }

}
