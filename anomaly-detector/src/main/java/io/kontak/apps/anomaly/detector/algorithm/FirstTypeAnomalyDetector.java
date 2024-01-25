package io.kontak.apps.anomaly.detector.algorithm;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Primary
public class FirstTypeAnomalyDetector implements AnomalyDetector {

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
            if (tr.size() > FIRST_ALGORITHM_PACKAGE_SIZE) {
                int amount = tr.size() - FIRST_ALGORITHM_PACKAGE_SIZE;
                for (int i = 0; i < amount; i++) {
                    tr.remove(0);
                }
            }
        });
        List<Anomaly> anomaliesList = new ArrayList<>();
        temperatureReadings.stream().forEach(tr -> {
            List<TemperatureReading> temp = readings.get(tr.thermometerId());
            if (temp.size() >= FIRST_ALGORITHM_PACKAGE_SIZE) {
                Optional<Anomaly> anomalyOpt = checkIfAnomalyExists(temp, tr);
                anomalyOpt.ifPresent(anomaliesList::add);
            }
        });
        if (anomaliesList.isEmpty())
            return Optional.empty() ;
        else
            return Optional.of(anomaliesList.get(0));
    }

    private Optional<Anomaly> checkIfAnomalyExists(List<TemperatureReading> temp, TemperatureReading current) {
        double avg = temp.stream().filter(t -> t != current)
                .mapToDouble(TemperatureReading::temperature).sum() / (temp.size() - 1);
        if (current.temperature() > avg + MAX_TEMPERATURE_DIFFERENCE)
            return Optional.of(new Anomaly(current));
        else return Optional.empty();
    }
}
