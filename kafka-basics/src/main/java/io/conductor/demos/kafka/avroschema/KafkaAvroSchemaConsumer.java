package io.conductor.demos.kafka.avroschema;

import avro.Payment;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/* Example:
 * https://github.com/confluentinc/examples/blob/latest/clients/avro/src/main/java/io/confluent/examples/clients/basicavro/ConsumerExample.java
 */
public class KafkaAvroSchemaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaAvroSchemaConsumer.class.getSimpleName());
    private static final String TOPIC = "transactions";
    private static final Properties props = new Properties();

    public static void main(String[] args) {
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-payments");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // Auto commit offsets (default)
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,"http://localhost:8081"); // "schema.registry.url"
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true"); // This is necessary otherwise it will be treated as GenericData$Record

        try (final KafkaConsumer<String, Payment> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (true) {
                final ConsumerRecords<String, Payment> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, Payment> record : records) {
                    final String key = record.key();
                    final Payment value = record.value();
                    System.out.printf("key = %s, value = %s%n", key, value);
                }
            }
        }
    }
}