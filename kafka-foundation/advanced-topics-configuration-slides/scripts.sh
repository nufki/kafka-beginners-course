// Create new topic
kafka-topics.sh --create --bootstrap-server localhost:9092 --topic configured-topic --replication-factor 1 --partitions 3
>> Created topic configured-topic.

// Describe topic
kafka-topics.sh --bootstrap-server localhost:9092 --topic configured-topic --describe 
>> [2024-08-05 11:19:25,829] WARN [AdminClient clientId=adminclient-1] The DescribeTopicPartitions API is not supported, using Metadata API to describe topics. (org.apache.kafka.clients.admin.KafkaAdminClient)
        Topic: configured-topic TopicId: a9-nWw_4S7GtiPSzXeO30g PartitionCount: 3       ReplicationFactor: 1    Configs: 
        Topic: configured-topic Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: configured-topic Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: configured-topic Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A

// describe topic configuration 
kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name configured-topic --describe
>>Dynamic configs for topic configured-topic are:

// Change topic configuration
kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name configured-topic --alter --add-config min.insync.replicas=2
>>Completed updating config for topic configured-topic.

// Describe topic again to see the changed config
kafka-topics.sh --bootstrap-server localhost:9092 --topic configured-topic --describe                                                            
>>[2024-08-05 11:22:03,114] WARN [AdminClient clientId=adminclient-1] The DescribeTopicPartitions API is not supported, using Metadata API to describe topics. (org.apache.kafka.clients.admin.KafkaAdminClient)
        Topic: configured-topic TopicId: a9-nWw_4S7GtiPSzXeO30g PartitionCount: 3       ReplicationFactor: 1    Configs: min.insync.replicas=2
        Topic: configured-topic Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: configured-topic Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: configured-topic Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A

// Delete the topic configuration
kafka-configs.sh --bootstrap-server localhost:9092 --entity-type topics --entity-name configured-topic --alter --delete-config min.insync.replicas  
>>Completed updating config for topic configured-topic.

// Describe topic again to see the changed config (reset)
kafka-topics.sh --bootstrap-server localhost:9092 --topic configured-topic --describe    
[2024-08-05 11:32:19,819] WARN [AdminClient clientId=adminclient-1] The DescribeTopicPartitions API is not supported, using Metadata API to describe topics. (org.apache.kafka.clients.admin.KafkaAdminClient)
>>Topic: configured-topic TopicId: a9-nWw_4S7GtiPSzXeO30g PartitionCount: 3       ReplicationFactor: 1    Configs: 
        Topic: configured-topic Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: configured-topic Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: configured-topic Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A



kafka-topics.sh --bootstrap-server localhost:9092 --topic __consumer_offsets --describe
[2024-08-05 12:33:33,987] WARN [AdminClient clientId=adminclient-1] The DescribeTopicPartitions API is not supported, using Metadata API to describe topics. (org.apache.kafka.clients.admin.KafkaAdminClient)
Topic: __consumer_offsets       TopicId: TwqIV3NASLK9wjAKBTnVsQ PartitionCount: 50      ReplicationFactor: 1    Configs: compression.type=producer,cleanup.policy=compact,segment.bytes=104857600
        Topic: __consumer_offsets       Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 3    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 4    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 5    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 6    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 7    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 8    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 9    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 10   Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A

// Describe __consumer_offsets topic
kafka-topics.sh --bootstrap-server localhost:9092 --topic __consumer_offsets --describe
[2024-08-05 12:33:33,987] WARN [AdminClient clientId=adminclient-1] The DescribeTopicPartitions API is not supported, using Metadata API to describe topics. (org.apache.kafka.clients.admin.KafkaAdminClient)
>>      Topic: __consumer_offsets       TopicId: TwqIV3NASLK9wjAKBTnVsQ PartitionCount: 50      ReplicationFactor: 1    Configs: compression.type=producer,cleanup.policy=compact,segment.bytes=104857600
        Topic: __consumer_offsets       Partition: 0    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 1    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 2    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 3    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 4    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 5    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 6    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 7    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 8    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 9    Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A
        Topic: __consumer_offsets       Partition: 10   Leader: 1       Replicas: 1     Isr: 1  Elr: N/A        LastKnownElr: N/A