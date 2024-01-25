package io.kontak.apps.anomaly.detector.algorithm;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface AnomalyDetector extends Function<List<TemperatureReading>, Optional<Anomaly>> {

    static final int MAX_TEMPERATURE_DIFFERENCE = 5;
    static final int FIRST_ALGORITHM_PACKAGE_SIZE = 10;


}
