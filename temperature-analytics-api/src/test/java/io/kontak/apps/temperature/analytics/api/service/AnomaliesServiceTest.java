package io.kontak.apps.temperature.analytics.api.service;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.temperature.analytics.api.model.AnomaliesCountByThermometer;
import io.kontak.apps.temperature.analytics.api.model.AnomalyDB;
import io.kontak.apps.temperature.analytics.api.repository.AnomaliesRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

public class AnomaliesServiceTest {

    @Mock
    private AnomaliesRepository repository;

    private AnomaliesService service;

    private static Random random;
    private static int threshold;
    private static UUID anomalyId1;
    private static UUID anomalyId2;
    private static UUID anomalyId3;
    private static String roomId;
    private static String thermometerId1;
    private static String thermometerId2;
    private static double temperature1;
    private static double temperature2;
    private static double temperature3;
    private static Instant timestamp1;
    private static Instant timestamp2;
    private static Instant timestamp3;
    private static AnomalyDB anomalyDB1;
    private static AnomalyDB anomalyDB2;
    private static AnomalyDB anomalyDB3;
    private static Anomaly anomaly1;
    private static Anomaly anomaly2;

    @BeforeAll
    static void setupTestData() {
        random = new Random();
        threshold = random.nextInt(20);
        anomalyId1 = UUID.randomUUID();
        anomalyId2 = UUID.randomUUID();
        anomalyId3 = UUID.randomUUID();
        roomId = UUID.randomUUID().toString();
        thermometerId1 = UUID.randomUUID().toString();
        thermometerId2 = UUID.randomUUID().toString();
        temperature1 = random.nextDouble(30)*10;
        temperature2 = random.nextDouble(threshold);
        temperature3 = random.nextDouble(threshold);
        timestamp1 = Instant.now();
        timestamp2 = Instant.now().minusSeconds(180);
        timestamp3 = Instant.now().minusSeconds(10);
        anomalyDB1 = new AnomalyDB(roomId, thermometerId1, temperature1, timestamp1);
        anomalyDB1.setId(anomalyId1);
        anomalyDB2 = new AnomalyDB(roomId, thermometerId1, temperature2, timestamp2);
        anomalyDB2.setId(anomalyId2);
        anomalyDB3 = new AnomalyDB(roomId, thermometerId2, temperature3, timestamp3);
        anomalyDB3.setId(anomalyId3);
        anomaly1 = new Anomaly(temperature1, roomId, thermometerId1, timestamp1);
        anomaly2 = new Anomaly(temperature2, roomId, thermometerId1, timestamp2);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AnomaliesService(repository);
    }

    @Test
    void shouldGetAllAnomalies() {
        given(repository.findAll()).willReturn(List.of(anomalyDB1, anomalyDB2));
        List<Anomaly> result = service.getAllAnomalies();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(anomaly1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(anomaly2);
    }

    @Test
    void shouldGetAnomaliesByRoomId() {
        Anomaly anomaly1 = new Anomaly(temperature1, roomId, thermometerId1, timestamp1);
        Anomaly anomaly2 = new Anomaly(temperature2, roomId, thermometerId1, timestamp2);
        given(repository.findByRoomId(roomId)).willReturn(List.of(anomalyDB1, anomalyDB2));

        List<Anomaly> result = service.getAnomaliesByRoomId(roomId);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(anomaly1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(anomaly2);
    }

    @Test
    void shouldGetAnomaliesByThermometerId() {
        given(repository.findByThermometerId(thermometerId1)).willReturn(List.of(anomalyDB1, anomalyDB2));

        List<Anomaly> result = service.getAnomaliesByThermometerId(thermometerId1);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(anomaly1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(anomaly2);
    }

    @Test
    void shouldGetAnomaliesCountForThermometerIdWithoutThreshold() {
        AnomaliesCountByThermometer count1 = new AnomaliesCountByThermometer(thermometerId1, 2L);
        AnomaliesCountByThermometer count2 = new AnomaliesCountByThermometer(thermometerId2, 1L);
        given(repository.findAll()).willReturn(List.of(anomalyDB1, anomalyDB2, anomalyDB3));

        List<AnomaliesCountByThermometer> result = service.getAllThermometersWithAnomaliesCount(Optional.empty());

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(count1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(count2);
    }

    @Test
    void shouldGetAnomaliesCountForThermometerIdWithThreshold() {
        AnomaliesCountByThermometer count1 = new AnomaliesCountByThermometer(thermometerId1, 1L);
        AnomaliesCountByThermometer count2 = new AnomaliesCountByThermometer(thermometerId2, 0L);
        given(repository.findAll()).willReturn(List.of(anomalyDB1, anomalyDB2, anomalyDB3));

        List<AnomaliesCountByThermometer> result = service.getAllThermometersWithAnomaliesCount(Optional.of(String.valueOf(threshold)));

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualToComparingFieldByField(count1);
        assertThat(result.get(1)).isEqualToComparingFieldByField(count2);
    }

}
