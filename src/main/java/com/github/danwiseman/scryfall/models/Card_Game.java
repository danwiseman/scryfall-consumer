package com.github.danwiseman.scryfall.models;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Card_Game {

  private String game;

  public Card_Game() {}

  public Card_Game(String game) {
    this.game = game;
  }

  public static List<Card_Game> arrayFromJson(JSONArray optJSONArray) {
    List<Card_Game> card_games = new ArrayList<>();
    if (optJSONArray != null) {
      for (Object jsonObject : optJSONArray) {
        if (jsonObject instanceof JSONObject) {
          Card_Game card_game = Card_Game.fromJson((JSONObject) jsonObject);
          card_games.add(card_game);
        }
      }
    }
    return card_games;
  }

  private static Card_Game fromJson(JSONObject jsonObject) {
    Card_Game card_game = new Card_Game();

    card_game.setGame(jsonObject.toString());
    return card_game;
  }

  public String getGame() {
    return game;
  }

  public void setGame(String game) {
    this.game = game;
  }
}
