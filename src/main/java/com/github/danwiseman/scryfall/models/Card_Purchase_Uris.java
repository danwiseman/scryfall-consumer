package com.github.danwiseman.scryfall.models;

import static com.github.danwiseman.scryfall.schemas.ScryfallSchemas.*;

import org.json.JSONObject;

public class Card_Purchase_Uris {

  private String tcgplayer;
  private String cardmarket;
  private String cardhoarder;

  public Card_Purchase_Uris() {}

  public Card_Purchase_Uris(
    String tcgplayer,
    String cardmarket,
    String cardhoarder
  ) {
    this.tcgplayer = tcgplayer;
    this.cardmarket = cardmarket;
    this.cardhoarder = cardhoarder;
  }

  public static Card_Purchase_Uris fromJson(JSONObject jsonObject) {
    Card_Purchase_Uris card_purchase_uris = new Card_Purchase_Uris();
    if (jsonObject != null) {
      card_purchase_uris.setCardhoarder(
        jsonObject.optString(CARD_PURCHASE_URIS_CARDHOARDER)
      );
      card_purchase_uris.setCardmarket(
        jsonObject.optString(CARD_PURCHASE_URIS_CARDMARKET)
      );
      card_purchase_uris.setTcgplayer(
        jsonObject.optString(CARD_PURCHASE_URIS_TCGPLAYER)
      );
    } else {
      card_purchase_uris.setCardhoarder("");
      card_purchase_uris.setCardmarket("");
      card_purchase_uris.setTcgplayer("");
    }

    return card_purchase_uris;
  }

  public String getTcgplayer() {
    return tcgplayer;
  }

  public void setTcgplayer(String tcgplayer) {
    this.tcgplayer = tcgplayer;
  }

  public String getCardmarket() {
    return cardmarket;
  }

  public void setCardmarket(String cardmarket) {
    this.cardmarket = cardmarket;
  }

  public String getCardhoarder() {
    return cardhoarder;
  }

  public void setCardhoarder(String cardhoarder) {
    this.cardhoarder = cardhoarder;
  }
}
