package com.github.danwiseman.kafka.streams.scryfall.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  public static Instant MaxInstant(Instant i1, Instant i2) {
    return i1.compareTo(i2) > 0 ? i1 : i2;
  }

  public static Instant InstantFromScryFallDate(String dateToConvert) {
    dateToConvert = dateToConvert + "T00:00:00Z";
    return Instant.parse(dateToConvert);
  }

  public static String ScryFallDateFromInstant(Instant dateToConvert) {
    return DateTimeFormatter
      .ofPattern("yyyy-MM-dd")
      .withZone(ZoneId.of("UTC"))
      .format(dateToConvert);
  }
}
