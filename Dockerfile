FROM openjdk:8-alpine AS scryfall_cards_consumer

WORKDIR /app/
COPY target/scryfall-cards-consumer-*.jar .
ENTRYPOINT ["java", "-cp", "*", "com.github.danwiseman.kafka.consumer.scryfall.ScryfallCardsConsumer"]


FROM openjdk:8-alpine AS scryfall_tagged_cards_consumer

WORKDIR /app/
COPY target/scryfall-cards-tags-consumer-*.jar .
ENTRYPOINT ["java", "-cp", "*", "com.github.danwiseman.kafka.consumer.scryfall.ScryfallCardTagsConsumer"]
