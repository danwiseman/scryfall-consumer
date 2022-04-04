FROM openjdk:8-jdk-alpine as cardsconsumer

WORKDIR /scryfall
COPY config config

VOLUME /scryfall/config

ARG JAR_FILE=target/scryfall-cards-consumer-0.1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /scryfall/app.jar /scryfall/config/ScryfallCardConsumer.properties"]


FROM openjdk:8-jdk-alpine as cardtagsconsumer

WORKDIR /scryfall
COPY config config

VOLUME /scryfall/config

ARG JAR_FILE=target/scryfall-cards-tags-consumer-0.1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /scryfall/app.jar /scryfall/config/ScryfallCardTagsConsumer.properties"]