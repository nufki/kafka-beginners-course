package io.conductor.demos.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Consumer");

        String groupId = "my-java-application";
        String topic = "demo_java";

        // create producer properties
        Properties properties = new Properties();

        // connect to localhost
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // connect to remote server e.g. Conduktor Playground
//        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
//        properties.setProperty("sasl.jass.config", "SASL_SSL");
//        properties.setProperty("sasl.mechanisms", "org.apache.kafka.common.security.plain.PlainLoginModule required username=...");
//        properties.setProperty("security.protocol", "PLAIN");

        // create consumer configs
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest"); // earliest = from the beginning

        // create a consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe for a topic
        consumer.subscribe(Arrays.asList(topic)); // in this demo its not really a list but to see that you can subscribe to multiple topics
        // poll for data (Kafka is very efficient as it fetches chunks of data up to 1mbyte from each partitions in batches
        while(true) {
            log.info("Polling");
            // ConsumerRecords<String, String> records = consumer.poll(1000); // in milliseconds
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000)); // in milliseconds
            for (ConsumerRecord<String, String> record : records) {
                log.info("Key: {}, Value: {}", record.key(), record.value());
                log.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }
    }
}