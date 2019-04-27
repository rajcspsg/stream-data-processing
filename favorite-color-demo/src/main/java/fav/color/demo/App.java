package fav.color.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class App {

    private static  final Set<String> colors = new HashSet<String>(Arrays.asList("red", "blue", "green"));

    public static void main(String[] args) {
        System.out.println("favorite color App!!!");

        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "favorite-color-app");
        streamsConfiguration.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1.com:9092,kafka2.com:9092,kafka3.com:9092");
        streamsConfiguration.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        final Serde<String> stringSerde = Serdes.String();
        final Serde<Long> longSerde = Serdes.Long();

        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> streamWords = builder.stream("favorite-color-input");

        final KStream<String, String> usersAndColors = streamWords
                .filter((k,v) -> v.contains(","))
                .selectKey((k,v) -> v.split(",")[0].toLowerCase())
                .mapValues(value ->  value.split(",")[1])
                .filter((user, color) -> colors.contains(color));

        usersAndColors.to("user-color-keys-colors");

        KTable<String, String> userAndColorsTable = builder.table("user-color-keys-colors");

        KTable<String, Long> colorsTable = userAndColorsTable
                .groupBy((user, color) -> new KeyValue<String, String>(color, color))
                .count();

        colorsTable.toStream().to("favorite-color-output", Produced.with(stringSerde, longSerde));

        final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

        System.out.println(streams.toString());
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}
