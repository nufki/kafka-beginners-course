# create a topic 3 partitions
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic wikimedia.stats.bots --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic wikimedia.stats.website --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic event-count-store --partitions 3 --replication-factor 1



