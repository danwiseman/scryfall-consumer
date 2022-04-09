package com.github.danwiseman.scryfall.models;

import static com.github.danwiseman.scryfall.schemas.ScryfallSchemas.*;

import org.json.JSONObject;

public class Card_Prices {

  private String usd;
  private String usd_foil;
  private String usd_etched;
  private String eur;
  private String eur_foil;
  private String tix;

  public Card_Prices() {}

  public Card_Prices(
    String usd,
    String usd_foil,
    String usd_etched,
    String eur,
    String eur_foil,
    String tix
  ) {
    this.usd = usd;
    this.usd_foil = usd_foil;
    this.usd_etched = usd_etched;
    this.eur = eur;
    this.eur_foil = eur_foil;
    this.tix = tix;
  }

  public static Card_Prices fromJson(JSONObject jsonObject) {
    Card_Prices card_prices = new Card_Prices();

    card_prices.setUsd(jsonObject.optString(CARD_PRICES_USD));
    card_prices.setUsd_foil(jsonObject.optString(CARD_PRICES_USD_FOIL));
    card_prices.setUsd_etched(jsonObject.optString(CARD_PRICES_USD_ETCHED));
    card_prices.setEur(jsonObject.optString(CARD_PRICES_EUR));
    card_prices.setEur(jsonObject.optString(CARD_PRICES_EUR_FOIL));
    card_prices.setEur_foil(jsonObject.optString(CARD_PRICES_EUR_FOIL));
    card_prices.setTix(jsonObject.optString(CARD_PRICES_TIX));

    return card_prices;
  }

  public String getUsd() {
    return usd;
  }

  public void setUsd(String usd) {
    this.usd = usd;
  }

  public String getUsd_foil() {
    return usd_foil;
  }

  public void setUsd_foil(String usd_foil) {
    this.usd_foil = usd_foil;
  }

  public String getUsd_etched() {
    return usd_etched;
  }

  public void setUsd_etched(String usd_etched) {
    this.usd_etched = usd_etched;
  }

  public String getEur() {
    return eur;
  }

  public void setEur(String eur) {
    this.eur = eur;
  }

  public String getEur_foil() {
    return eur_foil;
  }

  public void setEur_foil(String eur_foil) {
    this.eur_foil = eur_foil;
  }

  public String getTix() {
    return tix;
  }

  public void setTix(String tix) {
    this.tix = tix;
  }
}
