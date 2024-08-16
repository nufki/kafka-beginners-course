package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {
    private static final String INPUT_TOPIC = "wikimedia.recentchange";
    private static final Properties props = new Properties();
    
    public static void main(String[] args) throws InterruptedException {

        // create Producer Properties
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // set some high throughput producer configs
        props.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        props.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
        props.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        // set safe producer configs for Kafka <= 2.8) above these settings are default see slide picture (13)
        // props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        // props.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        // props.setProperty(ProducerConfig.RETRIES_CONFIG, String.valueOf(Integer.MAX_VALUE));

        // to set the min.insync.replicas value configure using:
        // kafka-configs.sh --bootstrap-server localhost:9092 --alter --entity-type topics --entity-name configured-topic --add-config min.insync.replicas=2
        // Read: https://www.conduktor.io/kafka/kafka-topic-configuration-min-insync-replicas/

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        
        // Doing HTTP efficiently makes your stuff load faster and saves bandwidth. OkHttp is an HTTP client that's efficient by default
        EventHandler eventHandler = new WikimediaChangeHandler(producer, INPUT_TOPIC);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        // start the producer in another thread
        eventSource.start(); // start the eventhandler

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10); // block main thread for 10 minutes
    }
}
