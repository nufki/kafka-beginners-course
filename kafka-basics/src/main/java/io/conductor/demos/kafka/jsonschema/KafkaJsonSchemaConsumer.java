package io.conductor.demos.kafka.jsonschema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;


/* Example is based on:
* https://docs.confluent.io/cloud/current/sr/fundamentals/serdes-develop/serdes-json.html#ak-producer-configurations
* */
public class KafkaJsonSchemaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaJsonSchemaConsumer.class.getSimpleName());
    private static final String TOPIC = "testjsonschema";
    private static final Properties props = new Properties();

    public static void main(String[] args) {
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonSchemaDeserializer.class.getName());
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, User.class.getName());

        ObjectMapper mapper = new ObjectMapper();
        final Consumer<String, JsonNode> consumer = new KafkaConsumer<String, JsonNode>(props);
        consumer.subscribe(Arrays.asList(TOPIC));

        try {
            while (true) {
                ConsumerRecords<String, JsonNode> records = consumer.poll(100);
                records.forEach(record -> {
                    try {
                        // Convert JsonNode to User object
                        User user = mapper.convertValue(record.value(), User.class);
                        log.info("Received User: {}", user);
                        // Process the User object
                    } catch (Exception e) {
                        log.error("Error converting JsonNode to User", e);
                    }
                });
            }
        }
        catch (Exception e) {

        } finally {
            consumer.close();
        }
    }
}