package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;


public class AnomaliesListenerTest extends AbstractIntegrationTest {

    @Value("${kafka.topic}")
    private String inputTopic;

    @Test
    void testInFlow() {
        try (TestKafkaProducer<Anomaly> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            Anomaly anomaly = new Anomaly(20, "room", "kafkaTestThermometer", Instant.parse("2023-01-01T00:00:00.000Z"));

            producer.produce(anomaly.thermometerId(), anomaly);

        }
    }
}
