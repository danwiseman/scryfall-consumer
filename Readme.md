# Scryfall Kafka 

This project is now known as **scryfall-kafka**. There will be producers for pulling scryfall data from the
excellent [Scryfall API](https://scryfall.com/docs/api).

# Scryfall Kafka Producers

## Scryfall Card Producer

This is a very basic Kafka producer that grabs ALL of the English prints in the Scryfall API. It has 
hard coded rate limits; but will keep going until they are all produced into the kafka topic.

:hammer: soon an AVRO producer...

## Scryfall Card Tagger Producer

:hammer: WIP...

# Scryfall Kafka Consumers

## Scryfall Card Consumer

This is a very basic scryfall card consumer for putting cards into a MongoDB streamed from my 
[Scryfall Kafka Connect](https://github.com/danwiseman/scryfallconnect) application or the above Scyrfall 
Kafka Producer. It is set up to use the default settings for a MongoDB docker. Change the settings for 
your database.

:hammer: soon an AVRO consumer...

## Scryfall Card Tagger Consumer

This application will take JSONs that have Tagger tags from Scryfall, and it will then
update the corresponding card in the database. This works best in conjunction with the
above scryfall card consumer.

Currently, I use [NiFi](https://nifi.apache.org/) to push these JSONs into Kafka, but there will be a 
producer created soon.

```json
     {
        "name": "Abuna Acolyte",
        "id": "9e17bbf7-00c0-46f2-9718-2762fd7388d3",
        "tags": ["2-people"],
        "tag_type": "art"
      }
```

This could also be combined into a Kafka Streams application for fun.

# Scryfall Kafka Streams

## Oracle Card Streamer

This Kafka Streams application joins the individual card prints together into a smaller JSON object with some of the card
information. 

The JSON looks similar to below (not finalized):

```json
{
  "oracle_id": "fae37e28-e137-4177-b973-fa8b4dd8f409",
  "oracle_name": "Generous Gift",
  "colors": [
    "W"
  ],
  "color_identity": [
    "W"
  ],
  "color_indicator": [
    "W"
  ],
  "mana_cost": "string",
  "oracle_text": "string",
  "power": "string",
  "toughness": "string",
  "layout": "string",
  "cheapest_price": 2.1,
  "highest_price": 2.5,
  "cmc": 3,
  "type_line": "Instant",
  "legalities": {
    "standard": "legal",
    "future": "legal",
    "historic": "legal",
    "gladiator": "legal",
    "pioneer": "legal",
    "modern": "legal",
    "legacy": "legal",
    "pauper": "legal",
    "vintage": "legal",
    "penny": "legal",
    "commander": "legal",
    "brawl": "legal",
    "historicbrawl": "legal",
    "alchemy": "legal",
    "paupercommander": "legal",
    "duel": "legal",
    "oldschool": "legal",
    "premodern": "legal"
  },
  "card_prints": [
    "f2bb6b0c-45e8-43ff-8fd6-78fbf6979b68",
    "96d4e224-e630-4696-85ef-cf42f9d03ca5"
  ],
  "card_images": {
    "small": "string",
    "normal": "string",
    "large": "string",
    "png": "string",
    "art_crop": "string",
    "border_crop": "string"
  }
}
```

# :whale: Docker

First build each image or run docker compose.

```shell
docker build -t scryfall_cards_consumer --target scryfall_cards_consumer .
docker build -t scryfall_tagged_cards_consumer --target scryfall_tagged_cards_consumer .
#----- OR -----
docker-compose -f docker-compose.yml up -d
```