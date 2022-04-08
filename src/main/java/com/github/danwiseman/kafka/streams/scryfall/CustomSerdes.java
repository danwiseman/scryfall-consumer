package com.github.danwiseman.kafka.streams.scryfall;

import com.github.danwiseman.scryfall.models.OracleCard;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdes {

  private CustomSerdes() {}

  public static Serde<OracleCard> OracleCard() {
    JsonSerializer<OracleCard> serializer = new JsonSerializer<>();
    JsonDeserializer<OracleCard> deserializer = new JsonDeserializer<>(
      OracleCard.class
    );
    return Serdes.serdeFrom(serializer, deserializer);
  }
  //  public static Serde<CommanderCardsStats> CommanderCardsStats() {
  //    JsonSerializer<CommanderCardsStats> serializer = new JsonSerializer<>();
  //    JsonDeserializer<CommanderCardsStats> deserializer = new JsonDeserializer<>(
  //      CommanderCardsStats.class
  //    );
  //    return Serdes.serdeFrom(serializer, deserializer);
  //  }
}
