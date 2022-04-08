package com.github.danwiseman.scryfall.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OracleCard {

  private String oracle_id;
  private String oracle_name;
  private List<String> colors;
  private List<String> color_identity;
  private List<String> color_indicator;
  private String mana_cost;
  private String oracle_text;
  private String power;
  private String toughness;
  private String layout;
  private Double cheapest_price;
  private Double highest_price;
  private String cmc;
  private String type_line;
  private Card_Legalities legalities;
  private List<String> card_prints;
  private Card_Image_Uris card_images;

  public OracleCard() {}

  public OracleCard(
    String oracle_id,
    String oracle_name,
    List<String> colors,
    List<String> color_identity,
    List<String> color_indicator,
    String mana_cost,
    String oracle_text,
    String power,
    String toughness,
    String layout,
    Double cheapest_price,
    Double highest_price,
    String cmc,
    String type_line,
    Card_Legalities legalities,
    List<String> card_prints,
    Card_Image_Uris card_images
  ) {
    this.oracle_id = oracle_id;
    this.oracle_name = oracle_name;
    this.colors = colors;
    this.color_identity = color_identity;
    this.color_indicator = color_indicator;
    this.mana_cost = mana_cost;
    this.oracle_text = oracle_text;
    this.power = power;
    this.toughness = toughness;
    this.layout = layout;
    this.cheapest_price = cheapest_price;
    this.highest_price = highest_price;
    this.cmc = cmc;
    this.type_line = type_line;
    this.legalities = legalities;
    this.card_prints = card_prints;
    this.card_images = card_images;
  }

  public static OracleCard fromScryfallCard(Card card) {
    OracleCard oracleCard = new OracleCard();
    List<String> cardprints = new ArrayList<>();

    oracleCard.setOracle_id(card.getOracle_id());
    oracleCard.setOracle_name(card.getName());
    oracleCard.setColors(card.getColors());
    oracleCard.setColor_identity(card.getColor_identity());
    oracleCard.setColor_indicator(card.getColor_indicator());

    oracleCard.setLayout(card.getLayout());

    // Streams will take care of setting these prices
    if (card.getPrices().getUsd().isEmpty()) {
      // set a super high price for the lowest price, and a super low price for the highest,
      // this way, when compared, the values will be replaced if one is found...
      oracleCard.setCheapest_price(99999.9);
      oracleCard.setHighest_price(00000.0);
    } else {
      oracleCard.setCheapest_price(
        Double.parseDouble(card.getPrices().getUsd())
      );
      oracleCard.setHighest_price(
        Double.parseDouble(card.getPrices().getUsd())
      );
    }

    oracleCard.setType_line(card.getType_line());
    oracleCard.setLegalities(card.getLegalities());
    cardprints.add(card.getId());
    oracleCard.setCard_prints(cardprints);

    if (card.getCard_faces().size() >= 1) {
      // Pull from Card Face 0
      oracleCard.setPower(card.getCard_faces().get(0).getPower());
      oracleCard.setToughness(card.getCard_faces().get(0).getToughness());
      oracleCard.setCmc(card.getCard_faces().get(0).getCmc());
      oracleCard.setMana_cost(card.getCard_faces().get(0).getPower());
      oracleCard.setOracle_text(card.getCard_faces().get(0).getPower());
      oracleCard.setCard_images(card.getCard_faces().get(0).getImage_uris());
    } else {
      oracleCard.setPower(card.getPower());
      oracleCard.setToughness(card.getToughness());
      oracleCard.setCmc(card.getCmc());
      oracleCard.setMana_cost(card.getPower());
      oracleCard.setOracle_text(card.getPower());
      oracleCard.setCard_images(card.getImage_uris());
    }

    return oracleCard;
  }

  public void addCardPrints(String card_uuid) {
    if (!this.card_prints.contains(card_uuid)) {
      this.card_prints.add(card_uuid);
    }
  }

  public void addCardPrintsFromList(List<String> card_prints) {
    for (String card_print : card_prints) {
      if (!this.card_prints.contains(card_print)) {
        this.card_prints.add(card_print);
      }
    }
  }

  public String getOracle_id() {
    return oracle_id;
  }

  public void setOracle_id(String oracle_id) {
    this.oracle_id = oracle_id;
  }

  public String getOracle_name() {
    return oracle_name;
  }

  public void setOracle_name(String oracle_name) {
    this.oracle_name = oracle_name;
  }

  public List<String> getColors() {
    return colors;
  }

  public void setColors(List<String> colors) {
    this.colors = colors;
  }

  public List<String> getColor_identity() {
    return color_identity;
  }

  public void setColor_identity(List<String> color_identity) {
    this.color_identity = color_identity;
  }

  public List<String> getColor_indicator() {
    return color_indicator;
  }

  public void setColor_indicator(List<String> color_indicator) {
    this.color_indicator = color_indicator;
  }

  public String getMana_cost() {
    return mana_cost;
  }

  public void setMana_cost(String mana_cost) {
    this.mana_cost = mana_cost;
  }

  public String getOracle_text() {
    return oracle_text;
  }

  public void setOracle_text(String oracle_text) {
    this.oracle_text = oracle_text;
  }

  public String getPower() {
    return power;
  }

  public void setPower(String power) {
    this.power = power;
  }

  public String getToughness() {
    return toughness;
  }

  public void setToughness(String toughness) {
    this.toughness = toughness;
  }

  public String getLayout() {
    return layout;
  }

  public void setLayout(String layout) {
    this.layout = layout;
  }

  public Double getCheapest_price() {
    return cheapest_price;
  }

  public void setCheapest_price(Double cheapest_price) {
    this.cheapest_price = cheapest_price;
  }

  public Double getHighest_price() {
    return highest_price;
  }

  public void setHighest_price(Double highest_price) {
    this.highest_price = highest_price;
  }

  public String getCmc() {
    return cmc;
  }

  public void setCmc(String cmc) {
    this.cmc = cmc;
  }

  public String getType_line() {
    return type_line;
  }

  public void setType_line(String type_line) {
    this.type_line = type_line;
  }

  public Card_Legalities getLegalities() {
    return legalities;
  }

  public void setLegalities(Card_Legalities legalities) {
    this.legalities = legalities;
  }

  public List<String> getCard_prints() {
    return card_prints;
  }

  public void setCard_prints(List<String> card_prints) {
    this.card_prints = card_prints;
  }

  public Card_Image_Uris getCard_images() {
    return card_images;
  }

  public void setCard_images(Card_Image_Uris card_images) {
    this.card_images = card_images;
  }
}
