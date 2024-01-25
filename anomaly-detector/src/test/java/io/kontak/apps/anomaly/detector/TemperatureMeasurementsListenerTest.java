package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;

public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<TemperatureReading> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            TemperatureReading temperatureReading0 = new TemperatureReading(20, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            TemperatureReading temperatureReading1 = new TemperatureReading(21, "room", "thermometer", Instant.parse("2023-01-01T00:00:01.000Z"));
            TemperatureReading temperatureReading2 = new TemperatureReading(22, "room", "thermometer", Instant.parse("2023-01-01T00:00:02.000Z"));
            TemperatureReading temperatureReading3 = new TemperatureReading(20, "room", "thermometer", Instant.parse("2023-01-01T00:00:03.000Z"));
            TemperatureReading temperatureReading4 = new TemperatureReading(19, "room", "thermometer", Instant.parse("2023-01-01T00:00:04.000Z"));
            TemperatureReading temperatureReading5 = new TemperatureReading(18.3, "room", "thermometer", Instant.parse("2023-01-01T00:00:05.000Z"));
            TemperatureReading temperatureReading6 = new TemperatureReading(19.8, "room", "thermometer", Instant.parse("2023-01-01T00:00:06.000Z"));
            TemperatureReading temperatureReading7 = new TemperatureReading(20.2, "room", "thermometer", Instant.parse("2023-01-01T00:00:07.000Z"));
            TemperatureReading temperatureReading8 = new TemperatureReading(28, "room", "thermometer", Instant.parse("2023-01-01T00:00:08.000Z"));
            TemperatureReading temperatureReading9 = new TemperatureReading(20.5, "room", "thermometer", Instant.parse("2023-01-01T00:00:09.000Z"));
            TemperatureReading temperatureReading10 = new TemperatureReading(27, "room", "thermometer", Instant.parse("2023-01-01T00:00:10.000Z"));

            producer.produce(temperatureReading0.thermometerId(), temperatureReading0);
            producer.produce(temperatureReading1.thermometerId(), temperatureReading1);
            producer.produce(temperatureReading2.thermometerId(), temperatureReading2);
            producer.produce(temperatureReading3.thermometerId(), temperatureReading3);
            producer.produce(temperatureReading4.thermometerId(), temperatureReading4);
            producer.produce(temperatureReading5.thermometerId(), temperatureReading5);
            producer.produce(temperatureReading6.thermometerId(), temperatureReading6);
            producer.produce(temperatureReading7.thermometerId(), temperatureReading7);
            producer.produce(temperatureReading8.thermometerId(), temperatureReading8);
            producer.produce(temperatureReading9.thermometerId(), temperatureReading9);
            producer.produce(temperatureReading10.thermometerId(), temperatureReading10);
            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> r.value().temperature() == temperatureReading10.temperature()),
                    Duration.ofSeconds(5)
            );
        }
    }
}
