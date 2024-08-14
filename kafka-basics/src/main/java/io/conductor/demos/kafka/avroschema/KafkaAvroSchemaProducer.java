package io.conductor.demos.kafka.avroschema;

import avro.Payment;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/* Example from:
* https://docs.confluent.io/cloud/current/sr/fundamentals/serdes-develop/serdes-json.html#ak-producer-configurations
* https://github.com/confluentinc/examples/blob/latest/clients/avro/src/main/java/io/confluent/examples/clients/basicavro/ProducerExample.java
* */
public class KafkaAvroSchemaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaAvroSchemaProducer.class.getSimpleName());
    private static final String TOPIC = "transactions";
    private static final Properties props = new Properties();

    public static void main(String[] args) {
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://127.0.0.1:8081");

        try (KafkaProducer<String, Payment> producer = new KafkaProducer<String, Payment>(props)) {

            for (long i = 0; i < 10; i++) {
                final String orderId = "id" + Long.toString(i);
                final Payment payment = new Payment(orderId, 1000.00d);
                final ProducerRecord<String, Payment> record = new ProducerRecord<String, Payment>(TOPIC, payment.getId().toString(), payment);
                producer.send(record);
                Thread.sleep(1000L);
            }

            producer.flush();
            System.out.printf("Successfully produced 10 messages to a topic called %s%n", TOPIC);

        } catch (final SerializationException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

    }
}