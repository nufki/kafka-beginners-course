package io.conductor.demos.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        // create producer properties
        Properties properties = new Properties();

        // connect to localhost
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // connect to remote server e.g. Conduktor Playground
//        properties.setProperty("bootstrap.servers", "cluster.playground.cdkt.io:9092");
//        properties.setProperty("sasl.jass.config", "SASL_SSL");
//        properties.setProperty("sasl.mechanisms", "org.apache.kafka.common.security.plain.PlainLoginModule required username=...");
//        properties.setProperty("security.protocol", "PLAIN");

        // set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        // create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);
        // create a producer record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java","hello world");
        // send data
        producer.send(producerRecord);
        // flush and close the producer
        producer.flush(); // tell the producer to send all data and block until done -- synchronous
        producer.close();
    }
}