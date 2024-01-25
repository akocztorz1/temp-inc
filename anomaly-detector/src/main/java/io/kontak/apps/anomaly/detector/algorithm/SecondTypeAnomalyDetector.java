package io.kontak.apps.anomaly.detector.algorithm;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SecondTypeAnomalyDetector implements AnomalyDetector {

    private Map<String, List<TemperatureReading>> readings = new HashMap<>();

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        temperatureReadings.forEach(r -> {
            if (readings.containsKey(r.thermometerId()))
                readings.get(r.thermometerId()).add(r);
            else {
                List<TemperatureReading> t = new ArrayList<>();
                t.add(r);
                readings.put(r.thermometerId(), t);
            }
        });
        readings.forEach((id, tr) -> {
            tr.sort(Comparator.comparing(TemperatureReading::timestamp));
        });
        List<Anomaly> anomaliesList = new ArrayList<>();
        temperatureReadings.forEach(tr -> {
            List<TemperatureReading> filtered = readings.get(tr.thermometerId()).stream()
                    .filter(t -> t.timestamp().plus(10, ChronoUnit.SECONDS).isAfter(tr.timestamp()))
                    .collect(Collectors.toList());
            double avgTemperature = filtered.stream().mapToDouble(TemperatureReading::temperature).sum() / (filtered.size());
            if (avgTemperature + MAX_TEMPERATURE_DIFFERENCE < tr.temperature()) {
                anomaliesList.add(new Anomaly(tr));
            }
        });
        if (anomaliesList.isEmpty())
            return Optional.empty() ;
        else
            return Optional.of(anomaliesList.get(0));
    }
}
