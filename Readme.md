There are now two Kafka Consumers here.

# Scryfall Card Consumer

This is a very basic scryfall card consumer for putting cards into a MongoDB streamed from my 
[Scryfall Kafka Connect](https://github.com/danwiseman/scryfallconnect) application. 
It is set up to use the default settings for a MongoDB docker. Change the settings 
for your database.

# Scryfall Card Tagger Consumer

This application will take JSONs that have Tagger tags from Scryfall, and it will then
update the corresponding card in the database. This works best in conjunction with the
above scryfall card consumer.

Currently, I use [NiFi](https://nifi.apache.org/) to push these JSONs into Kafka:

```json
     {
        "name": "Abuna Acolyte",
        "id": "9e17bbf7-00c0-46f2-9718-2762fd7388d3",
        "tags": ["2-people"],
        "tag_type": "art"
      }
```

This would be a good thing to use with Kafka Streams to combine similar tag_type tags 
into one JSON.

# :whale: Docker

First build each image or run docker compose.

```shell
docker build -t scryfall_cards_consumer --target scryfall_cards_consumer .
docker build -t scryfall_tagged_cards_consumer --target scryfall_tagged_cards_consumer .
#----- OR -----
docker-compose -f docker-compose.yml up -d
```