package io.conductor.demos.kafka.jsonschema;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;


/*
* https://docs.confluent.io/cloud/current/sr/fundamentals/serdes-develop/serdes-json.html#ak-producer-configurations
* */
public class KafkaJsonSchemaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaJsonSchemaProducer.class.getSimpleName());
    private static final String TOPIC = "testjsonschema";
    private static final Properties props = new Properties();
    
    public static void main(String[] args) {
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class.getName()); // io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://127.0.0.1:8081");

        Producer<String, User> producer = new KafkaProducer<String, User>(props);

        String key = "testkey";
        User user = new User("Kei", "Nufer", 43);

        ProducerRecord<String, User> record
                = new ProducerRecord<String, User>(TOPIC, key, user);

        try {
            producer.send(record).get();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        producer.close();
    }
}