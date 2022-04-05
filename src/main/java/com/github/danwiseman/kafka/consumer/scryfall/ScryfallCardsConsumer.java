package com.github.danwiseman.kafka.consumer.scryfall;

import com.github.danwiseman.kafka.consumer.scryfall.utils.EnvTools;
import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScryfallCardsConsumer {

  private static final Logger log = LoggerFactory.getLogger(
    ScryfallCardsConsumer.class
  );

  private static ConnectionString connectionString;
  private static MongoClient mongoClient;

  public static void main(String[] args) {
    Properties config = createProperties();

    String topic = EnvTools.getEnvValue(EnvTools.TOPIC, "scryfall_cards");
    Integer minBatchSize = Integer.parseInt(
      EnvTools.getEnvValue(EnvTools.MIN_BATCH_SIZE, "15")
    );

    connectionString =
      new ConnectionString(
        EnvTools.getEnvValue(
          EnvTools.MONGODB_CONNECTION_STRING,
          "mongodb://AzureDiamond:hunter2@docker:27017/"
        )
      );
    mongoClient = MongoClients.create(connectionString);

    config.setProperty(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class.getName()
    );
    config.setProperty(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class.getName()
    );
    config.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);

    final Thread mainThread = Thread.currentThread();

    // add shutdown hook
    Runtime
      .getRuntime()
      .addShutdownHook(
        new Thread() {
          public void run() {
            log.info("Detected a shutdown. Call consumer.wakeup()...");
            consumer.wakeup();

            // join main thread
            try {
              mainThread.join();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      );

    try {
      consumer.subscribe(Arrays.asList(topic));
      List<ConsumerRecord<String, String>> buffer = new ArrayList<>();

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(
          Duration.ofMillis(100)
        );

        for (ConsumerRecord<String, String> record : records) {
          log.info("Key" + record.key());
          buffer.add(record);
        }
        if (buffer.size() >= minBatchSize) {
          for (ConsumerRecord<String, String> record : buffer) {
            ConsumeScryfallCard(record.key(), record.value());
          }
          consumer.commitSync();
          buffer.clear();
        }
      }
    } catch (WakeupException e) {
      log.info("WakeupException");
    } catch (Exception e) {
      log.error("Unexpected exception", e);
    } finally {
      consumer.close();
      log.info("Consumer closed");
    }
  }

  private static Properties createProperties() {
    Properties props = new Properties();
    String groupId = EnvTools.getEnvValue(
      EnvTools.GROUP_ID_CONFIG,
      "scryfall-cards-consumer"
    );
    String bootstrapServersConfig = EnvTools.getEnvValue(
      EnvTools.BOOTSTRAP_SERVERS_CONFIG,
      "kafka1:9092"
    );
    String autoOffsetResetConfig = EnvTools.getEnvValue(
      EnvTools.AUTO_OFFSET_RESET_CONFIG,
      "earliest"
    );

    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetResetConfig);

    return props;
  }

  private static void ConsumeScryfallCard(String key, String value) {
    MongoDatabase database = mongoClient.getDatabase(
      EnvTools.getEnvValue(EnvTools.MONGODB_DATABASE, "scryfall")
    );
    MongoCollection<Document> collection = database.getCollection(
      EnvTools.getEnvValue(EnvTools.MONGODB_COLLECTION, "cards")
    );

    try {
      JSONObject cardJson = new JSONObject(value);
      cardJson.put("_id", cardJson.getString("id"));
      InsertOneResult insertResult = collection.insertOne(
        new Document().parse(cardJson.toString())
      );
      log.info("Inserted with id " + insertResult.getInsertedId());
    } catch (MongoException me) {
      log.error("MongoException " + me);
    }
  }
}
