package com.github.danwiseman.scryfall.models;

import static com.github.danwiseman.kafka.streams.scryfall.schemas.ScryfallSchemas.*;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Card_All_Parts {

  private String id;
  private String object;
  private String component;
  private String name;
  private String type_line;
  private String uri;

  public Card_All_Parts() {}

  public Card_All_Parts(
    String id,
    String object,
    String component,
    String name,
    String type_line,
    String uri
  ) {
    this.id = id;
    this.object = object;
    this.component = component;
    this.name = name;
    this.type_line = type_line;
    this.uri = uri;
  }

  public static List<Card_All_Parts> arrayFromJson(JSONArray optJSONArray) {
    List<Card_All_Parts> card_all_parts = new ArrayList<>();
    if (optJSONArray != null) {
      for (Object jsonObject : optJSONArray) {
        if (jsonObject instanceof JSONObject) {
          Card_All_Parts card_all_part = Card_All_Parts.fromJson(
            (JSONObject) jsonObject
          );
          card_all_parts.add(card_all_part);
        }
      }
    }
    return card_all_parts;
  }

  private static Card_All_Parts fromJson(JSONObject jsonObject) {
    Card_All_Parts card_all_parts = new Card_All_Parts();

    card_all_parts.setId(jsonObject.optString(CARD_ALL_PARTS_ID));
    card_all_parts.setObject(jsonObject.optString(CARD_ALL_PARTS_OBJECT));
    card_all_parts.setComponent(jsonObject.optString(CARD_ALL_PARTS_COMPONENT));
    card_all_parts.setName(jsonObject.optString(CARD_ALL_PARTS_NAME));
    card_all_parts.setType_line(jsonObject.optString(CARD_ALL_PARTS_TYPE_LINE));
    card_all_parts.setUri(jsonObject.optString(CARD_ALL_PARTS_URI));

    return card_all_parts;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getType_line() {
    return type_line;
  }

  public void setType_line(String type_line) {
    this.type_line = type_line;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }
}
