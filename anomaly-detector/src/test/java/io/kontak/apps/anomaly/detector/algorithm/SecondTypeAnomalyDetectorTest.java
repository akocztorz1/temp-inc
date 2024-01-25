package io.kontak.apps.anomaly.detector.algorithm;

import io.kontak.apps.anomaly.detector.algorithm.SecondTypeAnomalyDetector;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SecondTypeAnomalyDetectorTest {

    private static TemperatureReading reading1;
    private static  TemperatureReading reading2;
    private static  TemperatureReading reading3;
    private static  TemperatureReading reading4;
    private static  TemperatureReading reading5;
    private static  TemperatureReading reading6;
    private static  TemperatureReading reading7;
    private static  TemperatureReading reading8;
    private static  TemperatureReading reading9;
    private static  TemperatureReading reading10;
    private static  TemperatureReading reading11;
    private static  TemperatureReading reading12;
    private static  TemperatureReading reading13;
    private static  TemperatureReading reading14;


    @BeforeAll
    static void setupTestData() {
        String roomId = "room";
        String thermometerId = "thermometer";
        reading1 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading2 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading3 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading4 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading5 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading6 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading7 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading8 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading9 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading10 = new TemperatureReading(20, roomId, thermometerId, Instant.now());
        reading11 = new TemperatureReading(30, roomId, thermometerId, Instant.now());
        reading12 = new TemperatureReading(40, roomId, thermometerId, Instant.now());
        reading12 = new TemperatureReading(40, roomId, thermometerId, Instant.now());
        reading13 = new TemperatureReading(40, roomId, "thermometer2", Instant.now());
        reading14 = new TemperatureReading(40, roomId, thermometerId, Instant.now().plusSeconds(20));

    }

    @Test
    void shouldNotFoundAnomaly() {
        SecondTypeAnomalyDetector detector = new SecondTypeAnomalyDetector();
        Optional<Anomaly> result = detector.apply(List.of(reading1, reading2, reading3, reading4, reading5, reading6, reading7, reading8, reading9, reading10));

        assertThat(result.isPresent()).isEqualTo(false);
    }

    @Test
    void shouldFoundAnomaly() {
        SecondTypeAnomalyDetector detector = new SecondTypeAnomalyDetector();
        Optional<Anomaly> result = detector.apply(List.of(reading1, reading2, reading3, reading4, reading5, reading6, reading7, reading8, reading9, reading11));
        Optional<Anomaly> result2 = detector.apply(List.of(reading12));

        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().temperature()).isEqualTo(reading11.temperature());
        assertThat(result.get().timestamp()).isEqualTo(reading11.timestamp());
        assertThat(result2.isPresent()).isEqualTo(true);
        assertThat(result2.get().temperature()).isEqualTo(reading12.temperature());
        assertThat(result2.get().timestamp()).isEqualTo(reading12.timestamp());
    }

    @Test
    void shouldNotFoundAnomalyDueToBigTimeGap() {
        SecondTypeAnomalyDetector detector = new SecondTypeAnomalyDetector();
        Optional<Anomaly> result = detector.apply(List.of(reading1, reading2, reading3, reading4, reading5, reading6, reading7, reading8, reading9, reading11));
        Optional<Anomaly> result2 = detector.apply(List.of(reading14));

        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().temperature()).isEqualTo(reading11.temperature());
        assertThat(result.get().timestamp()).isEqualTo(reading11.timestamp());
        assertThat(result2.isPresent()).isEqualTo(false);
    }

    @Test
    void shouldFoundAnomalyOnlyForOneThermometer() {
        SecondTypeAnomalyDetector detector = new SecondTypeAnomalyDetector();
        Optional<Anomaly> result = detector.apply(List.of(reading1, reading2, reading3, reading4, reading5, reading6, reading7, reading8, reading9, reading11));
        Optional<Anomaly> result2 = detector.apply(List.of(reading13));

        assertThat(result.isPresent()).isEqualTo(true);
        assertThat(result.get().temperature()).isEqualTo(reading11.temperature());
        assertThat(result.get().timestamp()).isEqualTo(reading11.timestamp());
        assertThat(result2.isPresent()).isEqualTo(false);
    }

}
