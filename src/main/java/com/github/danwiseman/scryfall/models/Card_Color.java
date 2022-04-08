package com.github.danwiseman.scryfall.models;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Card_Color {

  private String color;

  public Card_Color() {}

  public Card_Color(String color) {
    this.color = color;
  }

  public static List<Card_Color> arrayFromJson(JSONArray optJSONArray) {
    List<Card_Color> card_colors = new ArrayList<>();
    if (optJSONArray != null) {
      for (Object jsonObject : optJSONArray) {
        if (jsonObject instanceof JSONObject) {
          Card_Color card_color = Card_Color.fromJson((JSONObject) jsonObject);
          card_colors.add(card_color);
        }
      }
    }
    return card_colors;
  }

  private static Card_Color fromJson(JSONObject jsonObject) {
    Card_Color card_color = new Card_Color();

    card_color.setColor(jsonObject.toString());
    return card_color;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}
