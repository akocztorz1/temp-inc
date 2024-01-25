package io.kontak.apps.anomaly.storage;

import io.kontak.apps.anomaly.storage.db.AnomaliesRepository;
import io.kontak.apps.anomaly.storage.db.AnomalyDB;
import io.kontak.apps.event.Anomaly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AnomaliesService implements IAnomaliesService {

    private AnomaliesRepository repository;

    @Autowired
    public AnomaliesService(AnomaliesRepository anomaliesRepository) {
        this.repository = anomaliesRepository;
    }

    @Override
    public Optional<Anomaly> apply(Anomaly anomaly) {
        saveAnomaly(anomaly);
        return Optional.empty();
    }

    private void saveAnomaly(Anomaly anomaly) {
        System.out.println(anomaly);
        repository.save(new AnomalyDB(anomaly));
    }
}
