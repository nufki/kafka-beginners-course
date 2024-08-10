package io.conductor.demos.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoWithCallback {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemoWithCallback.class.getSimpleName());

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

        properties.setProperty("batch.size", "400"); // Kafka default is 16kbyte, reduce to 400 bytes to demo partition switch
        // properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName()); // Don't apply this in prod (only to demo the partitioner switching all the time

        // create the producer
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        // send data (demo the StickyPartitioner if messages are quickly send in series kafka is smart enough to batch them up to make it more efficient this is why all the messages get sent to the same partition
        // Since we don't apply a key here kafka will take the default partitioner (partitioner.class = null)... to demo switches multi batches and sleep will be applied to see partition switches
        for (int j = 0; j < 10; j++) {

            for (int i = 0; i < 30; i++) {
                // create a producer record
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("demo_java", "hello world: " + i);
                producer.send(producerRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        // executed everytime a record is successfully sent or an exception is thrown
                        if (e == null) {
                            // the record was successfully sent
                            log.info("Received new metadata \n" +
                                    "Topic: " + recordMetadata.topic() + "\n" +
                                    "Partition: " + recordMetadata.partition() + "\n" +
                                    "Offset: " + recordMetadata.offset() + "\n" +
                                    "Timestamp: " + recordMetadata.timestamp());
                        } else {
                            log.error("Error while producing message", e);
                        }
                    }
                });
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // flush and close the producer
        producer.flush(); // tell the producer to send all data and block until done -- synchronous
        producer.close();
    }
}