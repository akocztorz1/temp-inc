package io.kontak.apps.temperature.analytics.api.service;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.temperature.analytics.api.model.AnomaliesCountByThermometer;
import io.kontak.apps.temperature.analytics.api.model.AnomalyDB;
import io.kontak.apps.temperature.analytics.api.repository.AnomaliesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnomaliesService {

    private AnomaliesRepository repository;

    @Autowired
    public AnomaliesService(AnomaliesRepository anomaliesRepository) {
        this.repository = anomaliesRepository;
    }

    public List<Anomaly> getAnomaliesByThermometerId(String thermometerId) {
        return repository.findByThermometerId(thermometerId).stream()
                .map(anomaly -> new Anomaly(anomaly.getTemperature(), anomaly.getRoomId(), anomaly.getThermometerId(), anomaly.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<Anomaly> getAnomaliesByRoomId(String roomId) {
        return repository.findByRoomId(roomId).stream()
                .map(anomaly -> new Anomaly(anomaly.getTemperature(), anomaly.getRoomId(), anomaly.getThermometerId(), anomaly.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<Anomaly> getAllAnomalies() {
        List<AnomalyDB> resp = (List<AnomalyDB>) repository.findAll();
        return resp.stream()
                .map(anomaly -> new Anomaly(anomaly.getTemperature(), anomaly.getRoomId(), anomaly.getThermometerId(), anomaly.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<AnomaliesCountByThermometer> getAllThermometersWithAnomaliesCount(Optional<String> threshold) {
        List<AnomalyDB> resp = (List<AnomalyDB>) repository.findAll();
        Map<String, Long> resultMap = resp.stream()
                .filter(anomaly -> filterAnomalies(threshold, anomaly))
                .collect(Collectors.groupingBy(AnomalyDB::getThermometerId, Collectors.counting()));
        return resp.stream().map(a -> a.getThermometerId()).distinct().collect(Collectors.toList()).stream()
                .map(key -> new AnomaliesCountByThermometer(key, resultMap.getOrDefault(key, 0L))).collect(Collectors.toList());
    }

    private boolean filterAnomalies(Optional<String> threshold, AnomalyDB anomaly) {
        if(threshold.isPresent()) {
            double t = Double.parseDouble(threshold.get());
            if (anomaly.getTemperature() > t) return true;
            return false;
        }
        return true;
    }
}
