package com.github.danwiseman.scryfall;

import static com.github.danwiseman.scryfall.schemas.ScryfallSchemas.NEXT_PAGE_FIELD;

import com.mashape.unirest.http.Headers;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScryfallApiClient {

  private static final Logger log = LoggerFactory.getLogger(
    ScryfallApiClient.class
  );

  private String next_page_from_response = "";

  public ScryfallApiClient() {}

  public JSONArray getScryfallData(String page) throws InterruptedException {
    HttpResponse<JsonNode> jsonResponse;
    rateLimitSleep();
    try {
      jsonResponse = getData(page);
      Headers headers = jsonResponse.getHeaders();

      switch (jsonResponse.getStatus()) {
        case 200:
          return processPage(jsonResponse.getBody().getObject());
        case 400:
          log.error("400 error. Bad Request");
          return new JSONArray();
        case 401:
          log.error("401 error. Unauthorized");
          return new JSONArray();
        case 404:
          log.error("404 error. Not found");
          return new JSONArray();
        case 429:
          log.warn("429 error. Rate Limit Hit, sleeping and trying again.");
          this.sleep();
          return getScryfallData(page);
        default:
          log.error(
            "Unhandled error {}, sleeping and trying again.",
            jsonResponse.getStatus()
          );
          this.sleep();
          return getScryfallData(page);
      }
    } catch (UnirestException e) {
      e.printStackTrace();
      return new JSONArray();
    }
  }

  private void sleep() throws InterruptedException {
    long longSleep = 30l;
    log.debug(String.format("Sleeping for %s seconds", longSleep));
    TimeUnit.SECONDS.sleep(longSleep);
  }

  private void rateLimitSleep() throws InterruptedException {
    long rateLimit = 125l;
    log.debug(String.format("Forced Rate Limit Sleep for %s ms", rateLimit));
    TimeUnit.MILLISECONDS.sleep(rateLimit);
  }

  public boolean hasMoreData() {
    return (next_page_from_response != "");
  }

  public String getNextPage() {
    return next_page_from_response;
  }

  private JSONArray processPage(JSONObject object) {
    if (object.has(NEXT_PAGE_FIELD)) {
      this.next_page_from_response = object.getString(NEXT_PAGE_FIELD);
    } else {
      this.next_page_from_response = "";
    }
    if (object.has("data")) {
      return object.getJSONArray("data");
    } else {
      return new JSONArray();
    }
  }

  private HttpResponse<JsonNode> getData(String page) throws UnirestException {
    GetRequest unirest = Unirest.get(page);
    log.info(String.format("getting %s", unirest.getUrl()));
    return unirest.asJson();
  }
}
