package com.github.danwiseman.scryfall.models;

import static com.github.danwiseman.kafka.streams.scryfall.schemas.ScryfallSchemas.*;

import org.json.JSONObject;

public class Card_Image_Uris {

  private String png;
  private String border_crop;
  private String art_crop;
  private String large;
  private String normal;
  private String small;

  public Card_Image_Uris() {}

  public Card_Image_Uris(
    String png,
    String border_crop,
    String art_crop,
    String large,
    String normal,
    String small
  ) {
    this.png = png;
    this.border_crop = border_crop;
    this.art_crop = art_crop;
    this.large = large;
    this.normal = normal;
    this.small = small;
  }

  public static Card_Image_Uris fromJson(JSONObject jsonObject) {
    Card_Image_Uris card_image_uris = new Card_Image_Uris();
    if (jsonObject != null) {
      card_image_uris.setPng(jsonObject.optString(CARD_IMAGE_URI_PNG));
      card_image_uris.setBorder_crop(
        jsonObject.optString(CARD_IMAGE_URI_BORDER_CROP)
      );
      card_image_uris.setArt_crop(
        jsonObject.optString(CARD_IMAGE_URI_ART_CROP)
      );
      card_image_uris.setLarge(jsonObject.optString(CARD_IMAGE_URI_LARGE));
      card_image_uris.setNormal(jsonObject.optString(CARD_IMAGE_URI_NORMAL));
      card_image_uris.setSmall(jsonObject.optString(CARD_IMAGE_URI_SMALL));
    }
    return card_image_uris;
  }

  public String getPng() {
    return png;
  }

  public void setPng(String png) {
    this.png = png;
  }

  public String getBorder_crop() {
    return border_crop;
  }

  public void setBorder_crop(String border_crop) {
    this.border_crop = border_crop;
  }

  public String getArt_crop() {
    return art_crop;
  }

  public void setArt_crop(String art_crop) {
    this.art_crop = art_crop;
  }

  public String getLarge() {
    return large;
  }

  public void setLarge(String large) {
    this.large = large;
  }

  public String getNormal() {
    return normal;
  }

  public void setNormal(String normal) {
    this.normal = normal;
  }

  public String getSmall() {
    return small;
  }

  public void setSmall(String small) {
    this.small = small;
  }
}
