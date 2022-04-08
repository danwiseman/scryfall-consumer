package com.github.danwiseman.kafka.producer.scryfall;

import com.github.danwiseman.kafka.producer.scryfall.utils.EnvTools;
import com.github.danwiseman.scryfall.ScryfallApiClient;
import com.github.danwiseman.scryfall.models.Card;
import com.github.danwiseman.scryfall.utils.DateUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScryfallCardTagsProducer {

  private static final Logger log = LoggerFactory.getLogger(
    ScryfallCardTagsProducer.class
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
    String topic = EnvTools.getEnvValue(
      EnvTools.TOPIC,
      "tagged-scryfall-cards"
    );
    String tags_json = EnvTools.getEnvValue(
      EnvTools.SCRYFALL_TAGS_JSON,
      ScryfallCardTagsProducer.class.getClassLoader()
        .getResource("scryfall-tags.json")
        .getFile()
    );

    KafkaProducer<String, String> producer = new KafkaProducer<>(config);

    ScryfallApiClient scryfallApiClient = new ScryfallApiClient();

    // Steps:
    // Step 1: Load the JSON file containing the tags and the urls.

    InputStream inStream = null;
    String cardTagJsonFile = null;
    try {
      inStream = new FileInputStream(tags_json);
      cardTagJsonFile =
        IOUtils.toString(inStream, StandardCharsets.UTF_8.name());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    JSONObject cardTagJson = new JSONObject(cardTagJsonFile);
    JSONArray cardTagArray = cardTagJson.getJSONArray("tags");
    for (Object tag : cardTagArray) {
      JSONObject jsonTag = (JSONObject) tag;
      // Step 2: Set the tag type from the URL
      String tagLink = jsonTag.getString("tag_link");
      String tagType = tagLink.substring(
        tagLink.indexOf("q=") + 2,
        tagLink.indexOf("%")
      );
      String tagName = jsonTag.getString("tag");
      String page = tagLink;
      do {
        // Step 3: Grab the search pages containing those tags, using initial URL from the JSON
        try {
          JSONArray cards = scryfallApiClient.getScryfallData(page);
          if (scryfallApiClient.hasMoreData()) page =
            scryfallApiClient.getNextPage();
          // Step 4: Create a CARD object for each result, get its oracle_id
          for (Object jsonCard : cards) {
            Card card = Card.fromJson((JSONObject) jsonCard);
            // Step 5: Generate the JSON for the tag:
            //    {
            //      "name": "Abuna Acolyte",
            //            "id": "9e17bbf7-00c0-46f2-9718-2762fd7388d3",
            //            "tag_type": "art",
            //            "tags": ["2-people"]
            //    }
            String taggedCard = String.format(
              "{ \"name\": \"%s\", \"id\": \"%s\", \"tag_type\": \"%s\", \"tags\": [\"%s\"] }",
              card.getName(),
              card.getOracle_id(),
              tagType,
              tagName
            );
            // Step 6: Produce the JSON into the topic. key is the tagName
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
              topic,
              tagName,
              taggedCard
            );
            producer.send(producerRecord);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } while (scryfallApiClient.hasMoreData());
      // Step 7: Repeat...
    }

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
