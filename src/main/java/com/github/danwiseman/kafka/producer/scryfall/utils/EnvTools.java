package com.github.danwiseman.kafka.producer.scryfall.utils;

public class EnvTools {

  public static final String TOPIC = "TOPIC";
  public static final String BOOTSTRAP_SERVERS_CONFIG =
    "BOOTSTRAP_SERVERS_CONFIG";
  public static final String COMPRESSION_TYPE_CONFIG =
    "COMPRESSION_TYPE_CONFIG";
  public static final String LINGER_MS_CONFIG = "LINGER_MS_CONFIG";
  public static final String BATCH_SIZE_CONFIG = "BATCH_SIZE_CONFIG";

  public static String getEnvValue(String environmentKey, String defaultValue) {
    String envValue = System.getenv(environmentKey);
    if (envValue != null && !envValue.isEmpty()) {
      return envValue;
    }
    return defaultValue;
  }
}
