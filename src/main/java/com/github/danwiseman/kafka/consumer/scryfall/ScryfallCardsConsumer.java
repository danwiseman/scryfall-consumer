package com.github.danwiseman.kafka.consumer.scryfall;

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
    if (args.length == 0) {
      System.out.println(
        "Please input a properties file and mongo connection string"
      );
      System.exit(0);
    }

    Properties properties = new Properties();

    try (InputStream input = new FileInputStream(args[0])) {
      properties.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    connectionString =
      new ConnectionString(properties.getProperty("mongo.connection"));
    mongoClient = MongoClients.create(connectionString);

    String topic = properties.getProperty("topic");
    final int minBatchSize = Integer.parseInt(
      properties.getProperty("min.batch.size")
    );

    // create consumer configs
    Properties consumerConfigs = new Properties();
    consumerConfigs.setProperty(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
      properties.getProperty("bootstrap.servers")
    );
    consumerConfigs.setProperty(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class.getName()
    );
    consumerConfigs.setProperty(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      StringDeserializer.class.getName()
    );
    consumerConfigs.setProperty(
      ConsumerConfig.GROUP_ID_CONFIG,
      properties.getProperty("group.id")
    );
    consumerConfigs.setProperty(
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
      properties.getProperty("auto.offset.reset.config")
    );
    consumerConfigs.setProperty(
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
      "false"
    );

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(
      consumerConfigs
    );

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

  private static void ConsumeScryfallCard(String key, String value) {
    MongoDatabase database = mongoClient.getDatabase("scryfall");
    MongoCollection<Document> documents = database.getCollection("cards");

    try {
      JSONObject cardJson = new JSONObject(value);
      cardJson.put("_id", cardJson.getString("id"));
      InsertOneResult insertResult = documents.insertOne(
        new Document().parse(cardJson.toString())
      );
      log.info("Inserted with id " + insertResult.getInsertedId());
    } catch (MongoException me) {
      log.error("MongoException " + me);
    }
  }
}
