# create a topic 3 partitions
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic wikimedia.recentchange --partitions 3 --replication-factor 1

