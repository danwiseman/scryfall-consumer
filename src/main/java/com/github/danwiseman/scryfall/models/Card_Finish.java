package com.github.danwiseman.scryfall.models;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Card_Finish {

  private String finish;

  public Card_Finish() {}

  public Card_Finish(String finish) {
    this.finish = finish;
  }

  public static List<Card_Finish> arrayFromJson(JSONArray optJSONArray) {
    List<Card_Finish> card_finishes = new ArrayList<>();
    if (optJSONArray != null) {
      for (Object jsonObject : optJSONArray) {
        if (jsonObject instanceof JSONObject) {
          Card_Finish card_finish = Card_Finish.fromJson(
            (JSONObject) jsonObject
          );
          card_finishes.add(card_finish);
        }
      }
    }
    return card_finishes;
  }

  private static Card_Finish fromJson(JSONObject jsonObject) {
    Card_Finish card_finish = new Card_Finish();

    card_finish.setFinish(jsonObject.toString());
    return card_finish;
  }

  public String getFinish() {
    return finish;
  }

  public void setFinish(String finish) {
    this.finish = finish;
  }
}
