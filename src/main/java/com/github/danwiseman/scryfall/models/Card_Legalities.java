package com.github.danwiseman.scryfall.models;

import static com.github.danwiseman.scryfall.schemas.ScryfallSchemas.*;

import org.json.JSONObject;

public class Card_Legalities {

  private String standard;
  private String future;
  private String historic;
  private String gladiator;
  private String pioneer;
  private String modern;
  private String legacy;
  private String pauper;
  private String vintage;
  private String penny;
  private String commander;
  private String brawl;
  private String historicalbrawl;
  private String alchemy;
  private String paupercommander;
  private String duel;
  private String oldschool;
  private String premodern;

  public Card_Legalities() {}

  public Card_Legalities(
    String standard,
    String future,
    String historic,
    String gladiator,
    String pioneer,
    String modern,
    String legacy,
    String pauper,
    String vintage,
    String penny,
    String commander,
    String brawl,
    String historicalbrawl,
    String alchemy,
    String paupercommander,
    String duel,
    String oldschool,
    String premodern
  ) {
    this.standard = standard;
    this.future = future;
    this.historic = historic;
    this.gladiator = gladiator;
    this.pioneer = pioneer;
    this.modern = modern;
    this.legacy = legacy;
    this.pauper = pauper;
    this.vintage = vintage;
    this.penny = penny;
    this.commander = commander;
    this.brawl = brawl;
    this.historicalbrawl = historicalbrawl;
    this.alchemy = alchemy;
    this.paupercommander = paupercommander;
    this.duel = duel;
    this.oldschool = oldschool;
    this.premodern = premodern;
  }

  public String getStandard() {
    return standard;
  }

  public void setStandard(String standard) {
    this.standard = standard;
  }

  public String getFuture() {
    return future;
  }

  public void setFuture(String future) {
    this.future = future;
  }

  public String getHistoric() {
    return historic;
  }

  public void setHistoric(String historic) {
    this.historic = historic;
  }

  public String getGladiator() {
    return gladiator;
  }

  public void setGladiator(String gladiator) {
    this.gladiator = gladiator;
  }

  public String getPioneer() {
    return pioneer;
  }

  public void setPioneer(String pioneer) {
    this.pioneer = pioneer;
  }

  public String getModern() {
    return modern;
  }

  public void setModern(String modern) {
    this.modern = modern;
  }

  public String getLegacy() {
    return legacy;
  }

  public void setLegacy(String legacy) {
    this.legacy = legacy;
  }

  public String getPauper() {
    return pauper;
  }

  public void setPauper(String pauper) {
    this.pauper = pauper;
  }

  public String getVintage() {
    return vintage;
  }

  public void setVintage(String vintage) {
    this.vintage = vintage;
  }

  public String getPenny() {
    return penny;
  }

  public void setPenny(String penny) {
    this.penny = penny;
  }

  public String getCommander() {
    return commander;
  }

  public void setCommander(String commander) {
    this.commander = commander;
  }

  public String getBrawl() {
    return brawl;
  }

  public void setBrawl(String brawl) {
    this.brawl = brawl;
  }

  public String getHistoricalbrawl() {
    return historicalbrawl;
  }

  public void setHistoricalbrawl(String historicalbrawl) {
    this.historicalbrawl = historicalbrawl;
  }

  public String getAlchemy() {
    return alchemy;
  }

  public void setAlchemy(String alchemy) {
    this.alchemy = alchemy;
  }

  public String getPaupercommander() {
    return paupercommander;
  }

  public void setPaupercommander(String paupercommander) {
    this.paupercommander = paupercommander;
  }

  public String getDuel() {
    return duel;
  }

  public void setDuel(String duel) {
    this.duel = duel;
  }

  public String getOldschool() {
    return oldschool;
  }

  public void setOldschool(String oldschool) {
    this.oldschool = oldschool;
  }

  public String getPremodern() {
    return premodern;
  }

  public void setPremodern(String premodern) {
    this.premodern = premodern;
  }

  public static Card_Legalities fromJson(JSONObject jsonObject) {
    Card_Legalities legalities = new Card_Legalities();

    legalities.setStandard(jsonObject.optString(CARD_LEGALITIES_STANDARD));
    legalities.setFuture(jsonObject.optString(CARD_LEGALITIES_FUTURE));
    legalities.setHistoric(jsonObject.optString(CARD_LEGALITIES_HISTORIC));
    legalities.setGladiator(jsonObject.optString(CARD_LEGALITIES_GLADIATOR));
    legalities.setPioneer(jsonObject.optString(CARD_LEGALITIES_PIONEER));
    legalities.setModern(jsonObject.optString(CARD_LEGALITIES_MODERN));
    legalities.setLegacy(jsonObject.optString(CARD_LEGALITIES_LEGACY));
    legalities.setPauper(jsonObject.optString(CARD_LEGALITIES_PAUPER));
    legalities.setVintage(jsonObject.optString(CARD_LEGALITIES_VINTAGE));
    legalities.setPenny(jsonObject.optString(CARD_LEGALITIES_PENNY));
    legalities.setCommander(jsonObject.optString(CARD_LEGALITIES_COMMANDER));
    legalities.setBrawl(jsonObject.optString(CARD_LEGALITIES_BRAWL));
    legalities.setHistoricalbrawl(
      jsonObject.optString(CARD_LEGALITIES_HISTORICALBRAWL)
    );
    legalities.setAlchemy(jsonObject.optString(CARD_LEGALITIES_ALCHEMY));
    legalities.setPaupercommander(
      jsonObject.optString(CARD_LEGALITIES_PAUPERCOMMANDER)
    );
    legalities.setDuel(jsonObject.optString(CARD_LEGALITIES_DUEL));
    legalities.setOldschool(jsonObject.optString(CARD_LEGALITIES_OLDSCHOOL));
    legalities.setPremodern(jsonObject.optString(CARD_LEGALITIES_PREMODERN));

    return legalities;
  }
}
