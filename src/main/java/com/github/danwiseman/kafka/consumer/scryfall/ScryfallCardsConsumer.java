package com.github.danwiseman.kafka.consumer.scryfall;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ScryfallCardsConsumer {
    private static final Logger log = LoggerFactory.getLogger(ScryfallCardsConsumer.class);
    static ConnectionString connectionString = new ConnectionString("mongodb://AzureDiamond:hunter2@docker:27017/");
    static MongoClient mongoClient = MongoClients.create(connectionString);

    public static void main(String[] args) {

        String bootstrapServers = "kafka1:9092";
        String groupId = "scryfall_cards_consumer";
        String topic = "scryfall_cards";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        final Thread mainThread = Thread.currentThread();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
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
        });

        try {
            // sub to topic
            consumer.subscribe(Arrays.asList(topic));

            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("Key" + record.key());
                    ConsumeScryfallCard(record.key(), record.value());

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
            InsertOneResult insertResult = documents.insertOne(new Document().parse(value));
            log.info("Inserted with id " + insertResult.getInsertedId());
        } catch (MongoException me) {
            log.error("MongoException " + me);
        }
    }

}
