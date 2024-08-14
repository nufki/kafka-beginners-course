 # Curl schema registry entries
 curl --silent -X GET http://localhost:8081/subjects/testjsonschema-value/versions/1/schema
 curl --silent -X GET http://localhost:8081/subjects/demo-schemaregistry-value/versions/2/schema


curl --silent -X GET http://localhost:8081/subjects/testjsonschema-value/versions/latest



# Producer Configurations: https://docs.confluent.io/platform/current/installation/configuration/producer-configs.html
# Consumer Configurations: https://docs.confluent.io/platform/current/installation/configuration/consumer-configs.html


# kafka-console-consumer.sh itself does not directly support Avro serialization out-of-the-box. Instead, you will need to use the kafka-console-consumer in conjunction with an Avro deserializer provided by a schema registry.
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic transactions --from-beginning --property schema.registry.url=localhost:8081

# the console consumer provided by confluent is able to do that.
kafka-avro-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic transactions \
  --from-beginning \
  --property schema.registry.url=http://localhost:8081 \
  --property value.deserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer \
  --property specific.avro.reader=true
