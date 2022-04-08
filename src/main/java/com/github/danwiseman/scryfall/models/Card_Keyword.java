package com.github.danwiseman.scryfall.models;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Card_Keyword {

  private String keyword;

  public Card_Keyword() {}

  public Card_Keyword(String keyword) {
    this.keyword = keyword;
  }

  public static List<Card_Keyword> arrayFromJson(JSONArray optJSONArray) {
    List<Card_Keyword> card_keywords = new ArrayList<>();
    if (optJSONArray != null) {
      for (Object jsonObject : optJSONArray) {
        if (jsonObject instanceof JSONObject) {
          Card_Keyword card_keyword = Card_Keyword.fromJson(
            (JSONObject) jsonObject
          );
          card_keywords.add(card_keyword);
        }
      }
    }
    return card_keywords;
  }

  private static Card_Keyword fromJson(JSONObject jsonObject) {
    Card_Keyword card_keyword = new Card_Keyword();

    card_keyword.setKeyword(jsonObject.toString());
    return card_keyword;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
}
