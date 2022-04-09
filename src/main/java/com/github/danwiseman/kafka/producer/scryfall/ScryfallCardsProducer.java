package com.github.danwiseman.kafka.producer.scryfall;

import com.github.danwiseman.kafka.producer.scryfall.utils.EnvTools;
import com.github.danwiseman.scryfall.ScryfallApiClient;
import com.github.danwiseman.scryfall.models.Card;
import com.github.danwiseman.scryfall.utils.DateUtils;
import java.time.Instant;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScryfallCardsProducer {

  private static final Logger log = LoggerFactory.getLogger(
    ScryfallCardsProducer.class
  );

  public static void main(String[] args) {
    Properties config = createProperties();
    config.setProperty(
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class.getName()
    );
    config.setProperty(
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class.getName()
    );
    config.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    String topic = EnvTools.getEnvValue(EnvTools.TOPIC, "scryfall-cards-input");

    KafkaProducer<String, String> producer = new KafkaProducer<>(config);

    ScryfallApiClient scryfallApiClient = new ScryfallApiClient();
    String page = constructUrl(DateUtils.InstantFromScryFallDate("1984-01-01"));
    do {
      try {
        JSONArray cards = scryfallApiClient.getScryfallData(page);
        if (scryfallApiClient.hasMoreData()) page =
          scryfallApiClient.getNextPage();

        for (Object jsonCard : cards) {
          Card card = Card.fromJson((JSONObject) jsonCard);
          ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
            topic,
            card.getId(),
            ((JSONObject) jsonCard).toString()
          );
          producer.send(producerRecord);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } while (scryfallApiClient.hasMoreData());

    producer.flush();
    producer.close();
  }

  protected static String constructUrl(Instant since) {
    return String.format(
      "https://api.scryfall.com/cards/search?order=released&dir=asc&q=year%%3E%%3D%s&unique=prints",
      DateUtils.ScryFallDateFromInstant(since)
    );
  }

  private static Properties createProperties() {
    Properties props = new Properties();

    String bootstrapServersConfig = EnvTools.getEnvValue(
      EnvTools.BOOTSTRAP_SERVERS_CONFIG,
      "kafka1:9092"
    );
    String compressionType = EnvTools.getEnvValue(
      EnvTools.COMPRESSION_TYPE_CONFIG,
      "snappy"
    );
    String lingerMs = EnvTools.getEnvValue(EnvTools.LINGER_MS_CONFIG, "20");
    String batchSize = EnvTools.getEnvValue(
      EnvTools.BATCH_SIZE_CONFIG,
      Integer.toString(32 * 1024)
    );

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
    props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);

    return props;
  }
}
