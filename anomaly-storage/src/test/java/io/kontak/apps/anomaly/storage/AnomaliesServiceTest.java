package io.kontak.apps.anomaly.storage;

import io.kontak.apps.anomaly.storage.db.AnomaliesRepository;
import io.kontak.apps.anomaly.storage.db.AnomalyDB;
import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class AnomaliesServiceTest extends AbstractIntegrationTest {

        @Autowired
        private AnomaliesRepository repository;

        private AnomaliesService service;

        @Test
        void testSave() {
            service = new AnomaliesService(repository);
            Anomaly anomaly = new Anomaly(20, "room", "testThermometer", Instant.parse("2023-01-01T00:00:00.000Z"));

            service.apply(anomaly);
            List<AnomalyDB> result = repository.findAll().stream()
                    .filter(d -> d.getThermometerId().toString().equals("testThermometer"))
                    .collect(Collectors.toList());
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getRoomId()).isEqualTo(anomaly.roomId());
            assertThat(result.get(0).getTemperature()).isEqualTo(anomaly.temperature());
            assertThat(result.get(0).getTimestamp()).isEqualTo(anomaly.timestamp());
            repository.deleteAll(result);
        }
}
