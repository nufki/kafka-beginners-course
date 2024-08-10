package io.conductor.demos.kafka.streams.wikimedia.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;

import java.io.IOException;
import java.util.Map;



//Example content form wikimedia
//
//        "_index" : "wikimedia.recentchange",
//        "_type" : "_doc",
//        "_id" : "wikimedia.recentchange+0+1462",
//        "_score" : 1.0,
//        "_source" : {
//          "server_script_path" : "/w",
//          "server_name" : "www.wikidata.org",
//          "$schema" : "/mediawiki/recentchange/1.0.0",
//          "minor" : false,
//          "bot" : false,
//          "wiki" : "wikidatawiki",
//          "length" : {
//            "new" : 15477
//          },
//          "type" : "new",
//          "title" : "Q128375738",
//          "notify_url" : "https://www.wikidata.org/w/index.php?oldid=2219598763&rcid=2285622858",
//          "revision" : {
//            "new" : 2219598763
//          },
//          "title_url" : "https://www.wikidata.org/wiki/Q128375738",
//          "patrolled" : true,
//          "meta" : {
//            "dt" : "2024-08-03T06:18:43Z",
//            "partition" : 0,
//            "offset" : 5307305983,
//            "stream" : "mediawiki.recentchange",
//            "domain" : "www.wikidata.org",
//            "topic" : "eqiad.mediawiki.recentchange",
//            "id" : "bb44eca6-52ba-479e-8cd1-11a6edd5c3b2",
//            "uri" : "https://www.wikidata.org/wiki/Q128375738",
//            "request_id" : "6a6ba171-b23a-4aff-9e4a-b0fa349da7f8"
//          },
//          "namespace" : 0,
//          "comment" : "/* wbeditentity-create-item:0| */ add scholarly article from crossref ([[:toollabs:editgroups/b/OR/4ef984c8d53|details]])",
//        "id" : 2285622858,
//        "server_url" : "https://www.wikidata.org",
//        "user" : "DaxServer",
//        "parsedcomment" : """‎<span dir="auto"><span class="autocomment">Created a new Item: </span></span> add scholarly article from crossref (<a href="https://iw.toolforge.org/editgroups/b/OR/4ef984c8d53" class="extiw" title="toollabs:editgroups/b/OR/4ef984c8d53">details</a>)""",
//        "timestamp" : 1722665923
//        }
//        },

public class BotCountStreamBuilder {

    private static final String BOT_COUNT_STORE = "bot-count-store";
    private static final String BOT_COUNT_TOPIC = "wikimedia.stats.bots";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final KStream<String, String> inputStream;

    public BotCountStreamBuilder(KStream<String, String> inputStream) {
        this.inputStream = inputStream;
    }

    public void setup() {
        this.inputStream
                .mapValues(changeJson -> {
                    try {
                        final JsonNode jsonNode = OBJECT_MAPPER.readTree(changeJson);
                        if (jsonNode.get("bot").asBoolean()) {
                            return "bot";
                        }
                        return "non-bot";
                    } catch (IOException e) {
                        return "parse-error";
                    }
                })
                .groupBy((key, botOrNot) -> botOrNot)
                .count(Materialized.as(BOT_COUNT_STORE))
                .toStream()
                .mapValues((key, value) -> {
                    /*  Sample output:
                        {
                            "bot": 4408
                        }
                     */
                    final Map<String, Long> kvMap = Map.of(String.valueOf(key), value);
                    try {
                        return OBJECT_MAPPER.writeValueAsString(kvMap);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .to(BOT_COUNT_TOPIC);
    }
}
