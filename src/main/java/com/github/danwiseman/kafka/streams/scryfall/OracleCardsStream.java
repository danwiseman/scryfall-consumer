package com.github.danwiseman.kafka.streams.scryfall;

import com.github.danwiseman.kafka.streams.scryfall.utils.EnvTools;
import com.github.danwiseman.scryfall.models.Card;
import com.github.danwiseman.scryfall.models.OracleCard;
import com.google.gson.JsonParser;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OracleCardsStream {

  private static final Logger log = LoggerFactory.getLogger(
    OracleCardsStream.class
  );

  public static void main(String[] args) {
    Properties config = createProperties();

    config.put(
      StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
      Serdes.String().getClass().getName()
    );
    config.put(
      StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
      Serdes.String().getClass().getName()
    );

    String inputTopic = EnvTools.getEnvValue(
      EnvTools.INPUT_TOPIC,
      "scryfall-cards-input"
    );
    String outputTopic = EnvTools.getEnvValue(
      EnvTools.OUTPUT_TOPIC,
      "oracle-cards-output"
    );

    StreamsBuilder builder = new StreamsBuilder();

    KStream<String, String> scryfallCardsInput = builder.stream(
      inputTopic,
      Consumed.with(Serdes.String(), Serdes.String())
    );

    KTable<String, OracleCard> oracleCardKTable = scryfallCardsInput
      .mapValues(card -> JsonParser.parseString(card))
      .map((key, value) ->
        KeyValue.pair(
          value.getAsJsonObject().get("oracle_id").getAsString(),
          OracleCard.fromScryfallCard(
            Card.fromJson(new org.json.JSONObject(value.toString()))
          )
        )
      )
      .groupByKey(Grouped.with(Serdes.String(), CustomSerdes.OracleCard()))
      .reduce((card1, card2) -> {
        log.info("reducing {}", card1.getOracle_name());
        card1.addCardPrintsFromList(card2.getCard_prints());
        // set the prices to the lowest and highest print costs.
        if (card1.getCheapest_price() > card2.getCheapest_price()) {
          card1.setCheapest_price(card2.getCheapest_price());
        }
        if (card1.getHighest_price() < card2.getHighest_price()) {
          card1.setHighest_price(card2.getHighest_price());
        }
        return card1;
      });

    oracleCardKTable
      .toStream()
      .to(
        outputTopic,
        Produced.with(Serdes.String(), CustomSerdes.OracleCard())
      );

    KafkaStreams streams = new KafkaStreams(builder.build(), config);

    streams.cleanUp();
    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }

  private static Properties createProperties() {
    Properties props = new Properties();
    String appIdConfig = EnvTools.getEnvValue(
      EnvTools.APPLICATION_ID_CONFIG,
      "oracle-cards-app"
    );
    String bootstrapServersConfig = EnvTools.getEnvValue(
      EnvTools.BOOTSTRAP_SERVERS_CONFIG,
      "kafka1:9092"
    );
    String autoOffsetResetConfig = EnvTools.getEnvValue(
      EnvTools.AUTO_OFFSET_RESET_CONFIG,
      "earliest"
    );

    props.put(StreamsConfig.APPLICATION_ID_CONFIG, appIdConfig);
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);

    return props;
  }
}
